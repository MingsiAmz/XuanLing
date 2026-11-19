package com.mingsiamz.xuanlingmod;

import java.util.function.Supplier;

import com.mingsiamz.xuanlingmod.animations.player.AnmMeditation;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.gui.BattleModeHud;
import com.mingsiamz.xuanlingmod.gui.StateGui;
import com.mingsiamz.xuanlingmod.network.EnergyMultiplierPacket;
import com.mingsiamz.xuanlingmod.network.MeditationPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.network.SkillCastPacket;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterEntities;
import com.mingsiamz.xuanlingmod.register.RegisterKey;
import com.mingsiamz.xuanlingmod.render.SkillEntityRender;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = XuanLing.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = XuanLing.MODID, value = Dist.CLIENT)
public class XuanLingClient {
        public static final Minecraft minecraft = Minecraft.getInstance();

        /** 能量倍率调整：H 键持续按住计时（tick），每 10 tick（0.5 秒）变化 5% */
        private static int energyHoldTicks = 0;
        /** 上一 tick H 键是否按下 */
        private static boolean hWasDown = false;
        /** 上一 tick Shift+H 是否按下 */
        private static boolean shiftHWasDown = false;

        /** 每次变化的倍率步长（5% = 0.05） */
        private static final float ENERGY_STEP = 0.05f;
        /** 按住 H 时，倍率为 0 时直接跳转到 5% */
        private static final float ENERGY_JUMP_START = 0.05f;
        /** 0.5 秒对应的 tick 数（20 TPS） */
        private static final int ENERGY_HOLD_INTERVAL = 10;

        public XuanLingClient(ModContainer container) {
                container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
                NeoForge.EVENT_BUS.addListener(ClientTickEvent.Pre.class, XuanLingClient::onClientTick);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
                PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(AnmMeditation.LAYER_ID, 1500,
                                AnmMeditation::create);
        }

        @SubscribeEvent
        public static void onGatherData(GatherDataEvent event) {
                event.createProvider(XuanlingLangPro.LangZh::new);
                event.createProvider(XuanlingLangPro.LangEn::new);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
                event.registerEntityRenderer(RegisterEntities.SKILL_ENTITY.get(), SkillEntityRender::new);
                event.registerEntityRenderer(RegisterEntities.EJECT_ENTITY.get(), SkillEntityRender::new);
                event.registerEntityRenderer(RegisterEntities.PROJECTILE_SPELL_ENTITY.get(), SkillEntityRender::new);
                event.registerEntityRenderer(RegisterEntities.AOE_SPELL_ENTITY.get(), SkillEntityRender::new);
        }

        public static void onClientTick(ClientTickEvent.Pre event) {
                try {
                        keyVoid();
                } catch (Throwable e) {
                        XuanLing.LOGGER.error("[dev] failed to open screen, restart the client if this class was newly added", e);
                }
        }

        private static void keyVoid() throws Throwable {
                if (minecraft.screen != null)
                        return;
                boolean battleWasActive = BattleModeHud.isActive();
                if (RegisterKey.KEY_LALT.consumeClick()) {
                        BattleModeHud.toggle();
                }
                // 战斗模式开启时，若倍率为 0，自动设为 50%
                if (!battleWasActive && BattleModeHud.isActive() && minecraft.player != null) {
                        BasicData data = RegisterAttachments.getBasicData(minecraft.player);
                        if (data != null && data.getEnergyMultiplier() == 0f) {
                                setMultiplierClient(0.5f);
                        }
                }
                if (!BattleModeHud.isActive() && RegisterKey.KEY_R.consumeClick()) {
                        minecraft.setScreen(new StateGui());
                }
                if (RegisterKey.KEY_MEDITATION.consumeClick()) {
                        XuanLing.LOGGER.info("click");
                        Networking.sendToServer(new MeditationPacket());
                }
                if (BattleModeHud.isActive()) {
                        if (RegisterKey.KEY_RELEASE.consumeClick()) {
                                if (!BattleModeHud.isEmpty())
                                        Networking.sendToServer(new SkillCastPacket(BattleModeHud.release()));
                        }
                        if (RegisterKey.KEY_R.consumeClick()) {
                                BattleModeHud.pressKey('R');
                        }
                        if (RegisterKey.KEY_G.consumeClick()) {
                                BattleModeHud.pressKey('G');
                        }
                        if (RegisterKey.KEY_C.consumeClick()) {
                                BattleModeHud.pressKey('C');
                        }
                        if (RegisterKey.KEY_V.consumeClick()) {
                                BattleModeHud.pressKey('V');
                        }
                        if (RegisterKey.KEY_B.consumeClick()) {
                                BattleModeHud.pressKey('B');
                        }
                }
                handleEnergyMultiplier();
        }

        private static void handleEnergyMultiplier() {
                if (minecraft.screen != null || minecraft.player == null)
                        return;
                boolean hDown = RegisterKey.KEY_HIGH_USE.isDown();
                boolean shiftHDown = hDown && minecraft.player != null && minecraft.player.isShiftKeyDown();
                BasicData data = RegisterAttachments.getBasicData(minecraft.player);
                if (data == null)
                        return;

                // H 键：持续按住递增（非 Shift）
                if (hDown && !shiftHDown) {
                        if (!hWasDown) {
                                // 刚按下：倍率为 0 时立即跳到 5%
                                energyHoldTicks = 0;
                                if (data.getEnergyMultiplier() == 0f) {
                                        setMultiplierClient(ENERGY_JUMP_START);
                                }
                        } else {
                                energyHoldTicks++;
                                if (energyHoldTicks >= ENERGY_HOLD_INTERVAL) {
                                        energyHoldTicks = 0;
                                        float next = Math.min(1f, data.getEnergyMultiplier() + ENERGY_STEP);
                                        setMultiplierClient(next);
                                }
                        }
                }
                // Shift+H：持续按住递减
                else if (shiftHDown) {
                        if (!shiftHWasDown) {
                                energyHoldTicks = 0;
                        } else {
                                energyHoldTicks++;
                                if (energyHoldTicks >= ENERGY_HOLD_INTERVAL) {
                                        energyHoldTicks = 0;
                                        float next = Math.max(0f, data.getEnergyMultiplier() - ENERGY_STEP);
                                        setMultiplierClient(next);
                                }
                        }
                }

                hWasDown = hDown;
                shiftHWasDown = shiftHDown;
        }

        private static void setMultiplierClient(float value) {
                if (minecraft.player == null)
                        return;
                BasicData data = RegisterAttachments.getBasicData(minecraft.player);
                if (data == null)
                        return;
                data.setEnergyMultiplier(value);
                Networking.sendToServer(new EnergyMultiplierPacket(value));
        }
}
