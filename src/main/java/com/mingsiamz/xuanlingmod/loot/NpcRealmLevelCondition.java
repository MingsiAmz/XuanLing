package com.mingsiamz.xuanlingmod.loot;

import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import com.mingsiamz.xuanlingmod.event.AutoGenerationEvents;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class NpcRealmLevelCondition implements LootItemCondition {

    public static final MapCodec<NpcRealmLevelCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.optionalFieldOf("min_level", 0).forGetter(condition -> condition.minLevel),
                    Codec.INT.optionalFieldOf("max_level", Integer.MAX_VALUE)
                            .forGetter(condition -> condition.maxLevel))
            .apply(instance, NpcRealmLevelCondition::new));

    private final int minLevel;
    private final int maxLevel;

    public NpcRealmLevelCondition(int minLevel, int maxLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    @Override
    public LootItemConditionType getType() {
        return RegisterLootConditions.NPC_REALM_LEVEL.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return Set.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity == null) {
            return false;
        }
        int realmLevel = AutoGenerationEvents.getNPCRealmLevel(entity);
        return realmLevel >= minLevel && realmLevel <= maxLevel;
    }
}
