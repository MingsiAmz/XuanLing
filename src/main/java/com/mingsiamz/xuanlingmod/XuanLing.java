package com.mingsiamz.xuanlingmod;

import java.util.Random;

import org.slf4j.Logger;

import com.mingsiamz.xuanlingmod.data.controller.DataLoader;
import com.mingsiamz.xuanlingmod.data.controller.DataManager;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.auto.AutoModEntry;
import com.mingsiamz.xuanlingmod.auto.config.GenerationConfig;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterBlocks;
import com.mingsiamz.xuanlingmod.register.RegisterCreativeTabs;
import com.mingsiamz.xuanlingmod.register.RegisterDataComponents;
import com.mingsiamz.xuanlingmod.register.RegisterEffects;
import com.mingsiamz.xuanlingmod.register.RegisterEntities;
import com.mingsiamz.xuanlingmod.register.RegisterItems;
import com.mingsiamz.xuanlingmod.register.RegisterKey;
import com.mingsiamz.xuanlingmod.loot.RegisterLootConditions;
import com.mingsiamz.xuanlingmod.loot.RegisterLootFunctions;
import com.mojang.logging.LogUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(XuanLing.MODID)
public class XuanLing {
        public static final String MODID = "xuanling";
        public static final Logger LOGGER = LogUtils.getLogger();
        public static final Random RANDOM = new Random();

        public XuanLing(IEventBus modEventBus, ModContainer modContainer) throws Exception {
                RegisterAttachments.register(modEventBus);
                RegisterDataComponents.register(modEventBus);
                RegisterItems.register(modEventBus);
                RegisterBlocks.register(modEventBus);
                RegisterCreativeTabs.register(modEventBus);
                RegisterEffects.register(modEventBus);
                RegisterEntities.register(modEventBus);
                RegisterLootConditions.register(modEventBus);
                RegisterLootFunctions.register(modEventBus);

                
                AutoModEntry.init(modEventBus);

                modEventBus.addListener(this::commonSetup);
                modEventBus.addListener(RegisterEntities::registerEntityAtb);
                NeoForge.EVENT_BUS.register(this);
                modEventBus.addListener(this::addCreative);
                modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, MODID + "/" + MODID + ".toml");
                modContainer.registerConfig(ModConfig.Type.COMMON, GenerationConfig.SPEC, MODID + "/auto_generation.toml");
                modEventBus.register(Networking.class);
                modEventBus.addListener(RegisterKey::register);

                DataManager.init();
                DataLoader.init(DataManager.MOD_CONFIG_DIR);
        }

        private void commonSetup(FMLCommonSetupEvent event) {
                LOGGER.info("HELLO FROM COMMON SETUP");
        }

        private void addCreative(BuildCreativeModeTabContentsEvent event) {
                if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
                        event.accept(RegisterItems.EXAMPLE_BLOCK_ITEM);
                }
        }

        @SubscribeEvent
        public void onServerStarting(ServerStartingEvent event) {
                LOGGER.info("HELLO from server starting");
        }

        public static ResourceLocation prefix(String name) {
                return ResourceLocation.fromNamespaceAndPath(MODID, name);
        }

        public static Component trans(String key, String name, Object... args) {
                return Component.translatable(key + name, args);
        }

}
