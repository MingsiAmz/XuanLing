package com.mingsiamz.xuanlingmod.auto.config;


public record RealmStatTemplate(
    double health,
    double parrentEnergy,
    double childEnergy,
    double normalLife,
    double soulLife
) {
    public RealmStatTemplate {
        health = Math.max(0, health);
        parrentEnergy = Math.max(0, parrentEnergy);
        childEnergy = Math.max(0, childEnergy);
        normalLife = Math.max(0, normalLife);
        soulLife = Math.max(0, soulLife);
    }

    public static RealmStatTemplate empty() {
        return new RealmStatTemplate(0, 0, 0, 0, 0);
    }

    public static RealmStatTemplate of(double health, double parrentEnergy, double childEnergy,
                                       double normalLife, double soulLife) {
        return new RealmStatTemplate(health, parrentEnergy, childEnergy, normalLife, soulLife);
    }

    public RealmStatTemplate multiply(double factor) {
        return new RealmStatTemplate(
                health * factor,
                parrentEnergy * factor,
                childEnergy * factor,
                normalLife * factor,
                soulLife * factor
        );
    }

    public RealmStatTemplate add(RealmStatTemplate other) {
        return new RealmStatTemplate(
                health + other.health,
                parrentEnergy + other.parrentEnergy,
                childEnergy + other.childEnergy,
                normalLife + other.normalLife,
                soulLife + other.soulLife
        );
    }
}