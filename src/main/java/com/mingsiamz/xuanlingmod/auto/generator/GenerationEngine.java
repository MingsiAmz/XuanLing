package com.mingsiamz.xuanlingmod.auto.generator;

import com.mingsiamz.xuanlingmod.auto.config.ArtTemplate;
import com.mingsiamz.xuanlingmod.auto.config.RealmTemplate;
import com.mingsiamz.xuanlingmod.auto.config.NPCTemplate;
import com.mingsiamz.xuanlingmod.auto.config.Rarity;
import com.mingsiamz.xuanlingmod.auto.config.SpellTemplate;
import com.mingsiamz.xuanlingmod.auto.config.ElementType;
import com.mingsiamz.xuanlingmod.auto.config.GenerationConfig;
import com.mingsiamz.xuanlingmod.auto.registry.TemplateRegistry;
import com.mingsiamz.xuanlingmod.data.component.ComponentRealm;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class GenerationEngine {
    public static final String GENERATOR_DATA_TYPE = "xuanling:auto_generation_templates";
    
    private static GenerationEngine INSTANCE;

    private final TemplateRegistry registry;
    private final NameGenerator nameGenerator;
    private final StatGenerator statGenerator;
    private final Random random = ThreadLocalRandom.current();

    private GenerationEngine() {
        this.registry = TemplateRegistry.getInstance();
        this.nameGenerator = new NameGenerator();
        this.statGenerator = new StatGenerator();
        
        
        registry.setGenerators(nameGenerator, statGenerator);
    }

    public static GenerationEngine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GenerationEngine();
        }
        return INSTANCE;
    }

    public void initialize() {
        statGenerator.refreshFromConfig();
        statGenerator.setNameGenerator(nameGenerator);
        
        registerBuiltinTemplates();
    }

    
    
    private void registerBuiltinTemplates() {
        
        registerDefaultArtTemplates();
        
        registerDefaultSpellTemplates();
        
        registerDefaultRealmTemplates();
        
        registerDefaultNPCTemplates();
    }

    private void registerDefaultArtTemplates() {
        
        registry.registerArt(ArtTemplate.builder("art:neigong:basic", "混元")
                .prefixes("入门", "基础", "普通")
                .suffixes("心法", "吐纳", "诀")
                .rarity(Rarity.COMMON)
                .category("内功")
                .elementType("none")
                .realmLevelRange(0, 3)
                .statModifier("insight", 1.1)
                .description("最基础的内功心法，适合炼气期修炼")
                .build());

        registry.registerArt(ArtTemplate.builder("art:neigong:advanced", "太清")
                .prefixes("进阶", "精修", "内门")
                .suffixes("真经", "内功", "神篇")
                .rarity(Rarity.UNCOMMON)
                .category("内功")
                .elementType("light")
                .realmLevelRange(2, 5)
                .statModifier("insight", 1.3)
                .statModifier("mind_sense", 1.2)
                .description("太清门传承内功，注重神识培养")
                .build());

        registry.registerArt(ArtTemplate.builder("art:neigong:rare", "九转")
                .prefixes("上古", "失传", "秘传")
                .suffixes("大法", "神功", "仙经")
                .rarity(Rarity.RARE)
                .category("内功")
                .elementType("fire")
                .realmLevelRange(4, 7)
                .statModifier("insight", 1.5)
                .statModifier("inspiration", 1.3)
                .description("传说中能九死一生的顶级内功")
                .build());

        
        registry.registerArt(ArtTemplate.builder("art:shenfa:basic", "踏云")
                .prefixes("基础", "入门", "普通")
                .suffixes("步法", "身法", "轻功")
                .rarity(Rarity.COMMON)
                .category("身法")
                .elementType("wind")
                .realmLevelRange(0, 3)
                .statModifier("lucky", 1.2)
                .statModifier("speed", 1.1)
                .description("基础轻功，提升移动速度")
                .build());

        registry.registerArt(ArtTemplate.builder("art:shenfa:rare", "缩地")
                .prefixes("上古", "失传", "绝学")
                .suffixes("神行", "遁法", "大法")
                .rarity(Rarity.RARE)
                .category("身法")
                .elementType("wind")
                .realmLevelRange(3, 6)
                .statModifier("lucky", 1.5)
                .statModifier("speed", 1.4)
                .description("一步跨万里，缩地成寸的顶级身法")
                .build());

        
        registry.registerArt(ArtTemplate.builder("art:shenzhi:basic", "观想")
                .prefixes("入门", "基础", "浅显")
                .suffixes("法", "诀", "篇")
                .rarity(Rarity.COMMON)
                .category("神识")
                .elementType("spirit")
                .realmLevelRange(1, 4)
                .statModifier("mind_sense", 1.2)
                .statModifier("insight", 1.1)
                .description("基础神识观想法")
                .build());

        registry.registerArt(ArtTemplate.builder("art:shenzhi:epic", "天眼")
                .prefixes("太古", "洪荒", "鸿蒙")
                .suffixes("神通", "圣典", "天书")
                .rarity(Rarity.EPIC)
                .category("神识")
                .elementType("spirit")
                .realmLevelRange(6, 9)
                .statModifier("mind_sense", 2.0)
                .statModifier("insight", 1.5)
                .description("开天眼，洞察世间万物")
                .build());
    }

    private void registerDefaultSpellTemplates() {
        
        registry.registerSpell(SpellTemplate.builder("spell:attack:fireball", "火球")
                .prefixes("基础", "初级", "普通")
                .suffixes("术", "法", "诀")
                .rarity(Rarity.COMMON)
                .element(ElementType.FIRE)
                .minRealmLevel(1)
                .maxTargets(1)
                .baseDamage(1.5)
                .category("攻击")
                .statScaling("insight", 0.5)
                .description("凝聚火元素攻击敌人")
                .build());

        registry.registerSpell(SpellTemplate.builder("spell:attack:lightning", "雷击")
                .prefixes("进阶", "精通", "高深")
                .suffixes("神通", "天罚", "大法")
                .rarity(Rarity.RARE)
                .element(ElementType.LIGHTNING)
                .minRealmLevel(4)
                .maxTargets(3)
                .baseDamage(3.0)
                .category("攻击")
                .statScaling("insight", 0.8)
                .statScaling("lucky", 0.3)
                .description("召唤天雷打击敌人")
                .build());

        registry.registerSpell(SpellTemplate.builder("spell:attack:legendary", "天罚")
                .prefixes("太古", "洪荒", "鸿蒙")
                .suffixes("神罚", "道法", "天道")
                .rarity(Rarity.LEGENDARY)
                .element(ElementType.LIGHTNING)
                .minRealmLevel(8)
                .maxTargets(10)
                .baseDamage(10.0)
                .category("攻击")
                .statScaling("insight", 1.5)
                .statScaling("lucky", 0.5)
                .description("天道降下的惩罚，无法躲避")
                .build());

        
        registry.registerSpell(SpellTemplate.builder("spell:heal:basic", "疗伤")
                .prefixes("入门", "基础", "普通")
                .suffixes("术", "诀", "法")
                .rarity(Rarity.COMMON)
                .element(ElementType.WATER)
                .minRealmLevel(1)
                .maxTargets(1)
                .baseHeal(1.0)
                .category("辅助")
                .statScaling("mind_sense", 0.5)
                .description("基础治疗术")
                .build());

        registry.registerSpell(SpellTemplate.builder("spell:heal:advanced", "回春")
                .prefixes("上古", "失传", "秘传")
                .suffixes("神术", "妙手", "大法")
                .rarity(Rarity.EPIC)
                .element(ElementType.LIGHT)
                .minRealmLevel(5)
                .maxTargets(5)
                .baseHeal(5.0)
                .category("辅助")
                .statScaling("mind_sense", 1.0)
                .statScaling("charm", 0.3)
                .description("起死回生的顶级治疗神通")
                .build());

        
        registry.registerSpell(SpellTemplate.builder("spell:defense:shield", "护盾")
                .prefixes("基础", "初级", "普通")
                .suffixes("术", "法", "罡")
                .rarity(Rarity.UNCOMMON)
                .element(ElementType.EARTH)
                .minRealmLevel(2)
                .maxTargets(1)
                .baseDamage(0)
                .baseHeal(0)
                .category("防御")
                .statScaling("insight", 0.4)
                .description("生成土元素护盾抵挡攻击")
                .build());

        
        registry.registerSpell(SpellTemplate.builder("spell:movement:blink", "闪现")
                .prefixes("进阶", "精通", "高深")
                .suffixes("术", "步", "遁")
                .rarity(Rarity.RARE)
                .element(ElementType.WIND)
                .minRealmLevel(3)
                .maxTargets(1)
                .baseDamage(0)
                .baseHeal(0)
                .category("移动")
                .statScaling("lucky", 0.6)
                .statScaling("speed", 0.8)
                .description("短距离空间跳跃")
                .build());
    }

    private void registerDefaultRealmTemplates() {
        
        registry.registerRealm(RealmTemplate.builder("realm:standard:lianqi", "炼气", 1)
                .size(10)
                .baseStats(10, 5, 2, 100, 10)
                .breakthroughStats(50, 20, 10, 500, 50)
                .category("正统")
                .statWeight("insight", 1.0)
                .statWeight("inspiration", 1.0)
                .build());

        registry.registerRealm(RealmTemplate.builder("realm:standard:zhuji", "筑基", 2)
                .size(9)
                .baseStats(50, 30, 15, 500, 50)
                .breakthroughStats(200, 100, 50, 2000, 200)
                .category("正统")
                .statWeight("insight", 1.2)
                .statWeight("mind_sense", 1.1)
                .build());

        registry.registerRealm(RealmTemplate.builder("realm:standard:jindan", "金丹", 3)
                .size(9)
                .baseStats(200, 150, 80, 2000, 200)
                .breakthroughStats(1000, 500, 300, 10000, 1000)
                .category("正统")
                .statWeight("insight", 1.5)
                .statWeight("lucky", 1.2)
                .build());

        registry.registerRealm(RealmTemplate.builder("realm:standard:yuanying", "元婴", 4)
                .size(9)
                .baseStats(1000, 800, 500, 10000, 1000)
                .breakthroughStats(5000, 3000, 2000, 50000, 5000)
                .category("正统")
                .statWeight("mind_sense", 1.5)
                .statWeight("insight", 1.3)
                .build());

        
        registry.registerRealm(RealmTemplate.builder("realm:custom:chaos", "混沌", 0)
                .size(9)
                .rarity(Rarity.LEGENDARY)
                .baseStats(100, 100, 100, 1000, 100)
                .breakthroughStats(500, 500, 500, 5000, 500)
                .canTranscend(true)
                .category("禁忌")
                .statWeight("insight", 2.0)
                .statWeight("lucky", 2.0)
                .statWeight("inspiration", 2.0)
                .build());

        registry.registerRealm(RealmTemplate.builder("realm:custom:eternal", "永恒", 0)
                .size(3)
                .rarity(Rarity.LEGENDARY)
                .baseStats(1000, 1000, 1000, 10000, 1000)
                .breakthroughStats(10000, 10000, 10000, 100000, 10000)
                .canTranscend(true)
                .category("彼岸")
                .statWeight("insight", 3.0)
                .statWeight("mind_sense", 3.0)
                .statWeight("charm", 2.0)
                .build());
    }

    private void registerDefaultNPCTemplates() {
        
        registry.registerNPC(NPCTemplate.builder("npc:rogue:casual", "minecraft:zombie")
                .displayName("散修")
                .rarity(Rarity.COMMON)
                .faction("rogue")
                .title("游方修士")
                .artChance(0.3)
                .spellChance(0.1)
                .topProdigyChance(0.001)
                .statMultiplier("health", 1.0)
                .statMultiplier("damage", 1.0)
                .build());

        
        registry.registerNPC(NPCTemplate.builder("npc:sect:disciple", "minecraft:zombie")
                .displayName("内门弟子")
                .rarity(Rarity.UNCOMMON)
                .faction("sect")
                .title("内门弟子")
                .artChance(0.8)
                .spellChance(0.4)
                .topProdigyChance(0.005)
                .statMultiplier("health", 1.5)
                .statMultiplier("damage", 1.3)
                .build());

        
        registry.registerNPC(NPCTemplate.builder("npc:sect:core", "minecraft:zombie")
                .displayName("核心弟子")
                .rarity(Rarity.RARE)
                .faction("sect")
                .title("真传弟子")
                .artChance(1.0)
                .spellChance(0.8)
                .topProdigyChance(0.02)
                .hasCustomRealm(true)
                .customRealmTemplate(registry.getRealm("realm:custom:chaos"))
                .statMultiplier("health", 2.5)
                .statMultiplier("damage", 2.0)
                .build());

        
        registry.registerNPC(NPCTemplate.builder("npc:sect:elder", "minecraft:zombie")
                .displayName("长老")
                .rarity(Rarity.EPIC)
                .faction("sect")
                .title("长老")
                .artChance(1.0)
                .spellChance(1.0)
                .topProdigyChance(0.1)
                .hasCustomRealm(true)
                .customRealmTemplate(registry.getRealm("realm:custom:eternal"))
                .statMultiplier("health", 5.0)
                .statMultiplier("damage", 3.0)
                .build());

        
        registry.registerNPC(NPCTemplate.builder("npc:demon:lord", "minecraft:zombie")
                .displayName("魔头")
                .rarity(Rarity.LEGENDARY)
                .faction("demon")
                .title("魔尊")
                .artChance(1.0)
                .spellChance(1.0)
                .topProdigyChance(0.5)
                .hasCustomRealm(true)
                .customRealmTemplate(registry.getRealm("realm:custom:eternal"))
                .statMultiplier("health", 10.0)
                .statMultiplier("damage", 5.0)
                .build());
    }

    

    
    public StatGenerator.ArtGeneratedStats generateArt(Rarity rarity, ElementType element) {
        ArtTemplate template = registry.getRandomArt(random,
                t -> t.rarity() == rarity && (element == ElementType.NONE || "none".equals(t.elementType()) || element.name().equalsIgnoreCase(t.elementType())));

        if (template != null) {
            return statGenerator.generateArtStats(template, random);
        }
        return statGenerator.generateRandomArtStats(rarity, element == ElementType.NONE ? "none" : element.name(), random);
    }

    
    public StatGenerator.ArtGeneratedStats generateArt(String templateId) {
        ArtTemplate template = registry.getArt(templateId);
        if (template != null) {
            return statGenerator.generateArtStats(template, random);
        }
        return null;
    }

    
    public StatGenerator.SpellGeneratedStats generateSpell(Rarity rarity, ElementType element, int realmLevel) {
        SpellTemplate template = registry.getRandomSpell(random,
                t -> t.rarity() == rarity && (element == ElementType.NONE || t.element() == element));

        if (template != null) {
            return statGenerator.generateSpellStats(template, realmLevel, random);
        }
        return statGenerator.generateRandomSpellStats(rarity, element == ElementType.NONE ? "none" : element.name(), realmLevel, random);
    }

    
    public StatGenerator.SpellGeneratedStats generateSpell(String templateId, int realmLevel) {
        SpellTemplate template = registry.getSpell(templateId);
        if (template != null) {
            return statGenerator.generateSpellStats(template, realmLevel, random);
        }
        return null;
    }

    
    public StatGenerator.RealmGeneratedStats generateRealm(int majorLevel, int minorLevel) {
        String templateId = resolveRealmTemplateId(majorLevel);

        RealmTemplate template = registry.getRealm(templateId);
        if (template != null) {
            return statGenerator.generateRealmStats(template, minorLevel, random);
        }
        return null;
    }

    
    public ComponentRealm generateComponentRealm(int majorLevel, int minorLevel) {
        StatGenerator.RealmGeneratedStats stats = generateRealm(majorLevel, minorLevel);
        if (stats == null) return null;

        RealmTemplate template = registry.getRealm(resolveRealmTemplateId(majorLevel));
        
        if (template != null) {
            return template.toComponentRealmBuilder().build();
        }
        return null;
    }

    
    public StatGenerator.RealmGeneratedStats generateCustomRealm(NPCTemplate npcTemplate) {
        return statGenerator.generateCustomRealmStats(npcTemplate, random);
    }

    
    public StatGenerator.NPCGeneratedStats generateNPC(String templateId, int realmLevel) {
        NPCTemplate template = registry.getNPC(templateId);
        if (template == null) {
            
            template = registry.getRandomNPC(random, t -> true);
            if (template == null) return null;
        }
        
        boolean isTopProdigy = template.isTopProdigy(random);
        return statGenerator.generateNPCStats(template, realmLevel, isTopProdigy, random);
    }

    
    public StatGenerator.NPCGeneratedStats generateRandomNPC(Rarity rarity, int realmLevel) {
        NPCTemplate template = registry.getRandomNPC(random, t -> t.rarity() == rarity);
        if (template != null) {
            boolean isTopProdigy = template.isTopProdigy(random);
            return statGenerator.generateNPCStats(template, realmLevel, isTopProdigy, random);
        }
        return null;
    }

    
    public JadeSlipData generateJadeSlip(Rarity rarity, int realmLevel) {
        StatGenerator.RealmGeneratedStats realmStats = generateRealm(
                Math.max(1, Math.min(4, realmLevel)), 
                random.nextInt(9) + 1
        );
        
        return new JadeSlipData(
                "玉简·" + (realmStats != null ? realmStats.name() : "未知境界"),
                rarity,
                realmStats,
                GenerationConfig.getJadeSlipMultiplier(rarity)
        );
    }

    

    public record JadeSlipData(
        String name,
        Rarity rarity,
        StatGenerator.RealmGeneratedStats realmData,
        double dropMultiplier
    ) {}

    

    public TemplateRegistry getRegistry() {
        return registry;
    }

    public NameGenerator getNameGenerator() {
        return nameGenerator;
    }

    public StatGenerator getStatGenerator() {
        return statGenerator;
    }

    
    private static String resolveRealmTemplateId(int majorLevel) {
        return switch (majorLevel) {
            case 2 -> "realm:standard:zhuji";
            case 3 -> "realm:standard:jindan";
            case 4 -> "realm:standard:yuanying";
            default -> "realm:standard:lianqi";
        };
    }
}