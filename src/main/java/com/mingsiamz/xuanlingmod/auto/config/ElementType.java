package com.mingsiamz.xuanlingmod.auto.config;

public enum ElementType {
    NONE("none", 0xFFFFFF, "无属性"),
    FIRE("fire", 0xFF4500, "火"),
    WATER("water", 0x1E90FF, "水"),
    EARTH("earth", 0x8B4513, "土"),
    WIND("wind", 0x87CEEB, "风"),
    LIGHTNING("lightning", 0xFFFF00, "雷"),
    ICE("ice", 0x00FFFF, "冰"),
    WOOD("wood", 0x228B22, "木"),
    METAL("metal", 0xC0C0C0, "金"),
    LIGHT("light", 0xFFFFE0, "光"),
    DARK("dark", 0x2F2F2F, "暗"),
    VOID("void", 0x8B008B, "虚空"),
    POISON("poison", 0x9932CC, "毒"),
    SPIRIT("spirit", 0xFF69B4, "灵");

    private final String id;
    private final int color;
    private final String displayName;

    ElementType(String id, int color, String displayName) {
        this.id = id;
        this.color = color;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ElementType fromId(String id) {
        for (ElementType type : values()) {
            if (type.id.equalsIgnoreCase(id)) {
                return type;
            }
        }
        return NONE;
    }

    public static ElementType random(java.util.Random random) {
        ElementType[] values = values();
        return values[random.nextInt(values.length - 1) + 1]; 
    }
}