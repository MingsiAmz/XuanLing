package com.mingsiamz.xuanlingmod.auto.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mingsiamz.xuanlingmod.data.ComponentGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public record NPCTemplate(
    String id,                              
    String entityType,                      
    String displayName,                     
    Rarity rarity,                          
    ArtTemplate artTemplate,          
    SpellTemplate spellTemplate,      
    boolean hasCustomRealm,                 
    RealmTemplate customRealmTemplate,    
    double artChance,                    
    double spellChance,                  
    double topProdigyChance,                
    LootTemplate lootTemplate,              
    Map<String, Double> statMultipliers,    
    String faction,                         
    String title                            
) {
    public NPCTemplate {
        rarity = rarity == null ? Rarity.COMMON : rarity;
        artChance = Math.clamp(artChance, 0.0, 1.0);
        spellChance = Math.clamp(spellChance, 0.0, 1.0);
        topProdigyChance = Math.clamp(topProdigyChance, 0.0, 1.0);
        lootTemplate = lootTemplate == null ? LootTemplate.defaultTemplate() : lootTemplate;
        statMultipliers = statMultipliers == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(statMultipliers);
        faction = faction == null ? "neutral" : faction;
        title = title == null ? "" : title;
        
        
        if (!hasCustomRealm) {
            customRealmTemplate = null;
        }
    }

    public static Builder builder(String id, String entityType) {
        return new Builder(id, entityType);
    }

    public static class Builder {
        private final String id;
        private final String entityType;
        private String displayName = "";
        private Rarity rarity = Rarity.COMMON;
        private ArtTemplate artTemplate = null;
        private SpellTemplate spellTemplate = null;
        private boolean hasCustomRealm = false;
        private RealmTemplate customRealmTemplate = null;
        private double artChance = 0.3;
        private double spellChance = 0.2;
        private double topProdigyChance = 0.001;
        private LootTemplate lootTemplate = LootTemplate.defaultTemplate();
        private final Map<String, Double> statMultipliers = new ConcurrentHashMap<>();
        private String faction = "neutral";
        private String title = "";

        private Builder(String id, String entityType) {
            this.id = id;
            this.entityType = entityType;
        }

        public Builder displayName(String name) {
            this.displayName = name;
            return this;
        }

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder artTemplate(ArtTemplate template) {
            this.artTemplate = template;
            return this;
        }

        public Builder spellTemplate(SpellTemplate template) {
            this.spellTemplate = template;
            return this;
        }

        public Builder hasCustomRealm(boolean hasCustomRealm) {
            this.hasCustomRealm = hasCustomRealm;
            return this;
        }

        public Builder customRealmTemplate(RealmTemplate template) {
            this.customRealmTemplate = template;
            this.hasCustomRealm = true;
            return this;
        }

        public Builder artChance(double chance) {
            this.artChance = chance;
            return this;
        }

        public Builder spellChance(double chance) {
            this.spellChance = chance;
            return this;
        }

        public Builder topProdigyChance(double chance) {
            this.topProdigyChance = chance;
            return this;
        }

        public Builder lootTemplate(LootTemplate template) {
            this.lootTemplate = template;
            return this;
        }

        public Builder statMultiplier(String stat, double multiplier) {
            this.statMultipliers.put(stat, multiplier);
            return this;
        }

        public Builder faction(String faction) {
            this.faction = faction;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public NPCTemplate build() {
            return new NPCTemplate(id, entityType, displayName, rarity, artTemplate,
                    spellTemplate, hasCustomRealm, customRealmTemplate,
                    artChance, spellChance, topProdigyChance,
                    lootTemplate, statMultipliers, faction, title);
        }
    }

    
    public static NPCTemplate fromJson(String id, JsonObject object) {
        try {
            String entityType = object.has("entity_type") ? object.get("entity_type").getAsString() : "";
            String displayName = object.has("display_name") ? object.get("display_name").getAsString() : id;
            Rarity rarity = object.has("rarity") ? Rarity.valueOf(object.get("rarity").getAsString().toUpperCase()) : Rarity.COMMON;
            String faction = object.has("faction") ? object.get("faction").getAsString() : "neutral";
            String title = object.has("title") ? object.get("title").getAsString() : "";
            
            double artChance = object.has("art_chance") ? object.get("art_chance").getAsDouble() : 0.3;
            double spellChance = object.has("spell_chance") ? object.get("spell_chance").getAsDouble() : 0.2;
            double topProdigyChance = object.has("top_prodigy_chance") ? object.get("top_prodigy_chance").getAsDouble() : 0.001;
            boolean hasCustomRealm = object.has("has_custom_realm") ? object.get("has_custom_realm").getAsBoolean() : false;

            
            Map<String, Double> statMultipliers = new ConcurrentHashMap<>();
            if (object.has("stat_multipliers")) {
                JsonObject stats = object.getAsJsonObject("stat_multipliers");
                for (Map.Entry<String, JsonElement> entry : stats.entrySet()) {
                    statMultipliers.put(entry.getKey(), entry.getValue().getAsDouble());
                }
            }

            
            LootTemplate lootTemplate = LootTemplate.defaultTemplate();
            if (object.has("loot_template")) {
                JsonObject loot = object.getAsJsonObject("loot_template");
                double baseJadeSlipChance = loot.has("base_jade_slip_chance") ? loot.get("base_jade_slip_chance").getAsDouble() : 0.1;
                boolean topProdigyAlwaysDropsRealm = loot.has("top_prodigy_always_drops_realm") ? loot.get("top_prodigy_always_drops_realm").getAsBoolean() : true;
                String customLootTable = loot.has("custom_loot_table") ? loot.get("custom_loot_table").getAsString() : "";
                
                Map<Rarity, Double> rarityMultipliers = new ConcurrentHashMap<>();
                if (loot.has("rarity_multipliers")) {
                    JsonObject mults = loot.getAsJsonObject("rarity_multipliers");
                    for (Map.Entry<String, JsonElement> entry : mults.entrySet()) {
                        try {
                            Rarity r = Rarity.valueOf(entry.getKey().toUpperCase());
                            rarityMultipliers.put(r, entry.getValue().getAsDouble());
                        } catch (IllegalArgumentException ignored) {}
                    }
                }
                
                lootTemplate = new LootTemplate(baseJadeSlipChance, rarityMultipliers, topProdigyAlwaysDropsRealm, customLootTable);
            }

            
            
            String artTemplateId = object.has("art_template_id") ? object.get("art_template_id").getAsString() : null;
            String spellTemplateId = object.has("spell_template_id") ? object.get("spell_template_id").getAsString() : null;
            String customRealmTemplateId = object.has("custom_realm_template_id") ? object.get("custom_realm_template_id").getAsString() : null;

            return new Builder(id, entityType)
                    .displayName(displayName)
                    .rarity(rarity)
                    .faction(faction)
                    .title(title)
                    .artChance(artChance)
                    .spellChance(spellChance)
                    .topProdigyChance(topProdigyChance)
                    .hasCustomRealm(hasCustomRealm)
                    .lootTemplate(lootTemplate)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    
    public boolean isTopProdigy(java.util.Random random) {
        return hasCustomRealm && random.nextDouble() < topProdigyChance;
    }

    
    public static class LootTemplate {
        private final double baseJadeSlipChance;      
        private final Map<Rarity, Double> rarityMultipliers; 
        private final boolean topProdigyAlwaysDropsRealm;    
        private final String customLootTable;                

        private LootTemplate(double baseJadeSlipChance, Map<Rarity, Double> rarityMultipliers,
                             boolean topProdigyAlwaysDropsRealm, String customLootTable) {
            this.baseJadeSlipChance = baseJadeSlipChance;
            this.rarityMultipliers = rarityMultipliers;
            this.topProdigyAlwaysDropsRealm = topProdigyAlwaysDropsRealm;
            this.customLootTable = customLootTable;
        }

        public static LootTemplate defaultTemplate() {
            Map<Rarity, Double> multipliers = new ConcurrentHashMap<>();
            multipliers.put(Rarity.COMMON, 1.0);
            multipliers.put(Rarity.UNCOMMON, 1.5);
            multipliers.put(Rarity.RARE, 2.0);
            multipliers.put(Rarity.EPIC, 3.0);
            multipliers.put(Rarity.LEGENDARY, 5.0);
            return new LootTemplate(0.1, multipliers, true, "");
        }

        public static Builder builder() {
            return new Builder();
        }

        public double getJadeSlipChance(Rarity rarity) {
            return baseJadeSlipChance * rarityMultipliers.getOrDefault(rarity, 1.0);
        }

        public boolean isTopProdigyAlwaysDropsRealm() {
            return topProdigyAlwaysDropsRealm;
        }

        public String getCustomLootTable() {
            return customLootTable;
        }

        public static class Builder {
            private double baseJadeSlipChance = 0.1;
            private final Map<Rarity, Double> rarityMultipliers = new ConcurrentHashMap<>();
            private boolean topProdigyAlwaysDropsRealm = true;
            private String customLootTable = "";

            private Builder() {
                rarityMultipliers.put(Rarity.COMMON, 1.0);
                rarityMultipliers.put(Rarity.UNCOMMON, 1.5);
                rarityMultipliers.put(Rarity.RARE, 2.0);
                rarityMultipliers.put(Rarity.EPIC, 3.0);
                rarityMultipliers.put(Rarity.LEGENDARY, 5.0);
            }

            public Builder baseJadeSlipChance(double chance) {
                this.baseJadeSlipChance = chance;
                return this;
            }

            public Builder rarityMultiplier(Rarity rarity, double multiplier) {
                this.rarityMultipliers.put(rarity, multiplier);
                return this;
            }

            public Builder topProdigyAlwaysDropsRealm(boolean value) {
                this.topProdigyAlwaysDropsRealm = value;
                return this;
            }

            public Builder customLootTable(String lootTable) {
                this.customLootTable = lootTable;
                return this;
            }

            public LootTemplate build() {
                return new LootTemplate(baseJadeSlipChance, rarityMultipliers,
                        topProdigyAlwaysDropsRealm, customLootTable);
            }
        }
    }
}