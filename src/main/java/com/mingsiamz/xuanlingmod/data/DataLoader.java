package com.mingsiamz.xuanlingmod.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mingsiamz.xuanlingmod.XuanLing;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DataLoader {

        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        private static final Map<String, JsonObject> CACHE = new ConcurrentHashMap<>();

        private static WatchService watcher;
        private static Thread watchThread;

        private DataLoader() {
        }

        public static void init(Path configDir) {
                try {
                        if (!Files.exists(configDir))
                                Files.createDirectories(configDir);
                        watcher = FileSystems.getDefault().newWatchService();
                        configDir.register(watcher,
                                        StandardWatchEventKinds.ENTRY_CREATE,
                                        StandardWatchEventKinds.ENTRY_MODIFY,
                                        StandardWatchEventKinds.ENTRY_DELETE);

                        Files.list(configDir).forEach(DataLoader::reloadFile);

                        watchThread = new Thread(DataLoader::watchLoop, "XuanLing-DataWatcher");
                        watchThread.setDaemon(true);
                        watchThread.start();
                } catch (IOException e) {
                        throw new RuntimeException("DataLoader init failed", e);
                }
        }

        public static void shutdown() {
                if (watchThread != null)
                        watchThread.interrupt();
                if (watcher != null) {
                        try {
                                watcher.close();
                        } catch (IOException ignored) {
                        }
                }
        }

        private static void watchLoop() {
                while (!Thread.currentThread().isInterrupted()) {
                        WatchKey key = null;
                        try {
                                key = watcher.take();
                                for (WatchEvent<?> event : key.pollEvents()) {
                                        if (event.kind() == StandardWatchEventKinds.OVERFLOW)
                                                continue;
                                        Path changed = (Path) event.context();
                                        if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                                                CACHE.remove(stripExt(changed));
                                        } else {
                                                reloadFile(DataManager.MOD_CONFIG_DIR.resolve(changed));
                                        }
                                }
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                        } catch (Exception e) {
                                XuanLing.LOGGER.warn("DataLoader watch error", e);
                        } finally {
                                if (key != null)
                                        key.reset();
                        }
                }
        }

        private static void reloadFile(Path file) {
                String fileName = file.getFileName().toString();
                if (!fileName.endsWith(".json"))
                        return;
                try (Reader reader = Files.newBufferedReader(file)) {
                        JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                        if (obj != null)
                                CACHE.put(stripExt(fileName), obj);
                } catch (IOException e) {
                        XuanLing.LOGGER.warn("Failed to reload {}", fileName, e);
                }
        }

        private static String stripExt(Path p) {
                return stripExt(p.getFileName().toString());
        }

        private static String stripExt(String name) {
                return name.endsWith(".json") ? name.substring(0, name.length() - 5) : name;
        }

        public static JsonObject get(String name) {
                return CACHE.get(name);
        }

        public static Map<String, JsonObject> getAll() {
                return CACHE;
        }
}
