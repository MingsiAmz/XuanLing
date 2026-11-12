package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.gui.PlayerVitalsHud;
import com.mingsiamz.xuanlingmod.gui.TopLeftQiHud;
import com.mingsiamz.xuanlingmod.layers.Tip;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterKey;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = XuanLing.MODID, value = Dist.CLIENT)
public class GuiEvent {
        @SubscribeEvent
        public static void registerLayers(RegisterGuiLayersEvent event) {
                event.registerAbove(
                                VanillaGuiLayers.HOTBAR,
                                ResourceLocation.fromNamespaceAndPath(XuanLing.MODID, "vitals"),
                                PlayerVitalsHud::render);

                event.registerAbove(
                                VanillaGuiLayers.HOTBAR,
                                ResourceLocation.fromNamespaceAndPath(XuanLing.MODID, "top_left_qi"),
                                TopLeftQiHud::render);
                event.registerAbove(VanillaGuiLayers.HOTBAR, XuanLing.prefix("not_use_meditation_layer"),
                                new Tip(() -> XuanLing.trans(XuanlingLangPro.GUI_STRING, "not_use_meditation"))
                                                .setCondition(player -> {
                                                        if (!RegisterKey.KEY_MEDITATION.isDown()) {
                                                                return false;
                                                        }

                                                        DefaultEnergy energy = RegisterAttachments
                                                                        .getDefaultEnergy(player);

                                                        if (energy == null || (energy.getMaxPValue() == 0
                                                                        && energy.getMaxCValue() == 0)) {
                                                                return true;
                                                        }

                                                        return false;
                                                })
                                                .setLifeTime(1000, true)
                                                .setFadeOutDuration(200)
                                                .setThemeColor(CultivationTheme.BORDER_CYAN));

        }

        @SubscribeEvent
        public static void onPreRenderLayer(RenderGuiLayerEvent.Pre event) {
                ResourceLocation name = event.getName();
                if (VanillaGuiLayers.PLAYER_HEALTH.equals(name) || VanillaGuiLayers.FOOD_LEVEL.equals(name)) {
                        event.setCanceled(true);
                }
        }

}
