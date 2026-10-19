package com.mingsiamz.xuanlingmod.data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.reflect.TypeToken;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.data.component.ComponentRealm;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class BasicRealm {
        private static HashMap<Integer, ComponentRealm> maps = new LinkedHashMap<>();

        private static final ComponentRealm[] realms = {
                        ComponentRealm.builder("fanren")
                                        .level(0)
                                        .size(10)
                                        .addHealth(0.5)
                                        .addChildEnergy(15)
                                        .nextLevelAddHealth(10)
                                        .addNormalLife(0.5)
                                        .nextLevelAddParrentEnergy(100)
                                        .nextLevelAddNormalLife(5)
                                        .nextLevelAddSoulLife(1)
                                        .trans()
                                        .build(),
                        ComponentRealm.builder("lianqi")
                                        .level(1)
                                        .size(10)
                                        .addHealth(1.5)
                                        .addParrentEnergy(50)
                                        .addChildEnergy(50)
                                        .nextLevelAddHealth(100)
                                        .nextLevelAddParrentEnergy(200)
                                        .nextLevelAddNormalLife(100)
                                        .nextLevelAddSoulLife(100)
                                        .trans()
                                        .build(),
                        ComponentRealm.builder("zhuji")
                                        .level(2)
                                        .size(10)
                                        .addHealth(10)
                                        .addParrentEnergy(100)
                                        .addChildEnergy(100)
                                        .nextLevelAddHealth(50)
                                        .nextLevelAddParrentEnergy(800)
                                        .nextLevelAddNormalLife(200)
                                        .nextLevelAddSoulLife(200)
                                        .trans()
                                        .build(),
                        ComponentRealm.builder("jindan")
                                        .level(3)
                                        .size(10)
                                        .addHealth(50)
                                        .addParrentEnergy(385)
                                        .addChildEnergy(385)
                                        .nextLevelAddHealth(100)
                                        .nextLevelAddParrentEnergy(2000)
                                        .nextLevelAddNormalLife(200)
                                        .nextLevelAddSoulLife(200)
                                        .trans()
                                        .build(),
                        ComponentRealm.builder("yuanying")
                                        .level(4)
                                        .size(3)
                                        .addHealth(125)
                                        .addParrentEnergy(5000)
                                        .addChildEnergy(5000)
                                        .nextLevelAddHealth(500)
                                        .nextLevelAddParrentEnergy(10000)
                                        .nextLevelAddNormalLife(500)
                                        .nextLevelAddSoulLife(500)
                                        .trans()
                                        .build(),
        };

        static {
                for (ComponentRealm realm : realms) {
                        maps.put(realm.getIndex(), realm);
                }
        }

        public static ComponentRealm getRealm(UUID uuid, String name, int level) {
                Type type = new TypeToken<LinkedHashMap<Integer, ComponentRealm>>() {
                }.getType();
                LinkedHashMap<Integer, ComponentRealm> data = null;
                if (name.equals("normal")) {
                        data = DataManager.loadData(type, XuanLingTypeEnum.REALM, "000", name);
                } else {
                        data = DataManager.loadData(type, XuanLingTypeEnum.REALM, uuid.toString(), name);
                }
                if (data == null || data.isEmpty())
                        return null;
                return data.get(level);
        }

        public static ComponentRealm[] getRealms() {
                return realms;
        }

        public static Map<Integer, ComponentRealm> getRealmMaps() {
                return maps;
        }
}
