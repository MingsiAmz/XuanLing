package com.mingsiamz.xuanlingmod.auto.config;

import java.util.Random;


public record CooldownTemplate(
    int baseCooldown,       
    int cooldownPerLevel,   
    int minCooldown,        
    int maxCooldown         
) {
    public CooldownTemplate {
        baseCooldown = Math.max(0, baseCooldown);
        cooldownPerLevel = Math.max(0, cooldownPerLevel);
        minCooldown = Math.max(0, minCooldown);
        maxCooldown = Math.max(minCooldown, maxCooldown);
    }

    public static CooldownTemplate defaultTemplate() {
        return new CooldownTemplate(10, 2, 1, 300);
    }

    public static CooldownTemplate of(int base, int perLevel, int min, int max) {
        return new CooldownTemplate(base, perLevel, min, max);
    }

    public int calculateCooldown(int realmLevel, Random random) {
        int cd = baseCooldown + cooldownPerLevel * realmLevel;
        cd = Math.max(minCooldown, Math.min(maxCooldown, cd));
        
        double variance = 0.9 + random.nextDouble() * 0.2;
        return (int) (cd * variance);
    }
}