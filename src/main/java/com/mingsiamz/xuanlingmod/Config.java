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
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class Config {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

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

        public static final ModConfigSpec.DoubleValue PARRENT_ENERGY_RANGE = BUILDER
                        .comment("主能量的最小与最大值")
                        .defineInRange("parrent_energy_range", 0, 0, Double.MAX_VALUE);

        public static final ModConfigSpec.DoubleValue CHILD_ENERGY_RANGE = BUILDER
                        .comment("副能量的最小与最大值")
                        .defineInRange("child_energy_range", 0, 0, Double.MAX_VALUE);

        public static final ModConfigSpec.DoubleValue CUSTOM_ENERGY_RANGE = BUILDER
                        .comment("自定义能量的最小与最大值")
                        .defineInRange("custom_energy_range", 0, 0, Double.MAX_VALUE);

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

        public static final ModConfigSpec.DoubleValue RANGE_M_SENSE = BUILDER
                        .comment("魅力的最小与最大值，最大不可超过" + Double.MAX_VALUE)
                        .defineInRange("range_m_sense", 0, 0, 50000.0);

        public static final ModConfigSpec.IntValue ENERGY_BAR_X_VALUE = BUILDER
                        .comment("气条X坐标")
                        .defineInRange("energy_bar_x", 14, 0, 1024);

        public static final ModConfigSpec.IntValue ENERGY_BAR_Y_VALUE = BUILDER
                        .comment("气条Y坐标")
                .defineInRange("energy_bar_y", 14, 0, 1024);

        public static final ModConfigSpec.IntValue ENERGY_BAR_W_VALUE = BUILDER
                        .comment("气条宽度")
                .defineInRange("energy_bar_w", 180, 0, 1024);

        public static final ModConfigSpec.IntValue ENERGY_BAR_H_VALUE = BUILDER
                        .comment("气条高度")
                .defineInRange("energy_bar_h", 8, 0, 1024);

        public static final ModConfigSpec.IntValue NEILI_BAR_W_VALUE = BUILDER
                        .comment("内力条宽度")
                .defineInRange("neili_bar_w", 140, 0, 1024);

        public static final ModConfigSpec.IntValue NEILI_BAR_H_VALUE = BUILDER
                        .comment("内力条高度")
                .defineInRange("neili_bar_h", 2, 0, 1024);

        public static final ModConfigSpec.IntValue BAR_GAP_VALUE = BUILDER
                        .comment("条间距")
                .defineInRange("bar_gap", 1, 0, 1024);

        static final ModConfigSpec SPEC = BUILDER.build();

        private static boolean validateItemName(final Object obj) {
                return obj instanceof String itemName
                                && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
        }

        public static Double getMaxValue(ModConfigSpec.DoubleValue spec) {
                return (Double) spec.getSpec().getRange().getMax();
        }

        public static Double getMinValue(ModConfigSpec.DoubleValue spec) {
                return (Double) spec.getSpec().getRange().getMin();
        }
}
