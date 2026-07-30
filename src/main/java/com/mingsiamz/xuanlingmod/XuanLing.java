package com.mingsiamz.xuanlingmod;

import java.util.Random;

import org.slf4j.Logger;

import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterBlocks;
import com.mingsiamz.xuanlingmod.register.RegisterCapabilities;
import com.mingsiamz.xuanlingmod.register.RegisterItems;
import com.mingsiamz.xuanlingmod.register.RegisterKey;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(XuanLing.MODID)
public class XuanLing {
    public static final String MODID = "xuanling";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Random RANDOM = new Random();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("xuanling_tabs", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.xuanling")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> RegisterItems.EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(RegisterItems.EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public XuanLing(IEventBus modEventBus, ModContainer modContainer) {
	modEventBus.addListener(this::commonSetup);
	RegisterItems.register(modEventBus);
	RegisterBlocks.register(modEventBus);
	CREATIVE_MODE_TABS.register(modEventBus);
	NeoForge.EVENT_BUS.register(this);
	modEventBus.addListener(this::addCreative);
	modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC, MODID+"/"+MODID+".toml");

	RegisterAttachments.register(modEventBus);
	modEventBus.addListener(RegisterKey::register);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
	LOGGER.info("HELLO FROM COMMON SETUP");

	// if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
	//     LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
	// }
	
	// LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());
	
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
}
