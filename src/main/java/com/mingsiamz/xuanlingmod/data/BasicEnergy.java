package com.mingsiamz.xuanlingmod.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.reflect.TypeToken;
import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy.Type;
import com.mingsiamz.xuanlingmod.data.controller.DataManager;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

public class BasicEnergy {
        private static HashMap<String, ComponentEnergy> energyMap = new LinkedHashMap<>();
        
        static {
                ComponentEnergy LingQi = ComponentEnergy
                        .builder("lingqi", ComponentEnergy.Type.PARRENT)
                        .trans()
                        .value(0)
                        .maxValue(Config.getMaxValue(Config.PARRENT_ENERGY_RANGE))
                        .build();
                ComponentEnergy NeiLi = ComponentEnergy
                        .builder("neili", Type.CHILD)
                        .trans()
                        .value(0)
                        .maxValue(Config.getMaxValue(Config.CHILD_ENERGY_RANGE))
                        .build();
                energyMap.put(Type.PARRENT.name(), LingQi);
                energyMap.put(Type.CHILD.name(), NeiLi);
        }

        public static ComponentEnergy getEnergy(UUID uuid, String name, String eType) {
                java.lang.reflect.Type type = new TypeToken<LinkedHashMap<Type, ComponentEnergy>>() {}.getType();
                LinkedHashMap<String, ComponentEnergy> data = null;
                if (name == "normal") {
                        data = DataManager.loadData(type, XuanLingTypeEnum.ENERGY, "000", name);
                } else {
                        data = DataManager.loadData(type, XuanLingTypeEnum.ENERGY, uuid.toString(), name);
                }
                if (data == null || data.isEmpty())
                        return null;
                return data.get(eType);
        }

        public static Map<String, ComponentEnergy> getEnergyMaps() {
                return energyMap;
        }

}
