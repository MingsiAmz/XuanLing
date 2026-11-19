package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy.Type;
import com.mingsiamz.xuanlingmod.network.BindSkillPacket;
import com.mingsiamz.xuanlingmod.network.ManageDataPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.network.TribulationFxPacket;
import com.mingsiamz.xuanlingmod.event.AutoGenerationEvents;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterEffects;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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

    /** 参悟读条开始时的位置，用于检测玩家移动打断 */
    private static final Map<UUID, Vec3> COMPREHENSION_POS = new HashMap<>();

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
            DefaultEnergy energy = new DefaultEnergy();
            // C2：新玩家初始内力 10，使左上角能量条能够显示
            energy.setMaxValueDirect(Type.CHILD, 10);
            energy.setEnergyValue(Type.CHILD, 10);
            player.setData(RegisterAttachments.DEFAULT_ENERGY, energy);

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
     * Issue 10：玩家死亡时重置并随机所有属性；全部移除自定义 buff。
     * C1：玩家击杀生物时获得修为，凡人期亦可获得，修为条随之上涨。
     */
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Entity killer = event.getSource() != null ? event.getSource().getEntity() : null;

        // C1：仅 level 0 凡人击杀生物可获得修为，按目标生命值与护甲值的平滑曲线计算
        // level ≥1 杀怪不再增加修为（修炼改由参悟/闭关获得）
        if (entity instanceof LivingEntity victim && killer instanceof Player killerPlayer) {
            if (killerPlayer instanceof ServerPlayer serverKiller
                            && killerPlayer.hasData(RegisterAttachments.BASIC_DATA)) {
                BasicData killerData = RegisterAttachments.getBasicData(killerPlayer);
                if (killerData != null && killerData.getRealmLevel() == 0) {
                    // 平滑曲线：base × sqrt(最大生命值 + 护甲值)，避免高血量生物修为线性失控
                    double healthAndArmor = victim.getMaxHealth() + victim.getArmorValue();
                    double gain = Config.getKillRealmExpBase() * Math.sqrt(healthAndArmor);
                    killerData.setRealmExp(killerData.getRealmExp() + gain);
                    XuanLing.LOGGER.info("{} 击杀生物获得 {} 修为（生命值+护甲 {}），境界等级 {}",
                                    killerPlayer.getName().getString(), gain, healthAndArmor,
                                    killerData.getRealmLevel());

                    // 击杀后检查突破，允许凡人直接突破到练气（level<4 不渡天劫）
                    checkBreakthrough(killerPlayer, killerData, killerPlayer.level(), serverKiller);

                    // 重新附加 BasicData，触发客户端附件同步（使 statgui 修为条实时推进）
                    killerPlayer.setData(RegisterAttachments.BASIC_DATA, BasicData.copy(killerData));
                }
            }
        }

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

        // 移除全部自定义 buff（走火入魔、重伤等）
        player.removeEffect(RegisterEffects.EFFECT_DEVIATION);
        player.removeEffect(RegisterEffects.EFFECT_CRITICAL_INJURY);

        // 重置并随机所有属性
        data.resetAndRandomize();
        XuanLing.LOGGER.info("{} 死亡，重置并随机所有属性，移除全部自定义 buff", player.getName().getString());
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

        // D2：灵感被动恢复，闭关修炼额外恢复（按消耗时间计算增加）
        double inspirationGain = Config.getInspirationGainPerTick();
        if (data.isMeditation()) {
            inspirationGain += Config.getInspirationMeditationGain();
        }
        data.setInspiration(data.getInspiration() + inspirationGain);

        // 参悟读条与实时削寿（Issue 08）：读条期间按滑动条强度削减寿命
        // 参悟顿悟与走火入魔（Issue 09）：随机顿悟进度×2，打断后施加走火入魔
        if (data.isComprehending() && data.getComprehensionTick() > 0) {
            int tick = data.getComprehensionTick();
            int maxTick = data.getComprehensionMaxTick();
            float intensity = data.getComprehensionIntensity();

            // 记录读条起始位置，用于移动打断检测
            if (tick == maxTick && maxTick > 0) {
                COMPREHENSION_POS.put(player.getUUID(), player.position());
            }

            // 移动打断：玩家离开读条起始方块即判定打断
            Vec3 startPos = COMPREHENSION_POS.get(player.getUUID());
            if (startPos != null && !player.blockPosition().equals(BlockPos.containing(startPos.x, startPos.y, startPos.z))) {
                interruptComprehension(player, data);
                checkBreakthrough(player, data, level, serverPlayer);
                return;
            }

            // 随机顿悟：进度 ×2
            boolean enlightened = level.random.nextDouble() < Config.ENLIGHTENMENT_CHANCE.get();
            // 自发走火入魔：概率触发，直接打断
            boolean deviated = level.random.nextDouble() < Config.DEVIATION_CHANCE.get();

            if (deviated) {
                interruptComprehension(player, data);
                checkBreakthrough(player, data, level, serverPlayer);
                return;
            }

            // 推进读条进度：顿悟时进度 ×2
            int progressStep = enlightened ? 2 : 1;
            if (enlightened) {
                XuanLing.LOGGER.info("{} 参悟顿悟，进度 ×2", player.getName().getString());
            }
            int newTick = tick - progressStep;

            // 实时削减寿命：总削减 = COMPREHENSION_LIFESPAN_COST × intensity 月，按读条进度分摊至每 tick
            if (maxTick > 0) {
                double cost = Config.COMPREHENSION_LIFESPAN_COST.get();
                int elapsed = maxTick - tick; // 本轮已流逝的 tick
                int drainInterval = Math.max(1, (int) Math.ceil(maxTick / Math.max(0.001, cost * intensity)));
                if (elapsed > 0 && elapsed % drainInterval == 0) {
                    data.setLifespanMonth(data.getLifespanMonth() - 1);
                }
            }

            if (newTick <= 0) {
                // 读条结束：参悟完成，重置读条状态（客户端滑动条随之复位）
                String target = data.getComprehensionTarget();
                data.setComprehending(false);
                data.setComprehensionTick(0);
                data.setComprehensionTarget("");
                data.setComprehensionIntensity(0f);
                data.setComprehensionMaxTick(0);
                COMPREHENSION_POS.remove(player.getUUID());
                XuanLing.LOGGER.info("{} 参悟完成：{}", player.getName().getString(), target);
            } else {
                data.setComprehensionTick(newTick);
            }
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
                                RegisterEffects.EFFECT_DEVIATION, data.getDeviationTick(), 0, false, true));
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
     * 打断参悟：停止参悟与修炼，并施加走火入魔 debuff。
     */
    private static void interruptComprehension(Player player, BasicData data) {
        data.setComprehending(false);
        data.setComprehensionTick(0);
        data.setComprehensionTarget("");
        data.setComprehensionIntensity(0f);
        data.setComprehensionMaxTick(0);
        // 停止修炼（打坐）
        data.setMeditation(false);
        if (player.getEffect(RegisterEffects.EFFECT_MEDITATION) != null) {
            player.removeEffect(RegisterEffects.EFFECT_MEDITATION);
        }
        COMPREHENSION_POS.remove(player.getUUID());
        // 施加走火入魔 debuff（仅“走火入魔”显示在效果栏，底层负面效果由 EffectDeviation 以不可见方式叠加）
        data.setDeviating(true);
        data.setDeviationTick(Config.getDeviationDebuffDurationTick());
        player.addEffect(new MobEffectInstance(
                        RegisterEffects.EFFECT_DEVIATION, data.getDeviationTick(), 0, false, true));
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
