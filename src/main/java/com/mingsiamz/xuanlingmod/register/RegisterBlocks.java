package com.mingsiamz.xuanlingmod.register;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.blocks.BodyExStaleBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterBlocks {
        public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(XuanLing.MODID);

        public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block",
                        BlockBehaviour.Properties.of().mapColor(MapColor.STONE));

        public static final DeferredBlock<BodyExStaleBlock> BODY_EX_STALE_BLOCK = BLOCKS.register(
                        "body_examination_stale", () -> new BodyExStaleBlock(BlockBehaviour.Properties.of()));

        public static void register(IEventBus modBus) {
                BLOCKS.register(modBus);
        }
}
