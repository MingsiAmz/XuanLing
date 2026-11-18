package com.mingsiamz.xuanlingmod.register;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.component.JadeSlipData;
import com.mingsiamz.xuanlingmod.data.component.TalismanPaperData;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegisterDataComponents {
        public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, XuanLing.MODID);

        public static final Supplier<DataComponentType<TalismanPaperData>> TALISMAN_PAPER_DATA = DATA_COMPONENTS.register(
                "talisman_paper_data",
                () -> DataComponentType.<TalismanPaperData>builder()
                                .persistent(TalismanPaperData.CODEC)
                                .networkSynchronized(TalismanPaperData.STREAM_CODEC)
                                .build()
        );

        public static final Supplier<DataComponentType<JadeSlipData>> JADE_SLIP_DATA = DATA_COMPONENTS.register(
                "jade_slip_data",
                () -> DataComponentType.<JadeSlipData>builder()
                                .persistent(JadeSlipData.CODEC)
                                .networkSynchronized(JadeSlipData.STREAM_CODEC)
                                .build()
        );

        public static void register(IEventBus modBus) {
                DATA_COMPONENTS.register(modBus);
        }
}