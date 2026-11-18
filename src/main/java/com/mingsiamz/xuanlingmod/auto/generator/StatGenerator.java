package com.mingsiamz.xuanlingmod.auto.generator;

import com.mingsiamz.xuanlingmod.auto.config.ArtTemplate;
import com.mingsiamz.xuanlingmod.auto.config.RealmTemplate;
import com.mingsiamz.xuanlingmod.auto.config.NPCTemplate;
import com.mingsiamz.xuanlingmod.auto.config.Rarity;
import com.mingsiamz.xuanlingmod.auto.config.SpellTemplate;
import com.mingsiamz.xuanlingmod.auto.config.WeightedComponent;
import com.mingsiamz.xuanlingmod.auto.config.CooldownTemplate;
import com.mingsiamz.xuanlingmod.auto.config.RealmStatTemplate;
import com.mingsiamz.xuanlingmod.auto.config.GenerationConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;


public class StatGenerator {
    private final Random random = ThreadLocalRandom.current();

    
    private NameGenerator nameGenerator;

    
    private double insightMin = 0, insightMax = 100;
    private double inspirationMin = 0, inspirationMax = 100;
    private double luckyMin = 0, luckyMax = 100;
    private double charmMin = 0, charmMax = 100;
    private double mindSenseMin = 0, mindSenseMax = 100;

    
    private final Map<Rarity, Double> rarityStatMultipliers = new ConcurrentHashMap<>();

    
    private final Map<String, Map<String, Double>> elementStatBiases = new ConcurrentHashMap<>();

    
    private final Map<String, Map<String, Double>> npcBaseStats = new ConcurrentHashMap<>();

    public StatGenerator() {
        initializeDefaults();
    }

    public void setNameGenerator(NameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    private void initializeDefaults() {
        
        rarityStatMultipliers.put(Rarity.COMMON, 1.0);
        rarityStatMultipliers.put(Rarity.UNCOMMON, 1.3);
        rarityStatMultipliers.put(Rarity.RARE, 1.7);
        rarityStatMultipliers.put(Rarity.EPIC, 2.2);
        rarityStatMultipliers.put(Rarity.LEGENDARY, 3.0);

        
        elementStatBiases.put("fire", Map.of(
                "damage", 1.5, "insight", 1.2, "inspiration", 0.8
        ));
        elementStatBiases.put("water", Map.of(
                "heal", 1.5, "mind_sense", 1.2, "charm", 1.1
        ));
        elementStatBiases.put("wind", Map.of(
                "speed", 1.5, "lucky", 1.3, "inspiration", 1.1
        ));
        elementStatBiases.put("earth", Map.of(
                "health", 1.5, "defense", 1.3, "insight", 1.0
        ));
        elementStatBiases.put("lightning", Map.of(
                "damage", 1.4, "speed", 1.2, "insight", 1.1
        ));
        elementStatBiases.put("ice", Map.of(
                "control", 1.5, "mind_sense", 1.2, "defense", 1.1
        ));
        elementStatBiases.put("poison", Map.of(
                "damage_over_time", 1.5, "lucky", 1.1, "charm", 0.9
        ));
        elementStatBiases.put("light", Map.of(
                "heal", 1.4, "charm", 1.3, "mind_sense", 1.2
        ));
        elementStatBiases.put("dark", Map.of(
                "damage", 1.3, "control", 1.2, "lucky", 1.1
        ));
        elementStatBiases.put("spirit", Map.of(
                "mind_sense", 1.5, "insight", 1.3, "inspiration", 1.2
        ));

        
        npcBaseStats.put("minecraft:zombie", Map.of(
                "health", 20.0, "damage", 3.0, "speed", 0.23, "armor", 2.0
        ));
        npcBaseStats.put("minecraft:skeleton", Map.of(
                "health", 20.0, "damage", 4.0, "speed", 0.25, "armor", 0.0
        ));
        npcBaseStats.put("minecraft:creeper", Map.of(
                "health", 20.0, "damage", 25.0, "speed", 0.25, "armor", 0.0
        ));
        npcBaseStats.put("minecraft:spider", Map.of(
                "health", 16.0, "damage", 3.0, "speed", 0.3, "armor", 0.0
        ));
        npcBaseStats.put("minecraft:enderman", Map.of(
                "health", 40.0, "damage", 7.0, "speed", 0.3, "armor", 0.0
        ));
    }

    
    public void refreshFromConfig() {
        insightMin = GenerationConfig.GEN_INSIGHT_MIN.get();
        insightMax = GenerationConfig.GEN_INSIGHT_MAX.get();
        inspirationMin = GenerationConfig.GEN_INSPIRATION_MIN.get();
        inspirationMax = GenerationConfig.GEN_INSPIRATION_MAX.get();
        luckyMin = GenerationConfig.GEN_LUCKY_MIN.get();
        luckyMax = GenerationConfig.GEN_LUCKY_MAX.get();
        charmMin = GenerationConfig.GEN_CHARM_MIN.get();
        charmMax = GenerationConfig.GEN_CHARM_MAX.get();
        mindSenseMin = GenerationConfig.GEN_MIND_SENSE_MIN.get();
        mindSenseMax = GenerationConfig.GEN_MIND_SENSE_MAX.get();
    }

    

    
    public Map<String, Double> generateCultivationStats(Rarity rarity, Random random) {
        double multiplier = rarityStatMultipliers.getOrDefault(rarity, 1.0);
        
        Map<String, Double> stats = new HashMap<>();
        stats.put("insight", nextDouble(insightMin, insightMax, random) * multiplier);
        stats.put("inspiration", nextDouble(inspirationMin, inspirationMax, random) * multiplier);
        stats.put("lucky", nextDouble(luckyMin, luckyMax, random) * multiplier);
        stats.put("charm", nextDouble(charmMin, charmMax, random) * multiplier);
        stats.put("mind_sense", nextDouble(mindSenseMin, mindSenseMax, random) * multiplier);
        
        return stats;
    }

    
    public Map<String, Double> generateElementalStats(String elementType, Rarity rarity, Random random) {
        Map<String, Double> baseStats = generateCultivationStats(rarity, random);
        Map<String, Double> biases = elementStatBiases.get(elementType.toLowerCase());
        
        if (biases != null) {
            for (Map.Entry<String, Double> entry : biases.entrySet()) {
                String stat = entry.getKey();
                double bias = entry.getValue();
                baseStats.merge(stat, 0.0, (old, v) -> old * bias);
            }
        }
        
        return baseStats;
    }

    
    public Map<String, Double> applyTemplateModifiers(Map<String, Double> baseStats, 
                                                       ArtTemplate template) {
        Map<String, Double> result = new HashMap<>(baseStats);
        for (Map.Entry<String, Double> entry : template.statModifiers().entrySet()) {
            result.merge(entry.getKey(), entry.getValue(), (old, mod) -> old * mod);
        }
        return result;
    }

    public Map<String, Double> applyTemplateModifiers(Map<String, Double> baseStats, 
                                                       SpellTemplate template) {
        Map<String, Double> result = new HashMap<>(baseStats);
        for (Map.Entry<String, Double> entry : template.statScaling().entrySet()) {
            result.merge(entry.getKey(), entry.getValue(), (old, mod) -> old * mod);
        }
        return result;
    }

    

    
    public ArtGeneratedStats generateArtStats(ArtTemplate template, Random random) {
        Rarity rarity = template.rarity();
        String element = template.elementType();
        
        Map<String, Double> baseStats = element.equals("none") 
                ? generateCultivationStats(rarity, random)
                : generateElementalStats(element, rarity, random);
        
        Map<String, Double> finalStats = applyTemplateModifiers(baseStats, template);
        
        
        List<String> selectedComponents = selectOptionalComponents(template, random);
        
        return new ArtGeneratedStats(
                template.generateName(random),
                finalStats,
                selectedComponents,
                template.minRealmLevel(),
                template.maxRealmLevel(),
                template.category(),
                template.elementType()
        );
    }

    
    public ArtGeneratedStats generateRandomArtStats(Rarity rarity, String element, Random random) {
        Map<String, Double> baseStats = element.equals("none") 
                ? generateCultivationStats(rarity, random)
                : generateElementalStats(element, rarity, random);
        
        return new ArtGeneratedStats(
                nameGenerator != null ? nameGenerator.generateRandomArtName(rarity, random) : generateFallbackArtName(rarity, random),
                baseStats,
                List.of(),
                0, 9,
                "general",
                element
        );
    }

    

    
    public SpellGeneratedStats generateSpellStats(SpellTemplate template, int realmLevel, Random random) {
        Rarity rarity = template.rarity();
        String element = template.element().name().toLowerCase();
        
        Map<String, Double> baseStats = element.equals("none") 
                ? generateCultivationStats(rarity, random)
                : generateElementalStats(element, rarity, random);
        
        Map<String, Double> finalStats = applyTemplateModifiers(baseStats, template);
        
        
        int cooldown = template.cooldownTemplate().calculateCooldown(realmLevel, random);
        
        
        List<String> selectedComponents = selectOptionalComponents(template, random);
        
        return new SpellGeneratedStats(
                template.generateName(random),
                finalStats,
                selectedComponents,
                template.minRealmLevel(),
                template.maxTargets(),
                template.baseDamage(),
                template.baseHeal(),
                cooldown,
                template.category(),
                template.element()
        );
    }

    
    public SpellGeneratedStats generateRandomSpellStats(Rarity rarity, String element, int realmLevel, Random random) {
        Map<String, Double> baseStats = element.equals("none") 
                ? generateCultivationStats(rarity, random)
                : generateElementalStats(element, rarity, random);
        
        int cooldown = new CooldownTemplate(
                GenerationConfig.SPELL_BASE_COOLDOWN.get(),
                GenerationConfig.SPELL_COOLDOWN_PER_LEVEL.get(),
                GenerationConfig.SPELL_MIN_COOLDOWN.get(),
                GenerationConfig.SPELL_MAX_COOLDOWN.get()
        ).calculateCooldown(realmLevel, random);
        
        return new SpellGeneratedStats(
                nameGenerator != null ? nameGenerator.generateRandomSpellName(rarity, random) : generateFallbackSpellName(rarity, random),
                baseStats,
                List.of(),
                1, 1,
                1.0, 0.0,
                cooldown,
                "general",
                com.mingsiamz.xuanlingmod.auto.config.ElementType.valueOf(element.toUpperCase())
        );
    }

    

    
    public RealmGeneratedStats generateRealmStats(RealmTemplate template, int minorLevel, Random random) {
        double sizeMultiplier = GenerationConfig.CUSTOM_REALM_STAT_MULTIPLIER.get();
        
        
        double levelFactor = (double) minorLevel / template.size();
        
        Map<String, Double> baseStats = new HashMap<>();
        baseStats.put("health", template.baseStats().health() * levelFactor * sizeMultiplier);
        baseStats.put("parrent_energy", template.baseStats().parrentEnergy() * levelFactor * sizeMultiplier);
        baseStats.put("child_energy", template.baseStats().childEnergy() * levelFactor * sizeMultiplier);
        baseStats.put("normal_life", template.baseStats().normalLife() * levelFactor * sizeMultiplier);
        baseStats.put("soul_life", template.baseStats().soulLife() * levelFactor * sizeMultiplier);
        
        
        Map<String, Double> breakthroughBonus = new HashMap<>();
        if (minorLevel == template.size()) {
            breakthroughBonus.put("health", template.breakthroughStats().health() * sizeMultiplier);
            breakthroughBonus.put("parrent_energy", template.breakthroughStats().parrentEnergy() * sizeMultiplier);
            breakthroughBonus.put("child_energy", template.breakthroughStats().childEnergy() * sizeMultiplier);
            breakthroughBonus.put("normal_life", template.breakthroughStats().normalLife() * sizeMultiplier);
            breakthroughBonus.put("soul_life", template.breakthroughStats().soulLife() * sizeMultiplier);
        }
        
        return new RealmGeneratedStats(
                nameGenerator != null ? nameGenerator.generateRealmName(template, minorLevel, random) : generateFallbackPhaseName(minorLevel, template.size(), random),
                template.level(),
                minorLevel,
                template.size(),
                baseStats,
                breakthroughBonus,
                template.canTranscend(),
                template.category()
        );
    }

    
    public RealmGeneratedStats generateCustomRealmStats(NPCTemplate npcTemplate, Random random) {
        if (!npcTemplate.hasCustomRealm() || npcTemplate.customRealmTemplate() == null) {
            return null;
        }
        
        RealmTemplate template = npcTemplate.customRealmTemplate();
        int size = template.size();
        int minorLevel = random.nextInt(size) + 1;
        
        return generateRealmStats(template, minorLevel, random);
    }

    

    
    public NPCGeneratedStats generateNPCStats(NPCTemplate template, int realmLevel, boolean isTopProdigy, Random random) {
        
        Map<String, Double> baseEntityStats = new HashMap<>(npcBaseStats.getOrDefault(
                template.entityType(), Map.of("health", 20.0, "damage", 2.0, "speed", 0.25, "armor", 0.0)
        ));
        
        
        for (Map.Entry<String, Double> entry : template.statMultipliers().entrySet()) {
            baseEntityStats.merge(entry.getKey(), entry.getValue(), (old, mult) -> old * mult);
        }
        
        
        double rarityBonus = rarityStatMultipliers.getOrDefault(template.rarity(), 1.0);
        baseEntityStats.replaceAll((k, v) -> v * rarityBonus);
        
        
        double realmMultiplier = 1.0 + realmLevel * 0.15;
        baseEntityStats.replaceAll((k, v) -> v * realmMultiplier);
        
        
        if (isTopProdigy) {
            baseEntityStats.replaceAll((k, v) -> v * 1.5);
        }
        
        
        Map<String, Double> cultivationStats = generateCultivationStats(template.rarity(), random);
        
        
        ArtGeneratedStats artStats = null;
        if (random.nextDouble() < template.artChance()) {
            if (template.artTemplate() != null) {
                artStats = generateArtStats(template.artTemplate(), random);
            } else {
                artStats = generateRandomArtStats(template.rarity(), "none", random);
            }
        }
        
        
        SpellGeneratedStats spellStats = null;
        if (random.nextDouble() < template.spellChance()) {
            if (template.spellTemplate() != null) {
                spellStats = generateSpellStats(template.spellTemplate(), realmLevel, random);
            } else {
                spellStats = generateRandomSpellStats(template.rarity(), "none", realmLevel, random);
            }
        }
        
        
        RealmGeneratedStats customRealmStats = null;
        if (isTopProdigy && template.hasCustomRealm()) {
            customRealmStats = generateCustomRealmStats(template, random);
        }
        
        return new NPCGeneratedStats(
                template.displayName().isEmpty() ? template.entityType() : template.displayName(),
                template.title(),
                template.faction(),
                baseEntityStats,
                cultivationStats,
                artStats,
                spellStats,
                customRealmStats,
                isTopProdigy,
                realmLevel,
                template.rarity()
        );
    }

    

    private double nextDouble(double min, double max, Random random) {
        return min + random.nextDouble() * (max - min);
    }

    private List<String> selectOptionalComponents(ArtTemplate template, Random random) {
        List<String> selected = new ArrayList<>();
        int maxComponents = GenerationConfig.MAX_OPTIONAL_COMPONENTS.get();
        
        for (Map.Entry<String, WeightedComponent> entry : template.optionalComponents().entrySet()) {
            if (selected.size() >= maxComponents) break;
            if (random.nextInt(100) < entry.getValue().weight()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }

    private List<String> selectOptionalComponents(SpellTemplate template, Random random) {
        List<String> selected = new ArrayList<>();
        int maxComponents = GenerationConfig.MAX_OPTIONAL_COMPONENTS.get();
        
        for (Map.Entry<String, WeightedComponent> entry : template.optionalComponents().entrySet()) {
            if (selected.size() >= maxComponents) break;
            if (random.nextInt(100) < entry.getValue().weight()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }

    
    private String generateFallbackArtName(Rarity rarity, Random random) {
        String[] bases = {"混元", "太清", "九转", "乾坤", "玄黄", "洪荒", "鸿蒙"};
        String[] suffixes = {"诀", "心法", "真经", "大法", "神功", "仙经"};
        return bases[random.nextInt(bases.length)] + suffixes[random.nextInt(suffixes.length)];
    }

    private String generateFallbackSpellName(Rarity rarity, Random random) {
        String[] bases = {"焚天", "玄冥", "九天", "乾坤", "神雷", "业火", "寒冰"};
        String[] suffixes = {"掌", "剑", "指", "瞳", "域", "劫", "印"};
        return bases[random.nextInt(bases.length)] + suffixes[random.nextInt(suffixes.length)];
    }

    private String generateFallbackPhaseName(int minorLevel, int totalSize, Random random) {
        if (totalSize <= 3) {
            String[] phases = {"初期", "中期", "后期"};
            return phases[Math.min(minorLevel - 1, 2)];
        } else if (totalSize <= 9) {
            if (minorLevel <= 3) return "初期";
            if (minorLevel <= 6) return "中期";
            if (minorLevel <= 9) return "后期";
            return "大圆满";
        } else {
            if (minorLevel <= totalSize / 3) return "初期";
            if (minorLevel <= 2 * totalSize / 3) return "中期";
            if (minorLevel < totalSize) return "后期";
            return "大圆满";
        }
    }

    

    public record ArtGeneratedStats(
        String name,
        Map<String, Double> stats,
        List<String> optionalComponents,
        int minRealmLevel,
        int maxRealmLevel,
        String category,
        String elementType
    ) {}

    public record SpellGeneratedStats(
        String name,
        Map<String, Double> stats,
        List<String> optionalComponents,
        int minRealmLevel,
        int maxTargets,
        double baseDamage,
        double baseHeal,
        int cooldown,
        String category,
        com.mingsiamz.xuanlingmod.auto.config.ElementType element
    ) {}

    public record RealmGeneratedStats(
        String name,
        int majorLevel,
        int minorLevel,
        int size,
        Map<String, Double> baseStats,
        Map<String, Double> breakthroughBonus,
        boolean canTranscend,
        String category
    ) {}

    public record NPCGeneratedStats(
        String displayName,
        String title,
        String faction,
        Map<String, Double> entityStats,
        Map<String, Double> cultivationStats,
        ArtGeneratedStats art,
        SpellGeneratedStats spell,
        RealmGeneratedStats customRealm,
        boolean isTopProdigy,
        int realmLevel,
        Rarity rarity
    ) {}
}