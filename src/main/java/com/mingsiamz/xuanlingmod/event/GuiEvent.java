package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.gui.PlayerVitalsHud;
import com.mingsiamz.xuanlingmod.gui.TopLeftQiHud;

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
                                    PlayerVitalsHud::render
                                    );

                event.registerAbove(
                                    VanillaGuiLayers.HOTBAR,
                                    ResourceLocation.fromNamespaceAndPath(XuanLing.MODID, "top_left_qi"),
                                    TopLeftQiHud::render
                                    );
        }

        @SubscribeEvent
        public static void onPreRenderLayer(RenderGuiLayerEvent.Pre event) {
                ResourceLocation name = event.getName();
                if (VanillaGuiLayers.PLAYER_HEALTH.equals(name)
                    || VanillaGuiLayers.FOOD_LEVEL.equals(name)) {
                        event.setCanceled(true);
                }
        }

}
