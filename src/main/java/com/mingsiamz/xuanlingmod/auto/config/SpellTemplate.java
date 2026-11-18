package com.mingsiamz.xuanlingmod.auto.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mingsiamz.xuanlingmod.data.ComponentGroup;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public record SpellTemplate(
    String id,                              
    String displayName,                     
    List<String> prefixes,                  
    List<String> suffixes,                  
    ComponentGroup baseComponents,          
    Map<String, WeightedComponent> optionalComponents, 
    Rarity rarity,                          
    ElementType element,                    
    int minRealmLevel,                      
    CooldownTemplate cooldownTemplate,      
    int maxTargets,                         
    double baseDamage,                      
    double baseHeal,                        
    String description,                     
    String category,                        
    Map<String, Double> statScaling         
) {
    public SpellTemplate {
        prefixes = prefixes == null ? List.of() : List.copyOf(prefixes);
        suffixes = suffixes == null ? List.of() : List.copyOf(suffixes);
        optionalComponents = optionalComponents == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(optionalComponents);
        rarity = rarity == null ? Rarity.COMMON : rarity;
        element = element == null ? ElementType.NONE : element;
        cooldownTemplate = cooldownTemplate == null ? CooldownTemplate.defaultTemplate() : cooldownTemplate;
        description = description == null ? "" : description;
        category = category == null ? "general" : category;
        statScaling = statScaling == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(statScaling);
        minRealmLevel = Math.max(0, minRealmLevel);
        maxTargets = Math.max(1, maxTargets);
        baseDamage = Math.max(0, baseDamage);
        baseHeal = Math.max(0, baseHeal);
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
        private ElementType element = ElementType.NONE;
        private int minRealmLevel = 1;
        private CooldownTemplate cooldownTemplate = CooldownTemplate.defaultTemplate();
        private int maxTargets = 1;
        private double baseDamage = 1.0;
        private double baseHeal = 0.0;
        private String description = "";
        private String category = "general";
        private final Map<String, Double> statScaling = new ConcurrentHashMap<>();

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

        public Builder element(ElementType element) {
            this.element = element;
            return this;
        }

        public Builder minRealmLevel(int level) {
            this.minRealmLevel = level;
            return this;
        }

        public Builder cooldownTemplate(CooldownTemplate template) {
            this.cooldownTemplate = template;
            return this;
        }

        public Builder maxTargets(int maxTargets) {
            this.maxTargets = maxTargets;
            return this;
        }

        public Builder baseDamage(double damage) {
            this.baseDamage = damage;
            return this;
        }

        public Builder baseHeal(double heal) {
            this.baseHeal = heal;
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

        public Builder statScaling(String stat, double scaling) {
            this.statScaling.put(stat, scaling);
            return this;
        }

        public SpellTemplate build() {
            return new SpellTemplate(id, displayName, prefixes, suffixes, baseComponents,
                    optionalComponents, rarity, element, minRealmLevel, cooldownTemplate,
                    maxTargets, baseDamage, baseHeal, description, category, statScaling);
        }
    }

    
    public static SpellTemplate fromJson(String id, JsonObject object) {
        try {
            String displayName = object.has("display_name") ? object.get("display_name").getAsString() : id;
            Rarity rarity = object.has("rarity") ? Rarity.valueOf(object.get("rarity").getAsString().toUpperCase()) : Rarity.COMMON;
            ElementType element = object.has("element") ? ElementType.valueOf(object.get("element").getAsString().toUpperCase()) : ElementType.NONE;
            String category = object.has("category") ? object.get("category").getAsString() : "general";
            String description = object.has("description") ? object.get("description").getAsString() : "";
            int minRealmLevel = object.has("min_realm_level") ? object.get("min_realm_level").getAsInt() : 1;
            int maxTargets = object.has("max_targets") ? object.get("max_targets").getAsInt() : 1;
            double baseDamage = object.has("base_damage") ? object.get("base_damage").getAsDouble() : 1.0;
            double baseHeal = object.has("base_heal") ? object.get("base_heal").getAsDouble() : 0.0;

            
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

            
            CooldownTemplate cooldownTemplate = CooldownTemplate.defaultTemplate();
            if (object.has("cooldown")) {
                JsonObject cd = object.getAsJsonObject("cooldown");
                cooldownTemplate = new CooldownTemplate(
                        cd.has("base") ? cd.get("base").getAsInt() : 10,
                        cd.has("per_level") ? cd.get("per_level").getAsInt() : 2,
                        cd.has("min") ? cd.get("min").getAsInt() : 1,
                        cd.has("max") ? cd.get("max").getAsInt() : 300
                );
            }

            
            Map<String, Double> statScaling = new ConcurrentHashMap<>();
            if (object.has("stat_scaling")) {
                JsonObject scaling = object.getAsJsonObject("stat_scaling");
                for (Map.Entry<String, JsonElement> entry : scaling.entrySet()) {
                    statScaling.put(entry.getKey(), entry.getValue().getAsDouble());
                }
            }

            
            ComponentGroup baseComponents = new ComponentGroup();
            Map<String, WeightedComponent> optionalComponents = new ConcurrentHashMap<>();
            
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
                    .element(element)
                    .category(category)
                    .description(description)
                    .minRealmLevel(minRealmLevel)
                    .maxTargets(maxTargets)
                    .baseDamage(baseDamage)
                    .baseHeal(baseHeal)
                    .cooldownTemplate(cooldownTemplate)
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    
    public String generateName(java.util.Random random) {
        StringBuilder name = new StringBuilder();
        if (!prefixes.isEmpty() && random.nextDouble() < 0.6) {
            name.append(prefixes.get(random.nextInt(prefixes.size()))).append(" ");
        }
        name.append(displayName);
        if (!suffixes.isEmpty()) {
            name.append(suffixes.get(random.nextInt(suffixes.size())));
        }
        return name.toString();
    }

    
    public static ComponentGroup createBaseComponentsForElement(ElementType element) {
        ComponentGroup group = new ComponentGroup();
        
        
        group.addComponent(com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum.SPELL, 
                com.mingsiamz.xuanlingmod.type.spell.BasicType.GROUP, 
                getBasicForElement(element));
        
        
        group.addComponent(com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum.SPELL,
                com.mingsiamz.xuanlingmod.type.spell.ActionType.GROUP,
                getActionForElement(element));
        
        
        group.addComponent(com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum.SPELL,
                com.mingsiamz.xuanlingmod.type.spell.SpecialType.GROUP,
                com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                        com.mingsiamz.xuanlingmod.type.spell.SpecialType.NORMAL.name));
        
        return group;
    }

    private static com.mingsiamz.xuanlingmod.type.ImpComponent getBasicForElement(ElementType element) {
        return switch (element) {
            case FIRE, LIGHTNING -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.BasicType.PROJECTLE.name);
            case WATER, ICE -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.BasicType.AOE.name);
            case EARTH, METAL -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.BasicType.SELF.name);
            case WIND, VOID -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.BasicType.PROJECTLE.name);
            default -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.BasicType.PROJECTLE.name);
        };
    }

    private static com.mingsiamz.xuanlingmod.type.ImpComponent getActionForElement(ElementType element) {
        return switch (element) {
            case FIRE, LIGHTNING, POISON -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.ActionType.DAMAGE.name);
            case WATER, WOOD, LIGHT -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.ActionType.RECOVER_HEALTH.name);
            case EARTH, METAL -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.ActionType.RECOVER_P_ENERGY.name);
            case WIND, VOID, DARK, SPIRIT -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.ActionType.RECOVER_C_ENERGY.name);
            default -> com.mingsiamz.xuanlingmod.type.RegistryComponent.getSpellComponent(
                    com.mingsiamz.xuanlingmod.type.spell.ActionType.DAMAGE.name);
        };
    }
}