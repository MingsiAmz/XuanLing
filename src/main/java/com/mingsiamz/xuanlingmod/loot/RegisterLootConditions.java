package com.mingsiamz.xuanlingmod.loot;

import java.util.function.Supplier;

import com.mingsiamz.xuanlingmod.XuanLing;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterLootConditions {

    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister
            .create(BuiltInRegistries.LOOT_CONDITION_TYPE, XuanLing.MODID);

    public static final Supplier<LootItemConditionType> NPC_REALM_LEVEL = LOOT_CONDITIONS.register("npc_realm_level",
            () -> new LootItemConditionType(NpcRealmLevelCondition.CODEC));

    public static final Supplier<LootItemConditionType> NPC_TOP_PRODIGY = LOOT_CONDITIONS.register("npc_top_prodigy",
            () -> new LootItemConditionType(NpcTopProdigyCondition.CODEC));

    public static void register(IEventBus modBus) {
        LOOT_CONDITIONS.register(modBus);
    }
}
