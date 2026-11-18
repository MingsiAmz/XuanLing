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
import net.neoforged.neoforge.common.ModConfigSpec.EnumValue;
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
                        .defineInRange("range_charm", 0, 0, 50000.0);

        public static final ModConfigSpec.DoubleValue RANGE_M_SENSE = BUILDER
                        .comment("神识的最小与最大值，最大不可超过" + Double.MAX_VALUE)
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

        /** 特效渲染等级：客户端按等级本地简化渲染，服务端不做粒子计算 */
        public enum SpecialEffectLevel {
                LOW, MEDIUM, HIGH
        }

        /** 境界所需修为平滑曲线：needExp(level) = base × growthRate^level，高境界指数级变慢 */
        public static final ModConfigSpec.DoubleValue REALM_EXP_BASE = BUILDER
                        .comment("境界突破所需修为的基础值，公式 needExp(level) = base × growthRate^level")
                        .defineInRange("realm_exp_base", 100.0, 0, Double.MAX_VALUE);

        public static final ModConfigSpec.DoubleValue REALM_EXP_GROWTH_RATE = BUILDER
                        .comment("修为增长率，建议取值 [1.5, 1.8]，越大高境界修炼越慢")
                        .defineInRange("realm_exp_growth_rate", 1.6, 1.0, 5.0);

        /** 渡天劫的最小大境界等级：4 = 元婴，练气/筑基/金丹不渡天劫 */
        public static final ModConfigSpec.IntValue TRIBULATION_MIN_LEVEL = BUILDER
                        .comment("渡天劫的最小大境界等级，4 表示元婴及以上才触发天劫")
                        .defineInRange("tribulation_min_level", 4, 0, 100);

        /** 道德值上限 */
        public static final ModConfigSpec.IntValue MORALITY_MAX = BUILDER
                        .comment("道德值上限")
                        .defineInRange("morality_max", 1000, 0, 100000);

        /** 天谴值上限 */
        public static final ModConfigSpec.IntValue CONDEMNATION_MAX = BUILDER
                        .comment("天谴值上限")
                        .defineInRange("condemnation_max", 1000, 0, 100000);

        /** 击杀敌对生物或高天谴值目标时，道德值增加量 */
        public static final ModConfigSpec.IntValue KILL_MORALITY_CHANGE = BUILDER
                        .comment("击杀敌对生物或高天谴值目标时，道德值增加量")
                        .defineInRange("kill_morality_change", 10, 0, 1000);

        /** 击杀友好/中立生物或高道德值目标时，天谴值增加量 */
        public static final ModConfigSpec.IntValue KILL_CONDEMNATION_CHANGE = BUILDER
                        .comment("击杀友好/中立生物或高道德值目标时，天谴值增加量")
                        .defineInRange("kill_condemnation_change", 10, 0, 1000);

        /** 每消耗多少个游戏天减少 1 月寿命，固定不随境界浮动 */
        public static final ModConfigSpec.IntValue LIFESPAN_DAYS_PER_MONTH = BUILDER
                        .comment("每消耗多少个游戏天减少 1 月寿命，固定 7 天，不随境界浮动")
                        .defineInRange("lifespan_days_per_month", 7, 1, 365);

        /** 天劫扣寿基础值，对应元婴（level 4）失败扣除的月寿命，公式 base × growthRate^(level - 4) */
        public static final ModConfigSpec.DoubleValue TRIBULATION_LIFESPAN_BASE = BUILDER
                        .comment("天劫失败扣寿基础值，对应元婴 level 4 扣除的月寿命，默认 200 个月")
                        .defineInRange("tribulation_lifespan_base", 200.0, 0, Double.MAX_VALUE);

        /** 天劫扣寿曲线增长率，建议取值 [1.6, 1.8]，越大高境界扣寿越多 */
        public static final ModConfigSpec.DoubleValue TRIBULATION_LIFESPAN_GROWTH_RATE = BUILDER
                        .comment("天劫扣寿曲线增长率，建议取值 [1.0, 5.0]，越大高境界扣寿越多")
                        .defineInRange("tribulation_lifespan_growth_rate", 1.7, 1.0, 5.0);

        /** 重伤 debuff 的修炼效率惩罚，默认 0.2 即修炼效率降至原来的 1/5 */
        public static final ModConfigSpec.DoubleValue INJURY_DEBUFF_CULTIVATION_PENALTY = BUILDER
                        .comment("重伤 debuff 的修炼效率惩罚，0-1 之间，默认 0.2 表示效率降至原来的 20%")
                        .defineInRange("injury_debuff_cultivation_penalty", 0.2, 0, 1);

        /** 善恶标签的善阈值，morality/(morality+condemnation) >= 该值时标记为“善” */
        public static final ModConfigSpec.DoubleValue ALIGNMENT_GOOD_THRESHOLD = BUILDER
                        .comment("善标签阈值，0-1 之间，默认 0.8，占比 >= 0.8 标记为善")
                        .defineInRange("alignment_good_threshold", 0.8, 0, 1);

        /** 善恶标签的恶阈值，morality/(morality+condemnation) <= 该值时标记为“恶” */
        public static final ModConfigSpec.DoubleValue ALIGNMENT_EVIL_THRESHOLD = BUILDER
                        .comment("恶标签阈值，0-1 之间，默认 0.2，占比 <= 0.2 标记为恶")
                        .defineInRange("alignment_evil_threshold", 0.2, 0, 1);

        /** 客户端特效等级：LOW/MEDIUM/HIGH，低等级下仅显示简单光环 */
        public static final ModConfigSpec.EnumValue<SpecialEffectLevel> SPECIAL_EFFECT_LEVEL = BUILDER
                        .comment("特效渲染等级：LOW/MEDIUM/HIGH，客户端按等级简化渲染，降低低配机压力")
                        .defineEnum("special_effect_level", SpecialEffectLevel.MEDIUM);

        /* ================================================================
         * 01-config-new-items：Config 新增项（天劫扣寿曲线、重伤惩罚、善恶阈值）
         * 所有数值不再硬编码，游戏逻辑应通过以下访问器读取默认值与范围。
         * ================================================================ */

        /**
         * 天劫扣寿基础值，对应元婴（level 4）失败扣除的月寿命，公式 base × growthRate^(level - 4)
         *
         * @return 默认 200，最小 0
         */
        public static double getTribulationLifespanBase() {
                return TRIBULATION_LIFESPAN_BASE.get();
        }

        /**
         * 天劫扣寿曲线增长率，建议取值 [1.0, 5.0]，越大高境界扣寿越多
         *
         * @return 默认 1.7，范围 [1.0, 5.0]
         */
        public static double getTribulationLifespanGrowthRate() {
                return TRIBULATION_LIFESPAN_GROWTH_RATE.get();
        }

        /**
         * 重伤 debuff 的修炼效率惩罚，0-1 之间，默认 0.2 表示效率降至原来的 20%
         *
         * @return 默认 0.2，范围 [0, 1]
         */
        public static double getInjuryDebuffCultivationPenalty() {
                return INJURY_DEBUFF_CULTIVATION_PENALTY.get();
        }

        /**
         * 善标签阈值，0-1 之间，占比 >= 该值标记为“善”
         *
         * @return 默认 0.8，范围 [0, 1]
         */
        public static double getAlignmentGoodThreshold() {
                return ALIGNMENT_GOOD_THRESHOLD.get();
        }

        /**
         * 恶标签阈值，0-1 之间，占比 <= 该值标记为“恶”
         *
         * @return 默认 0.2，范围 [0, 1]
         */
        public static double getAlignmentEvilThreshold() {
                return ALIGNMENT_EVIL_THRESHOLD.get();
        }

        /**
         * 击杀敌对生物或高天谴值目标时，道德值增加量
         *
         * @return 默认 10，范围 [0, 1000]
         */
        public static int getKillMoralityChange() {
                return KILL_MORALITY_CHANGE.get();
        }

        /**
         * 击杀友好/中立生物或高道德值目标时，天谴值增加量
         *
         * @return 默认 10，范围 [0, 1000]
         */
        public static int getKillCondemnationChange() {
                return KILL_CONDEMNATION_CHANGE.get();
        }

        /**
         * 计算天劫失败扣除的寿命月数：deduction = base × growthRate^(level - 4)
         * （元婴 level 4 扣除 base，更高境界按增长率指数递增）
         *
         * @param level 突破的境界等级
         * @return 扣除的月寿命
         */
        public static double getTribulationLifespanDeduction(int level) {
                return getTribulationLifespanBase()
                                * Math.pow(getTribulationLifespanGrowthRate(), level - 4);
        }

        /** 参悟所需参悟值基础值，公式 needComprehensionExp(level) = base × growthRate^level */
        public static final ModConfigSpec.DoubleValue COMPREHENSION_EXP_BASE = BUILDER
                        .comment("参悟升级所需参悟值基础值，公式 needComprehensionExp(level) = base × growthRate^level")
                        .defineInRange("comprehension_exp_base", 100.0, 0, Double.MAX_VALUE);

        /** 参悟增长率，建议取值 [1.5, 1.8] */
        public static final ModConfigSpec.DoubleValue COMPREHENSION_EXP_GROWTH_RATE = BUILDER
                        .comment("参悟增长率，建议取值 [1.5, 1.8]，越大高阶参悟越慢")
                        .defineInRange("comprehension_exp_growth_rate", 1.6, 1.0, 5.0);

        /** 顿悟概率，参悟时触发顿悟获得额外参悟值 */
        public static final ModConfigSpec.DoubleValue ENLIGHTENMENT_CHANCE = BUILDER
                        .comment("参悟时触发顿悟的概率，0-1 之间")
                        .defineInRange("enlightenment_chance", 0.05, 0, 1);

        /** 走火入魔概率，参悟时触发走火入魔受到伤害并失去参悟值 */
        public static final ModConfigSpec.DoubleValue DEVIATION_CHANCE = BUILDER
                        .comment("参悟时触发走火入魔的概率，0-1 之间")
                        .defineInRange("deviation_chance", 0.02, 0, 1);

        /** 参悟每消耗多少月寿命，固定不随境界浮动 */
        public static final ModConfigSpec.IntValue COMPREHENSION_LIFESPAN_COST = BUILDER
                        .comment("参悟每消耗多少月寿命，固定不随境界浮动")
                        .defineInRange("comprehension_lifespan_cost", 1, 1, 100);

        /**
         * 计算指定境界所需修为：needExp(level) = base × growthRate^level
         *
         * @param level 境界等级
         * @return 升级所需修为
         */
        public static double needExp(int level) {
                return REALM_EXP_BASE.get() * Math.pow(REALM_EXP_GROWTH_RATE.get(), level);
        }

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

        public static Integer getMaxValue(ModConfigSpec.IntValue spec) {
                return (Integer) spec.getSpec().getRange().getMax();
        }

        public static Integer getMinValue(ModConfigSpec.IntValue spec) {
                return (Integer) spec.getSpec().getRange().getMin();
        }

        public static Double getDefaultValue(ModConfigSpec.DoubleValue spec) {
                return (Double) spec.getSpec().getDefault();
        }

        public static Integer getDefaultValue(ModConfigSpec.IntValue spec) {
                return (Integer) spec.getSpec().getDefault();
        }
}
