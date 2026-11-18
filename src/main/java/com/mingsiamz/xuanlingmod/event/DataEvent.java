package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.network.BindSkillPacket;
import com.mingsiamz.xuanlingmod.network.ManageDataPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.network.TribulationFxPacket;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterEffects;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = XuanLing.MODID)
public class DataEvent {

    private static final String[] SYNC_KEYS = {
            XuanLing.MODID + XuanLingTypeEnum.SPELL.type,
            XuanLing.MODID + XuanLingTypeEnum.ART.type,
            BindSkillPacket.BIND_KEY,
            ManageDataPacket.ACTIVE_ART_KEY
    };

    private static void syncToClient(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer))
            return;
        for (String key : SYNC_KEYS)
            Networking.syncPersistentData(serverPlayer, key);
    }

    @SubscribeEvent
    public static void onPlayerJoinGame(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.hasData(RegisterAttachments.BASIC_DATA)) {
            BasicData basicData = BasicData.initData();
            basicData.setNormalLife(90);
            basicData.setSoulLife(90);
            player.setData(RegisterAttachments.BASIC_DATA, basicData);

        }
        if (!player.hasData(RegisterAttachments.DEFAULT_DATA)) {
            player.setData(RegisterAttachments.DEFAULT_DATA, new DefaultData());
        }
        if (!player.hasData(RegisterAttachments.DEFAULT_ENERGY)) {
            player.setData(RegisterAttachments.DEFAULT_ENERGY, new DefaultEnergy());
        }

        syncToClient(player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        syncToClient(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncToClient(event.getEntity());
    }

    /**
     * Issue 07：玩家死亡时全部移除自定义 buff（走火入魔、重伤等）。
     */
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }
        if (!player.hasData(RegisterAttachments.BASIC_DATA)) {
            return;
        }
        BasicData data = RegisterAttachments.getBasicData(player);
        if (data == null) {
            return;
        }

        // 移除走火入魔
        if (data.isDeviating()) {
            player.removeEffect(RegisterEffects.EFFECT_DEVIATION);
            data.setDeviating(false);
            data.setDeviationTick(0);
        }
        // 移除重伤
        if (data.isCriticalInjured()) {
            player.removeEffect(RegisterEffects.EFFECT_CRITICAL_INJURY);
            data.setCriticalInjured(false);
            data.setCriticalInjuryTick(0);
        }
    }

    @SubscribeEvent
    public static void onEntityWasDeath(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            if (!entity.isAlive()) {
                entity.getData(RegisterAttachments.DEFAULT_DATA);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) {
            return;
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (!player.hasData(RegisterAttachments.BASIC_DATA)) {
            return;
        }
        BasicData data = RegisterAttachments.getBasicData(player);
        if (data == null) {
            return;
        }

        // 服务端寿命消耗：每 7 天减 1 月
        long currentDay = level.getDayTime() / 24000L;
        int daysPerMonth = Config.LIFESPAN_DAYS_PER_MONTH.get();
        long lastDay = data.getLastLifespanCheckDay();
        if (currentDay - lastDay >= daysPerMonth) {
            long months = (currentDay - lastDay) / daysPerMonth;
            data.setLifespanMonth(data.getLifespanMonth() - (int) months);
            data.setLastLifespanCheckDay(lastDay + months * daysPerMonth);
        }

        // 境界修为累积与突破（仅在参悟时获得修为）
        if (data.isMeditation()) {
            double insight = data.getInsight();
            double tpa0 = data.getTPAO();
            // 修炼速度：base × insight^0.6 × TPAO^0.4
            double speedGain = 0.1 * Math.pow(insight, 0.6) * Math.pow(tpa0, 0.4);
            // 重伤 debuff：修炼效率降至原来的 1/5（Config 默认 0.2）
            if (data.isCriticalInjured()) {
                speedGain *= Config.getInjuryDebuffCultivationPenalty();
            }
            data.setRealmExp(data.getRealmExp() + speedGain);
        }

        // 检查境界突破
        checkBreakthrough(player, data, level, serverPlayer);

        // 天劫进行中
        if (data.isTribulating()) {
            handleTribulation(player, data, level, serverPlayer);
        }

        // 走火入魔倒计时
        if (data.isDeviating() && data.getDeviationTick() > 0) {
            data.setDeviationTick(data.getDeviationTick() - 1);
            // Issue 07：死亡前不可清除，若效果被移除则重新施加
            if (player.getEffect(RegisterEffects.EFFECT_DEVIATION) == null) {
                player.addEffect(new MobEffectInstance(
                                RegisterEffects.EFFECT_DEVIATION, data.getDeviationTick(), 0, false, false));
            }
            if (data.getDeviationTick() <= 0) {
                data.setDeviating(false);
                player.removeEffect(RegisterEffects.EFFECT_DEVIATION);
            }
        }

        // 重伤倒计时
        if (data.isCriticalInjured() && data.getCriticalInjuryTick() > 0) {
            data.setCriticalInjuryTick(data.getCriticalInjuryTick() - 1);
            // Issue 07：死亡前不可清除，若效果被移除则重新施加
            if (player.getEffect(RegisterEffects.EFFECT_CRITICAL_INJURY) == null) {
                player.addEffect(new MobEffectInstance(
                                RegisterEffects.EFFECT_CRITICAL_INJURY, data.getCriticalInjuryTick(), 0, false, false));
            }
            if (data.getCriticalInjuryTick() <= 0) {
                data.setCriticalInjured(false);
                player.removeEffect(RegisterEffects.EFFECT_CRITICAL_INJURY);
            }
        }
    }

    /**
     * 检查是否可以境界突破，若达到所需修为则开始突破流程
     */
    private static void checkBreakthrough(Player player, BasicData data, Level level, ServerPlayer serverPlayer) {
        int currentLevel = data.getRealmLevel();
        double needed = Config.needExp(currentLevel);
        if (data.getRealmExp() < needed) {
            return;
        }

        // 已达到突破条件
        int tribulationMinLevel = Config.TRIBULATION_MIN_LEVEL.get();
        if (currentLevel >= tribulationMinLevel) {
            // 极高道德值可无需渡劫，直接突破
            int maxMorality = Config.getMaxValue(Config.MORALITY_MAX);
            if (data.getMorality() >= maxMorality * 0.9) {
                data.setTribulating(false);
                data.setTribulationTick(0);
                data.setRealmLevel(currentLevel + 1);
                data.setRealmExp(0.0);
                XuanLing.LOGGER.info("{} 道德值极高，无需渡劫直接突破至 {}", player.getName().getString(), currentLevel + 1);
                return;
            }
            // level4+ 需要渡天劫
            data.setTribulating(true);
            data.setTribulationTick(400); // 20秒天劫
            data.setRealmExp(0.0); // 重置修为，突破成功后再计入
            XuanLing.LOGGER.info("{} 开始渡天劫，境界等级 {}", player.getName().getString(), currentLevel);
        } else {
            // level4 以下直接突破
            data.setRealmLevel(currentLevel + 1);
            data.setRealmExp(0.0);
            XuanLing.LOGGER.info("{} 境界突破至 {}", player.getName().getString(), currentLevel + 1);
        }
    }

    /**
     * 处理天劫过程：定时召唤闪电，造成伤害，倒计时结束后突破成功。
     * 若玩家在渡劫期间死亡，则判定为天劫失败：按平滑曲线扣寿并施加重伤 debuff。
     */
    private static void handleTribulation(Player player, BasicData data, Level level, ServerPlayer serverPlayer) {
        int tick = data.getTribulationTick();
        if (tick <= 0) {
            // 天劫结束，突破成功
            data.setTribulating(false);
            data.setTribulationTick(0);
            int newLevel = data.getRealmLevel() + 1;
            data.setRealmLevel(newLevel);
            data.setRealmExp(0.0);
            XuanLing.LOGGER.info("{} 渡天劫成功，境界突破至 {}", player.getName().getString(), newLevel);
            return;
        }

        // 玩家在渡劫期间死亡：天劫失败
        if (!player.isAlive()) {
            data.setTribulating(false);
            data.setTribulationTick(0);
            // 按平滑曲线扣除寿命
            int realmLevel = data.getRealmLevel();
            double deduction = Config.getTribulationLifespanDeduction(realmLevel);
            data.setLifespanMonth(data.getLifespanMonth() - (int) deduction);
            // 施加重伤 debuff
            int injuryDuration = Config.getInjuryDebuffDurationTick();
            data.setCriticalInjured(true);
            data.setCriticalInjuryTick(injuryDuration);
            player.addEffect(new MobEffectInstance(
                            RegisterEffects.EFFECT_CRITICAL_INJURY, injuryDuration, 0, false, false));
            XuanLing.LOGGER.info("{} 渡天劫失败，扣除 {} 月寿命并获得重伤 debuff，境界等级 {}", player.getName().getString(),
                            (int) deduction, realmLevel);
            return;
        }

        // 每 40 tick (2秒) 一道天雷
        if (tick % 40 == 0) {
            if (level instanceof ServerLevel) {
                BlockPos pos = player.blockPosition();
                // 闪电稍微偏移，避免直接叠在玩家头上
                BlockPos strikePos = pos.offset(
                        (int) (level.random.nextDouble() * 6 - 3),
                        0,
                        (int) (level.random.nextDouble() * 6 - 3));
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create((ServerLevel) level);
                if (lightning != null) {
                    lightning.moveTo(strikePos.getX(), strikePos.getY(), strikePos.getZ());
                    lightning.setVisualOnly(false);
                    level.addFreshEntity(lightning);
                }

                // 客户端 photon2 雷电特效（仅客户端渲染）
                TribulationFxPacket.send(player);

                // 天雷造成伤害（根据天谴值与善恶标签调整）
                double condemnation = data.getCondemnation();
                double baseDamage = 6.0; // 3颗心
                double damage = baseDamage + condemnation * 0.01; // 天谴值越高伤害越大
                // 善标签减免，恶标签增加，中立不变
                switch (data.getAlignmentLabel()) {
                    case GOOD -> damage *= 0.5;
                    case EVIL -> damage *= 1.5;
                    default -> {}
                }
                player.hurt(level.damageSources().lightningBolt(), (float) damage);
                XuanLing.LOGGER.info("天劫雷劈 {}，造成 {} 伤害", player.getName().getString(), damage);
            }
        }

        data.setTribulationTick(tick - 1);
    }
}
