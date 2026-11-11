package com.mingsiamz.xuanlingmod.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mingsiamz.xuanlingmod.XuanLing;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DataLoader {

        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

        private static final Map<String, JsonObject> CACHE = new ConcurrentHashMap<>();

        private static final Map<WatchKey, Path> WATCHED_DIRS = new ConcurrentHashMap<>();

        private static WatchService watcher;
        private static Thread watchThread;
        private static Path rootDir;

        private DataLoader() {
        }

        public static void init(Path configDir) {
                try {
                        rootDir = configDir.toAbsolutePath().normalize();
                        Files.createDirectories(rootDir);

                        watcher = FileSystems.getDefault().newWatchService();

                        registerAll(rootDir);
                        loadAll(rootDir);

                        watchThread = new Thread(DataLoader::watchLoop, "XuanLing-DataWatcher");
                        watchThread.setDaemon(true);
                        watchThread.start();
                } catch (IOException e) {
                        throw new RuntimeException("DataLoader init failed", e);
                }
        }

        public static void shutdown() {
                if (watchThread != null) {
                        watchThread.interrupt();
                        watchThread = null;
                }
                if (watcher != null) {
                        try {
                                watcher.close();
                        } catch (IOException ignored) {
                        }
                        watcher = null;
                }
                WATCHED_DIRS.clear();
                CACHE.clear();
        }

        private static void registerAll(Path start) throws IOException {
                Files.walkFileTree(start, new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                                        throws IOException {
                                register(dir);
                                return FileVisitResult.CONTINUE;
                        }
                });
        }

        private static void register(Path dir) throws IOException {
                WatchKey key = dir.register(watcher,
                                StandardWatchEventKinds.ENTRY_CREATE,
                                StandardWatchEventKinds.ENTRY_MODIFY,
                                StandardWatchEventKinds.ENTRY_DELETE);
                WATCHED_DIRS.put(key, dir);
        }

        private static void loadAll(Path start) throws IOException {
                Files.walkFileTree(start, new SimpleFileVisitor<>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                                if (isJsonFile(file)) {
                                        reloadFile(file);
                                }
                                return FileVisitResult.CONTINUE;
                        }
                });
        }

        private static void watchLoop() {
                while (!Thread.currentThread().isInterrupted()) {
                        WatchKey key;
                        try {
                                key = watcher.take();
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                        } catch (ClosedWatchServiceException e) {
                                break;
                        } catch (Exception e) {
                                XuanLing.LOGGER.warn("DataLoader watcher.take() failed", e);
                                continue;
                        }

                        Path dir = WATCHED_DIRS.get(key);
                        if (dir == null) {
                                key.reset();
                                continue;
                        }

                        try {
                                for (WatchEvent<?> event : key.pollEvents()) {
                                        WatchEvent.Kind<?> kind = event.kind();
                                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                                                continue;
                                        }
                                        Path child = dir.resolve((Path) event.context());

                                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                                handleCreate(child);
                                        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                                if (isJsonFile(child)) {
                                                        reloadFile(child);
                                                }
                                        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                                handleDelete(child);
                                        }
                                }
                        } catch (Exception e) {
                                XuanLing.LOGGER.warn("DataLoader watch error for {}", dir, e);
                        } finally {
                                if (!key.reset()) {
                                        WATCHED_DIRS.remove(key);
                                        clearCacheForDir(dir);
                                }
                        }
                }
        }

        private static void handleCreate(Path child) {
                try {
                        if (Files.isDirectory(child)) {
                                registerAll(child);
                                loadAll(child);
                        } else if (isJsonFile(child)) {
                                reloadFile(child);
                        }
                } catch (IOException e) {
                        XuanLing.LOGGER.warn("DataLoader failed to handle create for {}", child, e);
                }
        }

        private static void handleDelete(Path child) {
                String key = cacheKey(child);
                if (key == null) {
                        return;
                }
                if (CACHE.remove(key) != null) {
                        return;
                }
                String prefix = key + "/";
                CACHE.keySet().removeIf(k -> k.startsWith(prefix));
        }

        private static void clearCacheForDir(Path dir) {
                if (dir.equals(rootDir)) {
                        CACHE.clear();
                        return;
                }
                String dirKey = cacheKey(dir);
                if (dirKey == null || dirKey.isEmpty()) {
                        return;
                }
                String prefix = dirKey + "/";
                CACHE.keySet().removeIf(k -> k.startsWith(prefix));
        }

        private static boolean isJsonFile(Path file) {
                return Files.isRegularFile(file) && file.getFileName().toString().endsWith(".json");
        }

        private static void reloadFile(Path file) {
                String key = cacheKey(file);
                if (key == null) {
                        return;
                }
                try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                        if (obj != null) {
                                CACHE.put(key, obj);
                        }
                } catch (IOException e) {
                        XuanLing.LOGGER.warn("Failed to reload {}", file, e);
                } catch (Exception e) {
                        XuanLing.LOGGER.warn("Failed to parse {}", file, e);
                }
        }

        private static String cacheKey(Path file) {
                if (rootDir == null) {
                        return null;
                }
                Path abs = file.toAbsolutePath().normalize();
                if (!abs.startsWith(rootDir)) {
                        return null;
                }
                Path rel = rootDir.relativize(abs);
                if (rel.getNameCount() == 0) {
                        return "";
                }
                String s = rel.toString().replace('\\', '/');
                return s.endsWith(".json") ? s.substring(0, s.length() - 5) : s;
        }

        public static JsonObject get(String key) {
                return CACHE.get(key);
        }

        public static Map<String, JsonObject> getAll() {
                return CACHE;
        }
}
