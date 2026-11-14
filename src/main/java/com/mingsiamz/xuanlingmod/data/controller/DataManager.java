package com.mingsiamz.xuanlingmod.data.controller;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicEnergy;
import com.mingsiamz.xuanlingmod.data.BasicRealm;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

public final class DataManager {

        private static final Gson GSON = new GsonBuilder()
                        .setPrettyPrinting()
                        .serializeNulls()
                        .create();

        private static final Path CONFIG_DIR = Paths.get(".", "config");
        public static final Path MOD_CONFIG_DIR = CONFIG_DIR.resolve(XuanLing.MODID);
        public static final Path ART_CONFIG_DIR = MOD_CONFIG_DIR.resolve("skills");
        public static final Path SPELL_CONFIG_DIR = MOD_CONFIG_DIR.resolve("spells");
        public static final Path REALM_CONFIG_DIR = MOD_CONFIG_DIR.resolve("realms");
        public static final Path ENERGY_CONFIG_DIR = MOD_CONFIG_DIR.resolve("energy");

        private DataManager() {
        }

        public static Path resolveFile(XuanLingTypeEnum type, String entityUuid, String name) {
                Path baseDir = switch (type) {
                        case ART -> ART_CONFIG_DIR;
                        case SPELL -> SPELL_CONFIG_DIR;
                        case REALM -> REALM_CONFIG_DIR;
                        case ENERGY -> ENERGY_CONFIG_DIR;
                };
                return baseDir.resolve(entityUuid).resolve(name + ".json");
        }

        public static void saveData(Object obj, XuanLingTypeEnum type, String entityUuid, String name) {
                Path file = resolveFile(type, entityUuid, name);
                try {
                        Files.createDirectories(file.getParent());
                        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                                GSON.toJson(obj, writer);
                        }
                } catch (IOException e) {
                        throw new RuntimeException("Failed to save " + name, e);
                }
        }

        public static <T> T loadData(Type type, XuanLingTypeEnum enumType, String entityUuid, String name) {
                Path file = resolveFile(enumType, entityUuid, name);
                if (!Files.exists(file)) {
                        return null;
                }
                try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                        return GSON.fromJson(reader, type);
                } catch (IOException e) {
                        throw new RuntimeException("Failed to load " + name, e);
                }
        }

        public static void init() throws IOException {
                Files.createDirectories(ART_CONFIG_DIR);
                Files.createDirectories(SPELL_CONFIG_DIR);
                Files.createDirectories(REALM_CONFIG_DIR);
                Files.createDirectories(ENERGY_CONFIG_DIR);

                if (!Files.exists(resolveFile(XuanLingTypeEnum.REALM, "000", "normal"))) {
                        saveData(BasicRealm.getRealmMaps(), XuanLingTypeEnum.REALM, "000", "normal");
                }
                if (!Files.exists(resolveFile(XuanLingTypeEnum.ENERGY, "000", "normal"))) {
                        saveData(BasicEnergy.getEnergyMaps(), XuanLingTypeEnum.ENERGY, "000", "normal");
                }
        }

        public static void resetDefaults() {
                saveData(BasicRealm.getRealmMaps(), XuanLingTypeEnum.REALM, "000", "normal");
                saveData(BasicEnergy.getEnergyMaps(), XuanLingTypeEnum.ENERGY, "000", "normal");
        }
}
