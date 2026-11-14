package com.mingsiamz.xuanlingmod.type.spell;

public enum ParamType {
        INIT_SPEED("init_speed"),
        SPEED("speed"),
        DAMAGE("damage"),
        NORMAL_VALUE("normal_value");
        public String name;

        private ParamType(String name) {
                this.name = name;
        }
}
