package com.mingsiamz.xuanlingmod.register;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.items.TalismanPaperItem;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterItems {
        public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(XuanLing.MODID);

        public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", RegisterBlocks.EXAMPLE_BLOCK);

        public static final DeferredItem<Item> TALISMAN_PAPER = ITEMS.registerItem("talisman_paper", TalismanPaperItem::new);

        public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
                                                                                                                                  .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

        public static final DeferredItem<BlockItem> BODY_EX_STALE_ITEM = ITEMS.registerSimpleBlockItem("body_ex_stale_item", RegisterBlocks.BODY_EX_STALE_BLOCK);

        public static void register(IEventBus modBus) {
                ITEMS.register(modBus);
        }
}
