package com.mingsiamz.xuanlingmod;

import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.effects.EffectMeditation;
import com.mingsiamz.xuanlingmod.gui.StateGui;
import com.mingsiamz.xuanlingmod.network.MeditationPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterEntities;
import com.mingsiamz.xuanlingmod.register.RegisterKey;
import com.mingsiamz.xuanlingmod.render.SkillEntityRender;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = XuanLing.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = XuanLing.MODID, value = Dist.CLIENT)
public class XuanLingClient {
        public static Minecraft minecraft = Minecraft.getInstance();

        public XuanLingClient(ModContainer container) {
                container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
                XuanLing.LOGGER.info("HELLO FROM CLIENT SETUP");
                XuanLing.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
                
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
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Pre event) {
                keyVoid();
        }

        private static void keyVoid() {
                if (minecraft.screen != null)
                        return;
                if (RegisterKey.KEY_R.consumeClick()) {
                        minecraft.setScreen(new StateGui());
                }
                if (RegisterKey.KEY_MEDITATION.consumeClick()) {
                        XuanLing.LOGGER.info("click");
                        Networking.sendToServer(new MeditationPacket());
                }
        }
}
