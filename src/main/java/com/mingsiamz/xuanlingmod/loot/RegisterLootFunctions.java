package com.mingsiamz.xuanlingmod.loot;

import java.util.function.Supplier;

import com.mingsiamz.xuanlingmod.XuanLing;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterLootFunctions {

    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTIONS = DeferredRegister
            .create(BuiltInRegistries.LOOT_FUNCTION_TYPE, XuanLing.MODID);

    public static final Supplier<LootItemFunctionType<?>> CREATE_JADE_SLIP = LOOT_FUNCTIONS.register("create_jade_slip",
            () -> new LootItemFunctionType<>(CreateJadeSlipFunction.CODEC));

    public static void register(IEventBus modBus) {
        LOOT_FUNCTIONS.register(modBus);
    }
}
