package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.network.BindSkillPacket;
import com.mingsiamz.xuanlingmod.network.ManageDataPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LightningBolt;
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

    @SubscribeEvent
    public static void onEntityWasDeath(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            if (!entity.isAlive()) {
                entity.getData(RegisterAttachments.DEFAULT_DATA);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
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
            data.setRealmExp(data.getRealmExp() + speedGain);
        }

        // 检查境界突破
        checkBreakthrough(player, data, level, serverPlayer);

        // 天劫进行中
        if (data.isTribulating()) {
            handleTribulation(player, data, level, serverPlayer);
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
     * 处理天劫过程：定时召唤闪电，造成伤害，倒计时结束后突破成功
     */
    private static void handleTribulation(Player player, BasicData data, Level level, ServerPlayer serverPlayer) {
        int tick = data.getTribulationTick();
        if (tick <= 0) {
            // 天劫结束，突破成功
            data.setTribulating(false);
            data.setTribulationTick(0);
            data.setRealmLevel(data.getRealmLevel() + 1);
            data.setRealmExp(0.0);
            XuanLing.LOGGER.info("{} 渡天劫成功，境界突破至 {}", player.getName().getString(), data.getRealmLevel());
            return;
        }

        // 每 40 tick (2秒) 一道天雷
        if (tick % 40 == 0) {
            if (level instanceof ServerLevel serverLevel) {
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

                // 天雷造成伤害（根据天谴值调整）
                double condemnation = data.getCondemnation();
                double baseDamage = 6.0; // 3颗心
                double damage = baseDamage + condemnation * 0.01; // 天谴值越高伤害越大
                player.hurt(level.damageSources().lightningBolt(), (float) damage);
                XuanLing.LOGGER.info("天劫雷劈 {}，造成 {} 伤害", player.getName().getString(), damage);
            }
        }

        data.setTribulationTick(tick - 1);
    }
}
