package com.mingsiamz.xuanlingmod;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
    // 	.comment("Whether to log the dirt block on common setup")
    // 	.define("logDirtBlock", true);

    // public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
    // 	.comment("A magic number")
    // 	.defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    // public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
    // 	.comment("What you want the introduction message to be for the magic number")
    // 	.define("magicNumberIntroduction", "The magic number is... ");

    // // a list of strings that are treated as resource locations for items
    // public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
    // 	.comment("A list of items to log on common setup.")
    // 	.defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);

    public static final ModConfigSpec.DoubleValue RANGE_NORMAL_LIFE = BUILDER
	.comment("身寿的最小与最大值")
	.defineInRange("range_normal_life", 0, 0, 50000.0);

    public static final ModConfigSpec.DoubleValue RANGE_SOUL_LIFE = BUILDER
	.comment("灵寿的最小与最大值")
	.defineInRange("range_soul_life", 0, 0, 50000.0);

    public static final ModConfigSpec.DoubleValue RANGE_MOVE_SPEED = BUILDER
	.comment("移动速度加成的最小与最大值")
	.defineInRange("range_move_speed_modifier", 0, 0, 100.0);

    public static final ModConfigSpec.DoubleValue RANGE_FLY_SPEED = BUILDER
	.comment("飞行速度加成的最小与最大值")
	.defineInRange("range_fly_speed_modifier", 0, 0, 100.0);

    public static final ModConfigSpec.DoubleValue RANGE_ENERGY = BUILDER
	.comment("所有能量的最小与最大值")
	.defineInRange("range_energy", 0, 0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue RANGE_TPAO = BUILDER
	.comment("先天一窍的最小与最大值，最大不可超过" + Double.MAX_VALUE)
	.defineInRange("range_tpao", 0, 0, 50000.0);

    public static final ModConfigSpec.DoubleValue RANGE_INSIGHT = BUILDER
	.comment("悟性的最小与最大值，最大不可超过" + Double.MAX_VALUE)
	.defineInRange("range_insight", 0, 0, 50000.0);

    public static final ModConfigSpec.DoubleValue RANGE_INSPIRATION = BUILDER
	.comment("灵感的最小与最大值，最大不可超过" + Double.MAX_VALUE)
	.defineInRange("range_inspiration", 0, 0, 50000.0);

    public static final ModConfigSpec.DoubleValue RANGE_LUCKY = BUILDER
	.comment("机缘的最小与最大值，最大不可超过" + Double.MAX_VALUE)
	.defineInRange("range_lucky", 0, 0, 50000.0);

    public static final ModConfigSpec.DoubleValue RANGE_CHARM = BUILDER
	.comment("魅力的最小与最大值，最大不可超过" + Double.MAX_VALUE)
	.defineInRange("range_lucky", 0, 0, 50000.0);

    static final ModConfigSpec SPEC = BUILDER.build();


    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    public static double getMaxValue(ModConfigSpec.DoubleValue spec) {
	return (double)spec.getSpec().getRange().getMax();
    }

    public static double getMinValue(ModConfigSpec.DoubleValue spec) {
	return (double)spec.getSpec().getRange().getMin();
    }
}
