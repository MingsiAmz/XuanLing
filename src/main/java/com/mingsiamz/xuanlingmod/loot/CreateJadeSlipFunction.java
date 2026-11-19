package com.mingsiamz.xuanlingmod.loot;

import java.util.List;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.mingsiamz.xuanlingmod.auto.config.NPCTemplate;
import com.mingsiamz.xuanlingmod.auto.config.Rarity;
import com.mingsiamz.xuanlingmod.auto.generator.GenerationEngine;
import com.mingsiamz.xuanlingmod.auto.generator.StatGenerator;
import com.mingsiamz.xuanlingmod.data.component.ComponentRealm;
import com.mingsiamz.xuanlingmod.data.component.JadeSlipData;
import com.mingsiamz.xuanlingmod.event.AutoGenerationEvents;
import com.mingsiamz.xuanlingmod.items.JadeSlipItem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class CreateJadeSlipFunction extends LootItemConditionalFunction {

    public static final MapCodec<CreateJadeSlipFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance)
            .and(Codec.STRING.optionalFieldOf("rarity", Rarity.COMMON.getId()).forGetter(function -> function.rarity))
            .and(Codec.INT.optionalFieldOf("realm_level", -1).forGetter(function -> function.realmLevel))
            .and(Codec.STRING.optionalFieldOf("system", "").forGetter(function -> function.system))
            .and(Codec.STRING.optionalFieldOf("source", "loot").forGetter(function -> function.source))
            .apply(instance, CreateJadeSlipFunction::new));

    private final String rarity;
    private final int realmLevel;
    private final String system;
    private final String source;

    public CreateJadeSlipFunction(List<LootItemCondition> conditions, String rarity, int realmLevel, String system,
            String source) {
        super(conditions);
        this.rarity = rarity;
        this.realmLevel = realmLevel;
        this.system = system;
        this.source = source;
    }

    @Override
    @SuppressWarnings("unchecked")
    public LootItemFunctionType<CreateJadeSlipFunction> getType() {
        return (LootItemFunctionType<CreateJadeSlipFunction>) RegisterLootFunctions.CREATE_JADE_SLIP.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.THIS_ENTITY, LootContextParams.LAST_DAMAGE_PLAYER);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        int level = realmLevel >= 0 ? realmLevel : resolveRealmLevel(entity);
        String slipSystem = system.isEmpty() ? resolveSystem(entity) : system;
        Rarity slipRarity = resolveRarity(rarity);
        ComponentRealm realm = resolveCustomRealm(entity);
        if (realm == null) {
            realm = JadeSlipItem.resolveStandardRealm(slipRarity, level);
        }
        if (realm == null) {
            return stack;
        }
        return JadeSlipItem.createWithSystem(realm, slipRarity, source, resolveCreator(context), slipSystem);
    }

    private static int resolveRealmLevel(Entity entity) {
        return entity != null ? AutoGenerationEvents.getNPCRealmLevel(entity) : 0;
    }

    private static String resolveSystem(Entity entity) {
        return entity != null ? AutoGenerationEvents.getNPCSystem(entity) : JadeSlipData.DEFAULT_SYSTEM;
    }

    private static Rarity resolveRarity(String rarityId) {
        try {
            return Rarity.valueOf(rarityId.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Rarity.COMMON;
        }
    }

    private static String resolveCreator(LootContext context) {
        Player killer = context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
        return killer != null ? killer.getName().getString() : "unknown";
    }

    private static ComponentRealm resolveCustomRealm(Entity entity) {
        if (entity == null || !AutoGenerationEvents.isNPCTopProdigy(entity)) {
            return null;
        }
        String templateId = AutoGenerationEvents.getNPCTemplateId(entity);
        if (templateId.isEmpty()) {
            return null;
        }
        GenerationEngine engine = GenerationEngine.getInstance();
        NPCTemplate template = engine.getRegistry().getNPC(templateId);
        if (template == null) {
            return null;
        }
        StatGenerator.RealmGeneratedStats stats = engine.generateCustomRealm(template);
        return stats != null ? JadeSlipItem.convertToComponentRealm(stats) : null;
    }
}
