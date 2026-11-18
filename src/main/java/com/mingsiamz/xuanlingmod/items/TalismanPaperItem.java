package com.mingsiamz.xuanlingmod.items;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.component.TalismanPaperData;
import com.mingsiamz.xuanlingmod.register.RegisterDataComponents;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class TalismanPaperItem extends Item {

        public TalismanPaperItem(Properties properties) {
                super(properties);
        }

        private static Optional<TalismanPaperData> getData(ItemStack stack) {
                return Optional.ofNullable(stack.get(RegisterDataComponents.TALISMAN_PAPER_DATA.get()));
        }

        private static void setData(ItemStack stack, TalismanPaperData data) {
                stack.set(RegisterDataComponents.TALISMAN_PAPER_DATA.get(), data);
        }

        public static void setData(ItemStack stack, XuanLingTypeEnum type, String nickname, String description,
                        double speed) {
                TalismanPaperData data = TalismanPaperData.builder()
                                .type(type)
                                .nickname(nickname)
                                .description(description)
                                .speed(speed)
                                .build();
                setData(stack, data);
        }

        public static void setType(ItemStack stack, XuanLingTypeEnum type) {
                getData(stack).ifPresentOrElse(
                                data -> setData(stack, data.withType(type)),
                                () -> setData(stack, TalismanPaperData.builder().type(type).build()));
        }

        public static void setNickname(ItemStack stack, String nickname) {
                getData(stack).ifPresentOrElse(
                                data -> setData(stack, data.withNickname(nickname)),
                                () -> setData(stack, TalismanPaperData.builder().nickname(nickname).build()));
        }

        public static void setDescription(ItemStack stack, String description) {
                getData(stack).ifPresentOrElse(
                                data -> setData(stack, data.withDescription(description)),
                                () -> setData(stack, TalismanPaperData.builder().description(description).build()));
        }

        public static void setSpeed(ItemStack stack, double speed) {
                getData(stack).ifPresentOrElse(
                                data -> setData(stack, data.withSpeed(speed)),
                                () -> setData(stack, TalismanPaperData.builder().speed(speed).build()));
        }

        public static XuanLingTypeEnum getType(ItemStack stack) {
                return getData(stack).map(TalismanPaperData::getTypeEnum).orElse(null);
        }

        public static String getNickname(ItemStack stack) {
                return getData(stack).map(TalismanPaperData::nickname).orElse("");
        }

        public static String getDescription(ItemStack stack) {
                return getData(stack).map(TalismanPaperData::description).orElse("");
        }

        public static double getSpeed(ItemStack stack) {
                return getData(stack).map(TalismanPaperData::speed).orElse(1.0);
        }

        public static boolean hasData(ItemStack stack) {
                return getData(stack).map(TalismanPaperData::hasData).orElse(false);
        }

        @Override
        public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents,
                        TooltipFlag tooltipFlag) {
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

                Optional<TalismanPaperData> dataOpt = getData(stack);
                if (dataOpt.isEmpty() || !dataOpt.get().hasData()) {
                        tooltipComponents.add(Component.translatable("item.xuanling.talisman_paper.empty")
                                        .withStyle(ChatFormatting.GRAY));
                        return;
                }

                TalismanPaperData data = dataOpt.get();

                MutableComponent typeComp = Component.translatable("item.xuanling.talisman_paper.type",
                                data.getTypeDisplayName())
                                .withStyle(ChatFormatting.GOLD);
                tooltipComponents.add(typeComp);

                String nickname = data.nickname();
                if (!nickname.isEmpty()) {
                        tooltipComponents.add(Component.translatable("item.xuanling.talisman_paper.nickname",
                                        Component.literal(nickname))
                                        .withStyle(ChatFormatting.AQUA));
                }

                String speedStr = String.format("%.2f", data.speed());
                tooltipComponents.add(Component.translatable("item.xuanling.talisman_paper.speed",
                                Component.literal(speedStr))
                                .withStyle(ChatFormatting.GREEN));

                String description = data.description();
                if (!description.isEmpty()) {
                        tooltipComponents.add(Component.literal(" "));
                        String[] lines = description.split("\n");
                        for (String line : lines) {
                                if (line.length() > 80) {
                                        for (int i = 0; i < line.length(); i += 80) {
                                                tooltipComponents.add(Component.literal(
                                                                line.substring(i, Math.min(i + 80, line.length())))
                                                                .withStyle(ChatFormatting.GRAY));
                                        }
                                } else {
                                        tooltipComponents.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
                                }
                        }
                }
        }
}
