package com.mingsiamz.xuanlingmod.auto.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public record RealmTemplate(
    String id,                              
    String name,                            
    int level,                              
    int size,                               
    RealmStatTemplate baseStats,            
    RealmStatTemplate breakthroughStats,    
    String description,                     
    Rarity rarity,                          
    boolean canTranscend,                   
    String category,                        
    Map<String, Double> statWeights         
) {
    public RealmTemplate {
        baseStats = baseStats == null ? RealmStatTemplate.empty() : baseStats;
        breakthroughStats = breakthroughStats == null ? RealmStatTemplate.empty() : breakthroughStats;
        description = description == null ? "" : description;
        rarity = rarity == null ? Rarity.RARE : rarity;
        category = category == null ? "custom" : category;
        statWeights = statWeights == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(statWeights);
        size = Math.max(1, size);
        level = Math.max(0, level);
    }

    public static Builder builder(String id, String name, int level) {
        return new Builder(id, name, level);
    }

    public static class Builder {
        private final String id;
        private final String name;
        private final int level;
        private int size = 10;
        private RealmStatTemplate baseStats = RealmStatTemplate.empty();
        private RealmStatTemplate breakthroughStats = RealmStatTemplate.empty();
        private String description = "";
        private Rarity rarity = Rarity.RARE;
        private boolean canTranscend = true;
        private String category = "custom";
        private final Map<String, Double> statWeights = new ConcurrentHashMap<>();

        private Builder(String id, String name, int level) {
            this.id = id;
            this.name = name;
            this.level = level;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder baseStats(RealmStatTemplate stats) {
            this.baseStats = stats;
            return this;
        }

        public Builder breakthroughStats(RealmStatTemplate stats) {
            this.breakthroughStats = stats;
            return this;
        }

        public Builder baseStats(double health, double parrentEnergy, double childEnergy,
                                 double normalLife, double soulLife) {
            this.baseStats = new RealmStatTemplate(health, parrentEnergy, childEnergy, normalLife, soulLife);
            return this;
        }

        public Builder breakthroughStats(double health, double parrentEnergy, double childEnergy,
                                         double normalLife, double soulLife) {
            this.breakthroughStats = new RealmStatTemplate(health, parrentEnergy, childEnergy, normalLife, soulLife);
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder canTranscend(boolean canTranscend) {
            this.canTranscend = canTranscend;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder statWeight(String stat, double weight) {
            this.statWeights.put(stat, weight);
            return this;
        }

        public RealmTemplate build() {
            return new RealmTemplate(id, name, level, size, baseStats, breakthroughStats,
                    description, rarity, canTranscend, category, statWeights);
        }
    }

    
    public static RealmTemplate fromJson(String id, JsonObject object) {
        try {
            String name = object.has("name") ? object.get("name").getAsString() : id;
            int level = object.has("level") ? object.get("level").getAsInt() : 1;
            int size = object.has("size") ? object.get("size").getAsInt() : 10;
            String description = object.has("description") ? object.get("description").getAsString() : "";
            Rarity rarity = object.has("rarity") ? Rarity.valueOf(object.get("rarity").getAsString().toUpperCase()) : Rarity.RARE;
            boolean canTranscend = object.has("can_transcend") ? object.get("can_transcend").getAsBoolean() : true;
            String category = object.has("category") ? object.get("category").getAsString() : "custom";

            
            RealmStatTemplate baseStats = RealmStatTemplate.empty();
            if (object.has("base_stats")) {
                JsonObject stats = object.getAsJsonObject("base_stats");
                baseStats = new RealmStatTemplate(
                        stats.has("health") ? stats.get("health").getAsDouble() : 0,
                        stats.has("parrent_energy") ? stats.get("parrent_energy").getAsDouble() : 0,
                        stats.has("child_energy") ? stats.get("child_energy").getAsDouble() : 0,
                        stats.has("normal_life") ? stats.get("normal_life").getAsDouble() : 0,
                        stats.has("soul_life") ? stats.get("soul_life").getAsDouble() : 0
                );
            }

            
            RealmStatTemplate breakthroughStats = RealmStatTemplate.empty();
            if (object.has("breakthrough_stats")) {
                JsonObject stats = object.getAsJsonObject("breakthrough_stats");
                breakthroughStats = new RealmStatTemplate(
                        stats.has("health") ? stats.get("health").getAsDouble() : 0,
                        stats.has("parrent_energy") ? stats.get("parrent_energy").getAsDouble() : 0,
                        stats.has("child_energy") ? stats.get("child_energy").getAsDouble() : 0,
                        stats.has("normal_life") ? stats.get("normal_life").getAsDouble() : 0,
                        stats.has("soul_life") ? stats.get("soul_life").getAsDouble() : 0
                );
            }

            
            Map<String, Double> statWeights = new ConcurrentHashMap<>();
            if (object.has("stat_weights")) {
                JsonObject weights = object.getAsJsonObject("stat_weights");
                for (Map.Entry<String, JsonElement> entry : weights.entrySet()) {
                    statWeights.put(entry.getKey(), entry.getValue().getAsDouble());
                }
            }

            return new Builder(id, name, level)
                    .size(size)
                    .baseStats(baseStats)
                    .breakthroughStats(breakthroughStats)
                    .description(description)
                    .rarity(rarity)
                    .canTranscend(canTranscend)
                    .category(category)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    
    public com.mingsiamz.xuanlingmod.data.component.ComponentRealm.Builder toComponentRealmBuilder() {
        return com.mingsiamz.xuanlingmod.data.component.ComponentRealm.builder(name)
                .level(level)
                .size(size)
                .addHealth(baseStats.health())
                .addParrentEnergy(baseStats.parrentEnergy())
                .addChildEnergy(baseStats.childEnergy())
                .addNormalLife(baseStats.normalLife())
                .addSoulLife(baseStats.soulLife())
                .nextLevelAddHealth(breakthroughStats.health())
                .nextLevelAddParrentEnergy(breakthroughStats.parrentEnergy())
                .nextLevelAddChildEnergy(breakthroughStats.childEnergy())
                .nextLevelAddNormalLife(breakthroughStats.normalLife())
                .nextLevelAddSoulLife(breakthroughStats.soulLife());
    }
}