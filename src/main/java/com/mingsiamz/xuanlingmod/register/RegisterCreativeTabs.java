package com.mingsiamz.xuanlingmod.register;

import java.util.Iterator;

import com.mingsiamz.xuanlingmod.XuanLing;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.network.chat.Component;

public class RegisterCreativeTabs{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, XuanLing.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS
            .register("xuanling_tabs", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.xuanling")) 
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> RegisterItems.EXAMPLE_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                                    Iterator<DeferredHolder<Item, ? extends Item>> it = RegisterItems.ITEMS.getEntries().iterator();
                                    while (it.hasNext())
                                            output.accept(it.next().get());
                    }).build());

    public static void register(IEventBus modBus) {
        CREATIVE_MODE_TABS.register(modBus);
    }
}
