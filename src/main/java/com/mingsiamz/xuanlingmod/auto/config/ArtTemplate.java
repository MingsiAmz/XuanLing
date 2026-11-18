package com.mingsiamz.xuanlingmod.auto.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mingsiamz.xuanlingmod.data.ComponentGroup;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public record ArtTemplate(
    String id,                              
    String displayName,                     
    List<String> prefixes,                  
    List<String> suffixes,                  
    ComponentGroup baseComponents,          
    Map<String, WeightedComponent> optionalComponents, 
    Rarity rarity,                          
    int minRealmLevel,                      
    int maxRealmLevel,                      
    Map<String, Double> statModifiers,      
    String description,                     
    String category,                        
    String elementType                      
) {
    public ArtTemplate {
        prefixes = prefixes == null ? List.of() : List.copyOf(prefixes);
        suffixes = suffixes == null ? List.of() : List.copyOf(suffixes);
        optionalComponents = optionalComponents == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(optionalComponents);
        statModifiers = statModifiers == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(statModifiers);
        rarity = rarity == null ? Rarity.COMMON : rarity;
        category = category == null ? "general" : category;
        elementType = elementType == null ? "none" : elementType;
        minRealmLevel = Math.max(0, minRealmLevel);
        maxRealmLevel = Math.max(minRealmLevel, maxRealmLevel);
    }

    public static Builder builder(String id, String displayName) {
        return new Builder(id, displayName);
    }

    public static class Builder {
        private final String id;
        private final String displayName;
        private List<String> prefixes = List.of();
        private List<String> suffixes = List.of();
        private ComponentGroup baseComponents = new ComponentGroup();
        private final Map<String, WeightedComponent> optionalComponents = new ConcurrentHashMap<>();
        private Rarity rarity = Rarity.COMMON;
        private int minRealmLevel = 0;
        private int maxRealmLevel = 9;
        private final Map<String, Double> statModifiers = new ConcurrentHashMap<>();
        private String description = "";
        private String category = "general";
        private String elementType = "none";

        private Builder(String id, String displayName) {
            this.id = id;
            this.displayName = displayName;
        }

        public Builder prefixes(String... prefixes) {
            this.prefixes = List.of(prefixes);
            return this;
        }

        public Builder suffixes(String... suffixes) {
            this.suffixes = List.of(suffixes);
            return this;
        }

        public Builder baseComponents(ComponentGroup components) {
            this.baseComponents = components;
            return this;
        }

        public Builder addOptionalComponent(String key, WeightedComponent component) {
            this.optionalComponents.put(key, component);
            return this;
        }

        public Builder addOptionalComponent(String componentName, String subType, int weight) {
            this.optionalComponents.put(componentName, WeightedComponent.of(componentName, subType, weight));
            return this;
        }

        public Builder rarity(Rarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder realmLevelRange(int min, int max) {
            this.minRealmLevel = min;
            this.maxRealmLevel = max;
            return this;
        }

        public Builder statModifier(String stat, double modifier) {
            this.statModifiers.put(stat, modifier);
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder elementType(String elementType) {
            this.elementType = elementType;
            return this;
        }

        public ArtTemplate build() {
            return new ArtTemplate(id, displayName, prefixes, suffixes, baseComponents,
                    optionalComponents, rarity, minRealmLevel, maxRealmLevel,
                    statModifiers, description, category, elementType);
        }
    }

    
    public static ArtTemplate fromJson(String id, JsonObject object) {
        try {
            String displayName = object.has("display_name") ? object.get("display_name").getAsString() : id;
            Rarity rarity = object.has("rarity") ? Rarity.valueOf(object.get("rarity").getAsString().toUpperCase()) : Rarity.COMMON;
            String category = object.has("category") ? object.get("category").getAsString() : "general";
            String elementType = object.has("element_type") ? object.get("element_type").getAsString() : "none";
            String description = object.has("description") ? object.get("description").getAsString() : "";
            int minRealmLevel = object.has("min_realm_level") ? object.get("min_realm_level").getAsInt() : 0;
            int maxRealmLevel = object.has("max_realm_level") ? object.get("max_realm_level").getAsInt() : 9;

            
            List<String> prefixes = new ArrayList<>();
            List<String> suffixes = new ArrayList<>();
            if (object.has("prefixes")) {
                JsonArray arr = object.getAsJsonArray("prefixes");
                for (JsonElement e : arr) prefixes.add(e.getAsString());
            }
            if (object.has("suffixes")) {
                JsonArray arr = object.getAsJsonArray("suffixes");
                for (JsonElement e : arr) suffixes.add(e.getAsString());
            }

            
            Map<String, Double> statModifiers = new ConcurrentHashMap<>();
            if (object.has("stat_modifiers")) {
                JsonObject mods = object.getAsJsonObject("stat_modifiers");
                for (Map.Entry<String, JsonElement> entry : mods.entrySet()) {
                    statModifiers.put(entry.getKey(), entry.getValue().getAsDouble());
                }
            }

            
            ComponentGroup baseComponents = new ComponentGroup();
            Map<String, WeightedComponent> optionalComponents = new ConcurrentHashMap<>();
            
            if (object.has("base_components")) {
                JsonArray arr = object.getAsJsonArray("base_components");
                for (JsonElement e : arr) {
                    
                }
            }
            
            if (object.has("optional_components")) {
                JsonObject opt = object.getAsJsonObject("optional_components");
                for (Map.Entry<String, JsonElement> entry : opt.entrySet()) {
                    JsonObject comp = entry.getValue().getAsJsonObject();
                    String compName = comp.has("component") ? comp.get("component").getAsString() : entry.getKey();
                    String subType = comp.has("sub_type") ? comp.get("sub_type").getAsString() : "";
                    int weight = comp.has("weight") ? comp.get("weight").getAsInt() : 1;
                    optionalComponents.put(entry.getKey(), WeightedComponent.of(compName, subType, weight));
                }
            }

            return new Builder(id, displayName)
                    .prefixes(prefixes.toArray(new String[0]))
                    .suffixes(suffixes.toArray(new String[0]))
                    .baseComponents(baseComponents)
                    .rarity(rarity)
                    .category(category)
                    .elementType(elementType)
                    .description(description)
                    .realmLevelRange(minRealmLevel, maxRealmLevel)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    
    public String generateName(java.util.Random random) {
        StringBuilder name = new StringBuilder();
        if (!prefixes.isEmpty() && random.nextDouble() < 0.7) {
            name.append(prefixes.get(random.nextInt(prefixes.size()))).append(" ");
        }
        name.append(displayName);
        if (!suffixes.isEmpty()) {
            name.append(suffixes.get(random.nextInt(suffixes.size())));
        }
        return name.toString();
    }
}