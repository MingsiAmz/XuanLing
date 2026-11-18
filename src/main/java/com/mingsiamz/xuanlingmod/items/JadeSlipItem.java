package com.mingsiamz.xuanlingmod.items;

import com.mingsiamz.xuanlingmod.auto.config.Rarity;
import com.mingsiamz.xuanlingmod.auto.generator.GenerationEngine;
import com.mingsiamz.xuanlingmod.auto.generator.StatGenerator;
import com.mingsiamz.xuanlingmod.data.component.ComponentRealm;
import com.mingsiamz.xuanlingmod.data.component.JadeSlipData;
import com.mingsiamz.xuanlingmod.register.RegisterDataComponents;
import com.mingsiamz.xuanlingmod.register.RegisterItems;
import com.mingsiamz.xuanlingmod.XuanLing;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;


public class JadeSlipItem extends Item {
    public JadeSlipItem(Properties properties) {
        super(properties.stacksTo(1).fireResistant().rarity(net.minecraft.world.item.Rarity.COMMON));
    }

    
    public static ItemStack createBlank() {
        return new ItemStack(RegisterItems.JADE_SLIP.get());
    }

    
    public static ItemStack createWithRealm(ComponentRealm realm, Rarity rarity, String source, String creator) {
        ItemStack stack = createBlank();
        setRealmData(stack, realm);
        setRarity(stack, rarity);
        setSource(stack, source);
        setCreator(stack, creator);
        updateDisplayName(stack);
        return stack;
    }

    
    public static ItemStack createRandom(Rarity rarity, int realmLevel, String source) {
        GenerationEngine engine = GenerationEngine.getInstance();
        var jadeSlipData = engine.generateJadeSlip(rarity, realmLevel);

        if (jadeSlipData.realmData() != null) {
            
            ComponentRealm realm = convertToComponentRealm(jadeSlipData.realmData());
            ItemStack stack = createWithRealm(realm, rarity, source, "world_gen");

            
            var data = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
            if (data != null) {
                stack.set(RegisterDataComponents.JADE_SLIP_DATA.get(), data.withCreator("world_gen"));
            }
            return stack;
        }

        return createBlank();
    }

    
    public static ItemStack createFromTopProdigy(StatGenerator.NPCGeneratedStats npcStats, String killerName) {
        if (npcStats.customRealm() != null) {
            ComponentRealm realm = convertToComponentRealm(npcStats.customRealm());
            return createWithRealm(realm, npcStats.rarity(), "top_prodigy_kill", killerName);
        }
        return createBlank();
    }

    

    public static void setRealmData(ItemStack stack, ComponentRealm realm) {
        JadeSlipData currentData = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
        if (currentData == null) {
            currentData = JadeSlipData.EMPTY;
        }
        stack.set(RegisterDataComponents.JADE_SLIP_DATA.get(), currentData.withRealm(realm));
    }

    public static Optional<ComponentRealm> getRealmData(ItemStack stack) {
        JadeSlipData data = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
        if (data == null || data.realm() == null) {
            return Optional.empty();
        }
        return Optional.of(data.realm());
    }

    public static void setRarity(ItemStack stack, Rarity rarity) {
        JadeSlipData currentData = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
        if (currentData == null) {
            currentData = JadeSlipData.EMPTY;
        }
        stack.set(RegisterDataComponents.JADE_SLIP_DATA.get(), currentData.withRarity(rarity));
    }

    public static Rarity getRarity(ItemStack stack) {
        JadeSlipData data = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
        if (data == null) {
            return Rarity.COMMON;
        }
        return data.getRarityEnum();
    }

    public static void setSource(ItemStack stack, String source) {
        JadeSlipData currentData = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
        if (currentData == null) {
            currentData = JadeSlipData.EMPTY;
        }
        stack.set(RegisterDataComponents.JADE_SLIP_DATA.get(), currentData.withSource(source));
    }

    public static String getSource(ItemStack stack) {
        JadeSlipData data = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
        return data != null ? data.source() : "unknown";
    }

    public static void setCreator(ItemStack stack, String creator) {
        JadeSlipData currentData = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
        if (currentData == null) {
            currentData = JadeSlipData.EMPTY;
        }
        stack.set(RegisterDataComponents.JADE_SLIP_DATA.get(), currentData.withCreator(creator));
    }

    public static String getCreator(ItemStack stack) {
        JadeSlipData data = stack.get(RegisterDataComponents.JADE_SLIP_DATA.get());
        return data != null ? data.creator() : "";
    }

    public static boolean hasRealmData(ItemStack stack) {
        return getRealmData(stack).isPresent();
    }

    

    private static void updateDisplayName(ItemStack stack) {
        Optional<ComponentRealm> realmOpt = getRealmData(stack);
        Rarity rarity = getRarity(stack);

        if (realmOpt.isPresent()) {
            ComponentRealm realm = realmOpt.get();
            String realmName = realm.getName() + " " + realm.getIndex() + "层";
            MutableComponent name = Component.literal("玉简·" + realmName)
                    .withStyle(getRarityStyle(rarity));
            stack.set(DataComponents.CUSTOM_NAME, name);
        } else {
            MutableComponent name = Component.literal("空白玉简")
                    .withStyle(ChatFormatting.GRAY);
            stack.set(DataComponents.CUSTOM_NAME, name);
        }
    }

    private static ChatFormatting getRarityStyle(Rarity rarity) {
        return switch (rarity) {
            case COMMON -> ChatFormatting.WHITE;
            case UNCOMMON -> ChatFormatting.GREEN;
            case RARE -> ChatFormatting.BLUE;
            case EPIC -> ChatFormatting.LIGHT_PURPLE;
            case LEGENDARY -> ChatFormatting.GOLD;
        };
    }

    

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        Optional<ComponentRealm> realmOpt = getRealmData(stack);
        Rarity rarity = getRarity(stack);
        String source = getSource(stack);
        String creator = getCreator(stack);

        
        tooltip.add(Component.literal("稀有度: " + rarity.getId())
                .withStyle(getRarityStyle(rarity)));

        
        tooltip.add(Component.literal("来源: " + getSourceDisplayName(source))
                .withStyle(ChatFormatting.GRAY));

        if (!creator.isEmpty() && !creator.equals("world_gen")) {
            tooltip.add(Component.literal("击杀者: " + creator)
                    .withStyle(ChatFormatting.GOLD));
        }

        if (realmOpt.isPresent()) {
            ComponentRealm realm = realmOpt.get();
            tooltip.add(Component.literal("--- 境界信息 ---").withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.literal("境界: " + realm.getName())
                    .withStyle(ChatFormatting.WHITE));
            tooltip.add(Component.literal("大境界: " + realm.getIndex() + " (" + getRealmLevelName(realm.getIndex()) + ")")
                    .withStyle(ChatFormatting.WHITE));
            tooltip.add(Component.literal("小境界: " + realm.getIndex() + " / " + realm.size())
                    .withStyle(ChatFormatting.WHITE));

            
            tooltip.add(Component.literal("属性加成:").withStyle(ChatFormatting.AQUA));
            tooltip.add(Component.literal("  气血: +" + (int)realm.getAddHealth())
                    .withStyle(ChatFormatting.RED));
            tooltip.add(Component.literal("  父气: +" + (int)realm.getAddParrentEnergy())
                    .withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.literal("  子气: +" + (int)realm.getAddChildEnergy())
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.literal("  寿命: +" + (int)realm.getAddNormalLife())
                    .withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.literal("  元寿: +" + (int)realm.getAddSoulLife())
                    .withStyle(ChatFormatting.YELLOW));

            
            tooltip.add(Component.literal("突破奖励:").withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.literal("  气血: +" + (int)realm.getNextLevelAddHealth())
                    .withStyle(ChatFormatting.RED));
            tooltip.add(Component.literal("  父气: +" + (int)realm.getNextLevelAddParrentEnergy())
                    .withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.literal("  子气: +" + (int)realm.getNextLevelAddChildEnergy())
                    .withStyle(ChatFormatting.LIGHT_PURPLE));

            if (realm.canTrans()) {
                tooltip.add(Component.literal("可飞升/渡劫").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD));
            }
        } else {
            tooltip.add(Component.literal("右键使用可记录当前境界").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }

    private String getSourceDisplayName(String source) {
        return switch (source) {
            case "world_gen" -> "世界生成";
            case "loot" -> "战利品掉落";
            case "top_prodigy_kill" -> "击杀顶级天骄";
            case "crafting" -> "玩家制作";
            case "villager_trade" -> "村民交易";
            default -> source;
        };
    }

    private String getRealmLevelName(int level) {
        return switch (level) {
            case 0 -> "凡人";
            case 1 -> "炼气";
            case 2 -> "筑基";
            case 3 -> "金丹";
            case 4 -> "元婴";
            case 5 -> "化神";
            case 6 -> "合体";
            case 7 -> "大乘";
            case 8 -> "渡劫";
            case 9 -> "飞升";
            default -> "未知";
        };
    }

    

    private static ComponentRealm convertToComponentRealm(StatGenerator.RealmGeneratedStats stats) {
        ComponentRealm.Builder builder = com.mingsiamz.xuanlingmod.data.component.ComponentRealm.builder(stats.name())
                .level(stats.majorLevel())
                .size(stats.size())
                .addHealth(stats.baseStats().getOrDefault("health", 0.0))
                .addParrentEnergy(stats.baseStats().getOrDefault("parrent_energy", 0.0))
                .addChildEnergy(stats.baseStats().getOrDefault("child_energy", 0.0))
                .addNormalLife(stats.baseStats().getOrDefault("normal_life", 0.0))
                .addSoulLife(stats.baseStats().getOrDefault("soul_life", 0.0))
                .nextLevelAddHealth(stats.breakthroughBonus().getOrDefault("health", 0.0))
                .nextLevelAddParrentEnergy(stats.breakthroughBonus().getOrDefault("parrent_energy", 0.0))
                .nextLevelAddChildEnergy(stats.breakthroughBonus().getOrDefault("child_energy", 0.0))
                .nextLevelAddNormalLife(stats.breakthroughBonus().getOrDefault("normal_life", 0.0))
                .nextLevelAddSoulLife(stats.breakthroughBonus().getOrDefault("soul_life", 0.0));

        if (stats.canTranscend()) {
            builder.trans();
        }

        return builder.build();
    }
}
