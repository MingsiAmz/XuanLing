package com.mingsiamz.xuanlingmod.loot;

import java.util.Set;

import com.mojang.serialization.MapCodec;

import com.mingsiamz.xuanlingmod.event.AutoGenerationEvents;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class NpcTopProdigyCondition implements LootItemCondition {

    public static final MapCodec<NpcTopProdigyCondition> CODEC = MapCodec.unit(NpcTopProdigyCondition::new);

    @Override
    public LootItemConditionType getType() {
        return RegisterLootConditions.NPC_TOP_PRODIGY.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        return entity != null && AutoGenerationEvents.isNPCTopProdigy(entity);
    }
}
