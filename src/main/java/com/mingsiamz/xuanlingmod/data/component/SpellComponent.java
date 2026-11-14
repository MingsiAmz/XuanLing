package com.mingsiamz.xuanlingmod.data.component;

import com.mingsiamz.xuanlingmod.data.ComponentGroup;
import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

public class SpellComponent {

        private final XuanLingTypeEnum type = XuanLingTypeEnum.SPELL;
        private String name;
        private ComponentGroup group;

        private SpellComponent(Builder builder) {
                name = builder.name;
                group = builder.group;
        }

        public XuanLingTypeEnum getType() {
                return type;
        }

        public ComponentGroup getGroup() {
                return this.group;
        }

        public static Builder build(String name) {
                Builder builder = new Builder(name);
                return builder;
        }

        public static void run(SpellComponent spell) {
                spell.group.run();
        }

        public static class Builder {
                private ComponentGroup group = new ComponentGroup(XuanLingTypeEnum.SPELL);
                private String name;

                public Builder(String name) {
                        this.name = name;
                }

                public Builder addComponent(String name, ImpComponent component) {
                        this.group.addComponent(name, component);
                        return this;
                }
        }
}
