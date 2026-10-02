package com.mingsiamz.xuanlingmod;

import java.util.Random;

import org.slf4j.Logger;

import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterBlocks;
import com.mingsiamz.xuanlingmod.register.RegisterCreativeTabs;
import com.mingsiamz.xuanlingmod.register.RegisterEntities;
import com.mingsiamz.xuanlingmod.register.RegisterItems;
import com.mingsiamz.xuanlingmod.register.RegisterKey;
import com.mojang.logging.LogUtils;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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

        public XuanLing(IEventBus modEventBus, ModContainer modContainer) {
                modEventBus.addListener(this::commonSetup);
                RegisterAttachments.register(modEventBus);
                RegisterItems.register(modEventBus);
                RegisterBlocks.register(modEventBus);
                RegisterCreativeTabs.register(modEventBus);
                modEventBus.addListener(RegisterEntities::registerEntityAtb);
                RegisterEntities.register(modEventBus);
                NeoForge.EVENT_BUS.register(this);
                modEventBus.addListener(this::addCreative);
                modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, MODID + "/" + MODID + ".toml");

                modEventBus.addListener(RegisterKey::register);
        }

        private void commonSetup(FMLCommonSetupEvent event) {
                // Some common setup code
                LOGGER.info("HELLO FROM COMMON SETUP");

                // if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
                // LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
                // }

                // LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(),
                // Config.MAGIC_NUMBER.getAsInt());

                // Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
        }

        private void addCreative(BuildCreativeModeTabContentsEvent event) {
                if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
                        event.accept(RegisterItems.EXAMPLE_BLOCK_ITEM);
                }
        }

        @SubscribeEvent
        public void onServerStarting(ServerStartingEvent event) {
                // Do something when the server starts
                LOGGER.info("HELLO from server starting");
        }

        public static ResourceLocation prefix(String name) {
                return ResourceLocation.fromNamespaceAndPath(MODID, name);
        }

        public static Component trans(String key, String name, Object... args) {
                return Component.translatable(key + name, args);
        }

}
