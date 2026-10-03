package com.mingsiamz.xuanlingmod.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mingsiamz.xuanlingmod.XuanLing;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DataManager {

        private static final Gson GSON = new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();

        private static final Path CONFIG_DIR = Paths.get(".", "config");
        public static final Path MOD_CONFIG_DIR = CONFIG_DIR.resolve(XuanLing.MODID);

        private DataManager() {
        }

        public static void saveData(Object obj, String name) {
                try {
                        if (!Files.exists(MOD_CONFIG_DIR)) {
                                Files.createDirectories(MOD_CONFIG_DIR);
                        }
                        Path file = MOD_CONFIG_DIR.resolve(name + ".json");
                        try (Writer writer = new FileWriter(file.toFile())) {
                                GSON.toJson(obj, writer);
                        }
                } catch (IOException e) {
                        throw new RuntimeException("Failed to save " + name, e);
                }
        }

        public static <T> T loadData(Class<T> clazz, String name) {
                Path file = MOD_CONFIG_DIR.resolve(name + ".json");
                if (!Files.exists(file))
                        return null;
                try (Reader reader = new FileReader(file.toFile())) {
                        return GSON.fromJson(reader, clazz);
                } catch (IOException e) {
                        throw new RuntimeException("Failed to load " + name, e);
                }
        }

        public static boolean exists(String name) {
                return Files.exists(MOD_CONFIG_DIR.resolve(name + ".json"));
        }
}
