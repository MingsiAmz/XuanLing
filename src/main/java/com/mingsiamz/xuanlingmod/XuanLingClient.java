package com.mingsiamz.xuanlingmod;

import java.util.function.Supplier;

import com.mingsiamz.xuanlingmod.animations.player.AnmMeditation;
import com.mingsiamz.xuanlingmod.gui.BattleModeHud;
import com.mingsiamz.xuanlingmod.gui.StateGui;
import com.mingsiamz.xuanlingmod.network.MeditationPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.network.SkillCastPacket;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
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

        public XuanLingClient(ModContainer container) {
                container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
                NeoForge.EVENT_BUS.addListener(EntityRenderersEvent.RegisterRenderers.class, XuanLingClient::registerRenderers);
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
                if (RegisterKey.KEY_LALT.consumeClick()) {
                        BattleModeHud.toggle();
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
        }
}
