package com.mingsiamz.xuanlingmod.data.component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;
import com.mingsiamz.xuanlingmod.type.spell.SpecialType;

public class SpellComponent {

        private final XuanLingTypeEnum type = XuanLingTypeEnum.SPELL;
        private String name;
        private HashMap<String, BasicType> basicMaps;
        private HashMap<String, SpecialType> specialMaps;
        private HashMap<String, ActionType> actionMaps;

        private SpellComponent(Builder builder) {
                basicMaps = builder.basicMaps;
                specialMaps = builder.specialMaps;
                actionMaps = builder.actionMaps;
                name = builder.name;
        }

        public XuanLingTypeEnum getType() {
                return type;
        }

        public HashMap<String, BasicType> getBasicMaps() {
                return basicMaps;
        }

        public HashMap<String, SpecialType> getSpecialMaps() {
                return specialMaps;
        }

        public HashMap<String, ActionType> getActionMaps() {
                return actionMaps;
        }

        public static Builder build(String name) {
                Builder builder = new Builder(name);
                return builder;
        }

        public static void run(SpellComponent spell) {
                for (Map.Entry<String, BasicType> entry : spell.getBasicMaps().entrySet()) {
                        entry.getValue().run();
                }
                for (Map.Entry<String, SpecialType> entry : spell.getSpecialMaps().entrySet()) {
                        entry.getValue().run();
                }
                for (Map.Entry<String, ActionType> entry : spell.getActionMaps().entrySet()) {
                        entry.getValue().run();
                }
        }

        public static class Builder {
                private HashMap<String, BasicType> basicMaps = new LinkedHashMap<>();
                private HashMap<String, SpecialType> specialMaps = new LinkedHashMap<>();
                private HashMap<String, ActionType> actionMaps = new LinkedHashMap<>();
                private String name;

                public Builder(String name) {
                        this.name = name;
                }

                public Builder addBasicComponent(BasicType type) {
                        this.basicMaps.put(type.type(), type);
                        return this;
                }

                public Builder addSpecialComponent(SpecialType type) {
                        this.specialMaps.put(type.type(), type);
                        return this;
                }

                public Builder addActionComponent(ActionType type) {
                        this.actionMaps.put(type.type(), type);
                        return this;
                }
        }
}
