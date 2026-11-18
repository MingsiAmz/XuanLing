package com.mingsiamz.xuanlingmod.type.spell;

public enum ParamType {
        INIT_SPEED("init_speed"),
        SPEED("speed"),
        DAMAGE("damage"),
        RADIUS("radius"),
        NORMAL_VALUE("normal_value"),
        DURATION("duration"),
        COOLDOWN("cooldown"),
        LIFETIME("lifetime"),
        TICK_INTERVAL("tick_interval"),
        MAX_TARGETS("max_targets"),
        KNOCKBACK("knockback");
        public String name;

        private ParamType(String name) {
                this.name = name;
        }
}
