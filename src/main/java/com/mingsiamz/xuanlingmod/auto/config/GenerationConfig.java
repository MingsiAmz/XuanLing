package com.mingsiamz.xuanlingmod.auto.config;

import com.mingsiamz.xuanlingmod.Config;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class GenerationConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    
    public static final ModConfigSpec.BooleanValue ENABLE_ART_GENERATION;
    public static final ModConfigSpec.BooleanValue ENABLE_REALM_GENERATION;
    public static final ModConfigSpec.BooleanValue ENABLE_SPELL_GENERATION;
    public static final ModConfigSpec.BooleanValue ENABLE_NPC_GENERATION;
    public static final ModConfigSpec.BooleanValue ENABLE_JADE_SLIP_DROPS;
    public static final ModConfigSpec.BooleanValue ENABLE_DATAPACK_TEMPLATES;

    
    public static final ModConfigSpec.IntValue WEIGHT_COMMON;
    public static final ModConfigSpec.IntValue WEIGHT_UNCOMMON;
    public static final ModConfigSpec.IntValue WEIGHT_RARE;
    public static final ModConfigSpec.IntValue WEIGHT_EPIC;
    public static final ModConfigSpec.IntValue WEIGHT_LEGENDARY;

    
    public static final ModConfigSpec.DoubleValue NPC_CULTIVATOR_CHANCE;
    public static final ModConfigSpec.DoubleValue NPC_ART_BASE_CHANCE;
    public static final ModConfigSpec.DoubleValue NPC_SPELL_BASE_CHANCE;
    public static final ModConfigSpec.DoubleValue NPC_TOP_PRODIGY_CHANCE;
    public static final ModConfigSpec.DoubleValue NPC_CUSTOM_REALM_CHANCE;

    
    public static final ModConfigSpec.DoubleValue JADE_SLIP_BASE_DROP_CHANCE;
    public static final ModConfigSpec.DoubleValue JADE_SLIP_COMMON_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue JADE_SLIP_UNCOMMON_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue JADE_SLIP_RARE_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue JADE_SLIP_EPIC_MULTIPLIER;
    public static final ModConfigSpec.DoubleValue JADE_SLIP_LEGENDARY_MULTIPLIER;
    public static final ModConfigSpec.BooleanValue JADE_SLIP_TOP_PRODIGY_ALWAYS_DROPS_REALM;

    
    public static final ModConfigSpec.IntValue ART_MIN_PREFIX_CHANCE;      
    public static final ModConfigSpec.IntValue ART_MIN_SUFFIX_CHANCE;
    public static final ModConfigSpec.IntValue SPELL_MIN_PREFIX_CHANCE;
    public static final ModConfigSpec.IntValue SPELL_MIN_SUFFIX_CHANCE;
    public static final ModConfigSpec.IntValue MAX_OPTIONAL_COMPONENTS;       

    
    public static final ModConfigSpec.DoubleValue GEN_INSIGHT_MIN;
    public static final ModConfigSpec.DoubleValue GEN_INSIGHT_MAX;
    public static final ModConfigSpec.DoubleValue GEN_INSPIRATION_MIN;
    public static final ModConfigSpec.DoubleValue GEN_INSPIRATION_MAX;
    public static final ModConfigSpec.DoubleValue GEN_LUCKY_MIN;
    public static final ModConfigSpec.DoubleValue GEN_LUCKY_MAX;
    public static final ModConfigSpec.DoubleValue GEN_CHARM_MIN;
    public static final ModConfigSpec.DoubleValue GEN_CHARM_MAX;
    public static final ModConfigSpec.DoubleValue GEN_MIND_SENSE_MIN;
    public static final ModConfigSpec.DoubleValue GEN_MIND_SENSE_MAX;

    
    public static final ModConfigSpec.IntValue SPELL_BASE_COOLDOWN;
    public static final ModConfigSpec.IntValue SPELL_COOLDOWN_PER_LEVEL;
    public static final ModConfigSpec.IntValue SPELL_MIN_COOLDOWN;
    public static final ModConfigSpec.IntValue SPELL_MAX_COOLDOWN;

    
    public static final ModConfigSpec.IntValue CUSTOM_REALM_MIN_SIZE;
    public static final ModConfigSpec.IntValue CUSTOM_REALM_MAX_SIZE;
    public static final ModConfigSpec.DoubleValue CUSTOM_REALM_STAT_MULTIPLIER;

    static {
        BUILDER.push("general");
        ENABLE_ART_GENERATION = BUILDER.comment("启用随机功法生成")
                .define("enable_art_generation", true);
        ENABLE_REALM_GENERATION = BUILDER.comment("启用随机境界生成")
                .define("enable_realm_generation", true);
        ENABLE_SPELL_GENERATION = BUILDER.comment("启用随机神通生成")
                .define("enable_spell_generation", true);
        ENABLE_NPC_GENERATION = BUILDER.comment("启用NPC生成系统")
                .define("enable_npc_generation", true);
        ENABLE_JADE_SLIP_DROPS = BUILDER.comment("启用玉简掉落")
                .define("enable_jade_slip_drops", true);
        ENABLE_DATAPACK_TEMPLATES = BUILDER.comment("启用数据包模板加载")
                .define("enable_datapack_templates", true);
        BUILDER.pop();

        BUILDER.push("rarity_weights");
        WEIGHT_COMMON = BUILDER.comment("普通权重")
                .defineInRange("common", 100, 1, 10000);
        WEIGHT_UNCOMMON = BUILDER.comment("优秀权重")
                .defineInRange("uncommon", 50, 1, 10000);
        WEIGHT_RARE = BUILDER.comment("稀有权重")
                .defineInRange("rare", 20, 1, 10000);
        WEIGHT_EPIC = BUILDER.comment("史诗权重")
                .defineInRange("epic", 5, 1, 10000);
        WEIGHT_LEGENDARY = BUILDER.comment("传说权重")
                .defineInRange("legendary", 1, 1, 10000);
        BUILDER.pop();

        BUILDER.push("npc");
        NPC_CULTIVATOR_CHANCE = BUILDER.comment("NPC成为修士的概率")
                .defineInRange("cultivator_chance", 0.08, 0.0, 1.0);
        NPC_ART_BASE_CHANCE = BUILDER.comment("NPC拥有功法基础概率")
                .defineInRange("art_base_chance", 0.3, 0.0, 1.0);
        NPC_SPELL_BASE_CHANCE = BUILDER.comment("NPC拥有神通基础概率")
                .defineInRange("spell_base_chance", 0.2, 0.0, 1.0);
        NPC_TOP_PRODIGY_CHANCE = BUILDER.comment("NPC成为顶级天骄概率")
                .defineInRange("top_prodigy_chance", 0.001, 0.0, 0.1);
        NPC_CUSTOM_REALM_CHANCE = BUILDER.comment("NPC拥有自定义境界概率(非天骄)")
                .defineInRange("custom_realm_chance", 0.0, 0.0, 0.05);
        BUILDER.pop();

        BUILDER.push("jade_slip");
        JADE_SLIP_BASE_DROP_CHANCE = BUILDER.comment("玉简基础掉落率")
                .defineInRange("base_drop_chance", 0.1, 0.0, 1.0);
        JADE_SLIP_COMMON_MULTIPLIER = BUILDER.comment("普通玉简掉落倍率")
                .defineInRange("common_multiplier", 1.0, 0.1, 10.0);
        JADE_SLIP_UNCOMMON_MULTIPLIER = BUILDER.comment("优秀玉简掉落倍率")
                .defineInRange("uncommon_multiplier", 1.5, 0.1, 10.0);
        JADE_SLIP_RARE_MULTIPLIER = BUILDER.comment("稀有玉简掉落倍率")
                .defineInRange("rare_multiplier", 2.0, 0.1, 10.0);
        JADE_SLIP_EPIC_MULTIPLIER = BUILDER.comment("史诗玉简掉落倍率")
                .defineInRange("epic_multiplier", 3.0, 0.1, 10.0);
        JADE_SLIP_LEGENDARY_MULTIPLIER = BUILDER.comment("传说玉简掉落倍率")
                .defineInRange("legendary_multiplier", 5.0, 0.1, 10.0);
        JADE_SLIP_TOP_PRODIGY_ALWAYS_DROPS_REALM = BUILDER.comment("顶级天骄必掉境界玉简")
                .define("top_prodigy_always_drops_realm", true);
        BUILDER.pop();

        BUILDER.push("generation");
        ART_MIN_PREFIX_CHANCE = BUILDER.comment("功法前缀最小生成几率(百分比)")
                .defineInRange("art_min_prefix_chance", 70, 0, 100);
        ART_MIN_SUFFIX_CHANCE = BUILDER.comment("功法后缀最小生成几率(百分比)")
                .defineInRange("art_min_suffix_chance", 100, 0, 100);
        SPELL_MIN_PREFIX_CHANCE = BUILDER.comment("神通前缀最小生成几率(百分比)")
                .defineInRange("spell_min_prefix_chance", 60, 0, 100);
        SPELL_MIN_SUFFIX_CHANCE = BUILDER.comment("神通后缀最小生成几率(百分比)")
                .defineInRange("spell_min_suffix_chance", 100, 0, 100);
        MAX_OPTIONAL_COMPONENTS = BUILDER.comment("最大可选组件数量")
                .defineInRange("max_optional_components", 3, 0, 10);
        BUILDER.pop();

        BUILDER.push("stat_ranges");
        GEN_INSIGHT_MIN = BUILDER.comment("生成内容悟性最小值")
                .defineInRange("gen_insight_min", 0.0, 0.0, 10000.0);
        GEN_INSIGHT_MAX = BUILDER.comment("生成内容悟性最大值")
                .defineInRange("gen_insight_max", 100.0, 0.0, 10000.0);
        GEN_INSPIRATION_MIN = BUILDER.comment("生成内容灵感最小值")
                .defineInRange("gen_inspiration_min", 0.0, 0.0, 10000.0);
        GEN_INSPIRATION_MAX = BUILDER.comment("生成内容灵感最大值")
                .defineInRange("gen_inspiration_max", 100.0, 0.0, 10000.0);
        GEN_LUCKY_MIN = BUILDER.comment("生成内容机缘最小值")
                .defineInRange("gen_lucky_min", 0.0, 0.0, 10000.0);
        GEN_LUCKY_MAX = BUILDER.comment("生成内容机缘最大值")
                .defineInRange("gen_lucky_max", 100.0, 0.0, 10000.0);
        GEN_CHARM_MIN = BUILDER.comment("生成内容魅力最小值")
                .defineInRange("gen_charm_min", 0.0, 0.0, 10000.0);
        GEN_CHARM_MAX = BUILDER.comment("生成内容魅力最大值")
                .defineInRange("gen_charm_max", 100.0, 0.0, 10000.0);
        GEN_MIND_SENSE_MIN = BUILDER.comment("生成内容神识最小值")
                .defineInRange("gen_mind_sense_min", 0.0, 0.0, 10000.0);
        GEN_MIND_SENSE_MAX = BUILDER.comment("生成内容神识最大值")
                .defineInRange("gen_mind_sense_max", 100.0, 0.0, 10000.0);
        BUILDER.pop();

        BUILDER.push("cooldown");
        SPELL_BASE_COOLDOWN = BUILDER.comment("神通基础冷却(秒)")
                .defineInRange("spell_base_cooldown", 10, 0, 3600);
        SPELL_COOLDOWN_PER_LEVEL = BUILDER.comment("每境界等级增加冷却(秒)")
                .defineInRange("spell_cooldown_per_level", 2, 0, 100);
        SPELL_MIN_COOLDOWN = BUILDER.comment("最小冷却(秒)")
                .defineInRange("spell_min_cooldown", 1, 0, 3600);
        SPELL_MAX_COOLDOWN = BUILDER.comment("最大冷却(秒)")
                .defineInRange("spell_max_cooldown", 300, 0, 3600);
        BUILDER.pop();

        BUILDER.push("realm_generation");
        CUSTOM_REALM_MIN_SIZE = BUILDER.comment("自定义境界最小层数")
                .defineInRange("custom_realm_min_size", 3, 1, 20);
        CUSTOM_REALM_MAX_SIZE = BUILDER.comment("自定义境界最大层数")
                .defineInRange("custom_realm_max_size", 10, 1, 20);
        CUSTOM_REALM_STAT_MULTIPLIER = BUILDER.comment("自定义境界属性倍率")
                .defineInRange("custom_realm_stat_multiplier", 1.0, 0.1, 10.0);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    
    private static volatile Map<Rarity, Integer> cachedRarityWeights = null;
    private static volatile int cachedTotalWeight = -1;

    
    public static Map<Rarity, Integer> getRarityWeights() {
        if (cachedRarityWeights == null) {
            Map<Rarity, Integer> weights = new ConcurrentHashMap<>();
            weights.put(Rarity.COMMON, WEIGHT_COMMON.get());
            weights.put(Rarity.UNCOMMON, WEIGHT_UNCOMMON.get());
            weights.put(Rarity.RARE, WEIGHT_RARE.get());
            weights.put(Rarity.EPIC, WEIGHT_EPIC.get());
            weights.put(Rarity.LEGENDARY, WEIGHT_LEGENDARY.get());
            cachedRarityWeights = weights;
        }
        return cachedRarityWeights;
    }

    
    public static int getTotalRarityWeight() {
        if (cachedTotalWeight < 0) {
            cachedTotalWeight = getRarityWeights().values().stream().mapToInt(Integer::intValue).sum();
        }
        return cachedTotalWeight;
    }

    
    public static double getJadeSlipMultiplier(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> JADE_SLIP_COMMON_MULTIPLIER.get();
            case UNCOMMON -> JADE_SLIP_UNCOMMON_MULTIPLIER.get();
            case RARE -> JADE_SLIP_RARE_MULTIPLIER.get();
            case EPIC -> JADE_SLIP_EPIC_MULTIPLIER.get();
            case LEGENDARY -> JADE_SLIP_LEGENDARY_MULTIPLIER.get();
        };
    }

    
    public static double getJadeSlipBaseDropChance() {
        return JADE_SLIP_BASE_DROP_CHANCE.get();
    }

    
    public static void refreshCache() {
        cachedRarityWeights = null;
        cachedTotalWeight = -1;
    }

    
    public static void register(ModConfig.Type type, String modId) {
        
    }
}