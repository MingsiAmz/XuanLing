package com.mingsiamz.xuanlingmod.auto.config;

public enum Rarity {
    COMMON("common", 0xFFFFFF, 100),
    UNCOMMON("uncommon", 0x1EFF00, 50),
    RARE("rare", 0x0070FF, 20),
    EPIC("epic", 0xA335EE, 5),
    LEGENDARY("legendary", 0xFF8000, 1);

    private final String id;
    private final int color;
    private final int weight;

    Rarity(String id, int color, int weight) {
        this.id = id;
        this.color = color;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public static Rarity fromWeight(int totalWeight, java.util.Random random) {
        int roll = random.nextInt(totalWeight);
        int accumulated = 0;
        for (Rarity rarity : values()) {
            accumulated += rarity.weight;
            if (roll < accumulated) {
                return rarity;
            }
        }
        return COMMON;
    }

    public static int getTotalWeight() {
        int total = 0;
        for (Rarity r : values()) {
            total += r.weight;
        }
        return total;
    }
}