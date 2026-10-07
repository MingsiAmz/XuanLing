package com.mingsiamz.xuanlingmod.data;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class BasicRealm {
        private static HashMap<Integer, ComponentRealm> maps = new LinkedHashMap<>();

        private static final ComponentRealm[] realms = {
                        ComponentRealm.builder("fanren")
                                        .level(0)
                                        .size(10)
                                        .addHealth(0.5)
                                        .nextLevelAddHealth(10)
                                        .addNormalLife(0.5)
                                        .nextLevelAddNormalLife(5)
                                        .nextLevelAddSoulLife(1)
                                        .build(),
                        ComponentRealm.builder("lianqi")
                                        .level(1)
                                        .size(10)
                                        .addHealth(1.5)
                                        .addEnergy(50)
                                        .nextLevelAddHealth(100)
                                        .nextLevelAddEnergy(200)
                                        .nextLevelAddNormalLife(100)
                                        .nextLevelAddSoulLife(100)
                                        .build(),
                        ComponentRealm.builder("zhuji")
                                        .level(2)
                                        .size(10)
                                        .addHealth(10)
                                        .addEnergy(100)
                                        .nextLevelAddHealth(50)
                                        .nextLevelAddEnergy(800)
                                        .nextLevelAddNormalLife(200)
                                        .nextLevelAddSoulLife(200)
                                        .build(),
                        ComponentRealm.builder("jindan")
                                        .level(3)
                                        .size(10)
                                        .addHealth(50)
                                        .addEnergy(385)
                                        .nextLevelAddHealth(100)
                                        .nextLevelAddEnergy(2000)
                                        .nextLevelAddNormalLife(200)
                                        .nextLevelAddSoulLife(200)
                                        .build(),
                        ComponentRealm.builder("yuanying")
                                        .level(4)
                                        .size(3)
                                        .addHealth(125)
                                        .addEnergy(5000)
                                        .nextLevelAddHealth(500)
                                        .nextLevelAddEnergy(10000)
                                        .nextLevelAddNormalLife(500)
                                        .nextLevelAddSoulLife(500)
                                        .build(),
        };

        static {
                for (ComponentRealm realm : realms) {
                        maps.put(realm.getIndex(), realm);
                }
        }

        public static ComponentRealm getRealm(LivingEntity entity, int index) {
                Type type = new TypeToken<LinkedHashMap<Integer, ComponentRealm>>() {}.getType();
                LinkedHashMap<Integer, ComponentRealm> data = DataManager.loadData(type, XuanLingTypeEnum.REALM, entity.getUUID().toString(), "normal");
                if (data == null)
                        return null;
                return data.get(index);
        }

        public static ComponentRealm[] getRealms() {
                return realms;
        }

        public static Map<Integer, ComponentRealm> getRealmMaps() {
                return maps;
        }
}
