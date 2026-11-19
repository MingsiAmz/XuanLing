package com.mingsiamz.xuanlingmod.auto;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.auto.config.GenerationConfig;
import com.mingsiamz.xuanlingmod.auto.generator.GenerationEngine;
import com.mingsiamz.xuanlingmod.auto.registry.TemplateRegistry;
import com.mingsiamz.xuanlingmod.event.AutoGenerationEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

public class AutoModEntry {

        private static boolean initialized = false;
        private static GenerationEngine engine;
        private static TemplateRegistry registry;

        public static void init(IEventBus modBus) {
                if (initialized) {
                        XuanLing.LOGGER.warn("Auto generation system already initialized!");
                        return;
                }

                XuanLing.LOGGER.info("Initializing XuanLing Auto Generation System...");

                XuanLing.LOGGER.info("Generation config registered");

                registry = TemplateRegistry.getInstance();
                XuanLing.LOGGER.info("Template registry created");

                engine = GenerationEngine.getInstance();
                XuanLing.LOGGER.info("Generation engine created");

                NeoForge.EVENT_BUS.addListener(AutoModEntry::onAddReloadListener);

                NeoForge.EVENT_BUS.addListener(AutoModEntry::onServerStarting);
                NeoForge.EVENT_BUS.addListener(AutoModEntry::onServerStopping);

                NeoForge.EVENT_BUS.addListener(EntityJoinLevelEvent.class, AutoGenerationEvents::onEntityJoinLevel);

                initialized = true;
                XuanLing.LOGGER.info("XuanLing Auto Generation System initialized successfully!");
        }

        private static void onAddReloadListener(AddReloadListenerEvent event) {
                event.addListener(registry);
                XuanLing.LOGGER.info("Registered TemplateRegistry as data pack reload listener");
        }

        private static void onServerStarting(ServerStartingEvent event) {
                XuanLing.LOGGER.info("Server starting - initializing auto generation for world: {}",
                                event.getServer().getWorldData().getLevelName());

                if (registry.getAllArt().isEmpty()) {
                        XuanLing.LOGGER.warn("No art templates loaded, initializing engine");
                        engine.initialize();
                }
        }

        private static void onServerStopping(ServerStoppingEvent event) {
                XuanLing.LOGGER.info("Server stopping - cleaning up auto generation system");
        }

        public static TemplateRegistry getRegistry() {
                return registry;
        }

        public static GenerationEngine getEngine() {
                return engine;
        }

        public static boolean isInitialized() {
                return initialized;
        }
}
