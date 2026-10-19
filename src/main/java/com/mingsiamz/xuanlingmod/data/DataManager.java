package com.mingsiamz.xuanlingmod.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.XuanLingTypeEnum;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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

        public static void saveData(Object obj, XuanLingTypeEnum type, String entityUuid, String name) {
                try {
                        if (isExists()) {
                                Files.createDirectories(MOD_CONFIG_DIR);
                                Files.createDirectories(ART_CONFIG_DIR);
                                Files.createDirectories(REALM_CONFIG_DIR);
                                Files.createDirectories(SPELL_CONFIG_DIR);
                        }
                        Path file = null;
                        Path target = null;
                        switch (type) {
                                case ART -> {
                                        target = ART_CONFIG_DIR.resolve(entityUuid);
                                        if (!Files.exists(target))
                                                Files.createDirectories(target);
                                        file = target.resolve(name + ".json");
                                }
                                case SPELL -> {
                                        target = SPELL_CONFIG_DIR.resolve(entityUuid);
                                        if (!Files.exists(target))
                                                Files.createDirectories(target);
                                        file = target.resolve(name + ".json");
                                }
                                case REALM -> {
                                        target = REALM_CONFIG_DIR.resolve(entityUuid);
                                        if (!Files.exists(target))
                                                Files.createDirectories(target);
                                        file = target.resolve(name + ".json");
                                }
                                case ENERGY -> {
                                        target = ENERGY_CONFIG_DIR.resolve(entityUuid);
                                        if (!Files.exists(target))
                                                Files.createDirectories(target);
                                        file = target.resolve(name + ".json");
                                }
                        }

                        if (file == null)
                                throw new RuntimeException("file data is null");

                        try (Writer writer = new FileWriter(file.toFile())) {
                                GSON.toJson(obj, writer);
                        }
                } catch (IOException e) {
                        throw new RuntimeException("Failed to save " + name, e);
                }
        }

        public static <T> T loadData(Type type, XuanLingTypeEnum enumType, String entityUuid, String name) {
                try {
                        init();
                        Path file = null;
                        Path target = null;
                        switch (enumType) {
                                case ART -> {
                                        target = ART_CONFIG_DIR.resolve(entityUuid);
                                        file = target.resolve(name + ".json");
                                }
                                case SPELL -> {
                                        target = SPELL_CONFIG_DIR.resolve(entityUuid);
                                        file = target.resolve(name + ".json");
                                }
                                case REALM -> {
                                        target = REALM_CONFIG_DIR.resolve(entityUuid);
                                        file = target.resolve(name + ".json");
                                }
                                case ENERGY -> {
                                        target = ENERGY_CONFIG_DIR.resolve(entityUuid);
                                        file = target.resolve(name + ".json");
                                }
                        }

                        if (file == null)
                                throw new RuntimeException("not data file");

                        if (!Files.exists(file))
                                return null;

                        Reader reader = new FileReader(file.toFile());
                        return GSON.fromJson(reader, type);
                } catch (IOException e) {
                        throw new RuntimeException("Failed to load " + name, e);
                }
        }

        public static void init() throws IOException {
                if (isExists()) {
                        Files.createDirectories(MOD_CONFIG_DIR);
                        Files.createDirectories(ART_CONFIG_DIR);
                        Files.createDirectories(REALM_CONFIG_DIR);
                        Files.createDirectories(SPELL_CONFIG_DIR);
                }
                DataManager.saveData(BasicRealm.getRealmMaps(), XuanLingTypeEnum.REALM, "000", "normal");
                DataManager.saveData(BasicEnergy.getEnergyMaps(), XuanLingTypeEnum.ENERGY, "000", "normal");
        }

        private static boolean isExists() {
                return !Files.exists(MOD_CONFIG_DIR) || !Files.exists(ART_CONFIG_DIR) || !Files.exists(REALM_CONFIG_DIR)
                                || !Files.exists(SPELL_CONFIG_DIR);
        }

}
