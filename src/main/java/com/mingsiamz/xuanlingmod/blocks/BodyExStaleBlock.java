package com.mingsiamz.xuanlingmod.blocks;

import org.joml.Vector3f;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BodyExStaleBlock extends Block {

        public enum Part implements StringRepresentable {
                MAIN("main"),
                CHID("chid");

                private final String name;

                private Part(final String name) {
                        this.name = name;
                }

                @Override
                public String getSerializedName() {
                        return this.name;
                }

        }

        private int ox = 0;
        private int oy = 0;
        private int oz = 0;
        private int dx = 0;
        private int dy = 1;
        private int dz = 0;

        public static final EnumProperty<Part> PART = EnumProperty.create("part", Part.class);

        public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

        private static final VoxelShape SHAPE = Shapes.box(0, 0, 0, 1, 1, 1);

        public BodyExStaleBlock(final Properties properties) {
                super(properties.strength(2f).lightLevel(state -> 15).noOcclusion());
                this.registerDefaultState(this.stateDefinition.any()
                                          .setValue(PART, Part.MAIN)
                                          .setValue(FACING, Direction.NORTH));
        }

        @Override
        protected MapCodec<? extends Block> codec() {
                return simpleCodec(BodyExStaleBlock::new);
        }

        @Override
        protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
                builder.add(PART, FACING);
        }

        @Override
        public BlockState getStateForPlacement(BlockPlaceContext context) {
                Level level = context.getLevel();
                BlockPos pos = context.getClickedPos();
                BlockPos start = pos.offset(ox, oy, oz);
                BlockPos end = pos.offset(dx, dy, dz);
                Iterable<BlockPos> poss = BlockPos.betweenClosed(ox, -1, oz, dx, 1, dz);
                for (BlockPos trPos : poss) {
                        if (!level.getBlockState(trPos).canBeReplaced() && level.getBlockState(trPos).isAir()) {
                                return null;
                        }
                }
                return this.defaultBlockState()
                        .setValue(PART, Part.MAIN)
                        .setValue(FACING, context.getHorizontalDirection().getOpposite());
        }

        @Override
        protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState,
                               boolean movedByPiston) {
                super.onPlace(state, level, pos, oldState, movedByPiston);
                if (state.getValue(PART) != Part.MAIN)
                        return;
                if (oldState.is(this))
                        return;

                BlockPos mainPos = pos.immutable();
                Direction facing = state.getValue(FACING);
                BlockPos start = pos.offset(ox, oy, oz);
                BlockPos end = pos.offset(dx, dy, dz);
                Iterable<BlockPos> poss = BlockPos.betweenClosed(start, end);
                for (BlockPos trPos : poss) {
                        if (trPos.equals(mainPos))
                                continue;
                        if (!level.getBlockState(trPos).canBeReplaced())
                                return;
                        int x = trPos.getX() - mainPos.getX();
                        int y = trPos.getY() - mainPos.getY();
                        int z = trPos.getZ() - mainPos.getZ();
                        BlockState childState;
                        if (y > 0 && x == 0 && z == 0) {
                                childState = this.defaultBlockState()
                                        .setValue(PART, Part.CHID)
                                        .setValue(FACING, facing);
                                level.setBlock(trPos, childState, UPDATE_ALL);
                        } else if (y <= 0) {
                                childState = this.defaultBlockState()
                                        .setValue(PART, Part.CHID)
                                        .setValue(FACING, facing);
                                level.setBlock(trPos, childState, UPDATE_ALL);
                        }
                }
        }

        @Override
        protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

                super.tick(state, level, pos, random);
        }

        @Override
        public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
                destroyChild(level, state, player, pos);
                destroyMain(level, state, player, pos);
                return super.playerWillDestroy(level, pos, state, player);
        }

        private void destroyChild(Level level, BlockState state, Player player, BlockPos pos) {
                if (!level.isClientSide && state.is(this) && state.getValue(PART) == Part.CHID) {
                        BlockPos mainPos = findMainPos(level, pos);
                        BlockState mainState = level.getBlockState(mainPos);
                        if (mainPos != null) {
                                destroyMain(level, mainState, player, mainPos);
                        }
                }

        }

        private void destroyMain(Level level, BlockState state, Player player, BlockPos pos) {
                if (!level.isClientSide && state.getValue(PART) == Part.MAIN) {
                        BlockPos mainPos = pos.immutable();
                        BlockPos start = pos.offset(ox, oy, oz);
                        BlockPos end = pos.offset(dx, dy, dz);
                        Iterable<BlockPos> poss = BlockPos.betweenClosed(start, end);
                        for (BlockPos trPos : poss) {
                                if (trPos.equals(mainPos)) {
                                        level.destroyBlock(trPos, true, player);
                                        continue;
                                }
                                if (level.getBlockState(trPos).is(this))
                                        level.destroyBlock(trPos, false, player);
                        }
                }
        }

        private BlockPos findMainPos(Level level, BlockPos pos) {
                BlockPos start = pos.offset(ox, -1, oz);
                BlockPos end = pos.offset(dx, dy, dz);
                Iterable<BlockPos> poss = BlockPos.betweenClosed(start, end);
                for (BlockPos trPos : poss) {
                        if (level.getBlockState(trPos).is(this) && level.getBlockState(trPos).getValue(PART) == Part.MAIN) {
                                return trPos.immutable();
                        }
                }
                return null;
        }

        @Override
        protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
                return SHAPE;
        }

        @Override
        protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
                                               CollisionContext context) {
                return SHAPE;
        }

        @Override
        protected RenderShape getRenderShape(BlockState state) {
                if (state.getValue(PART) == Part.MAIN)
                        return RenderShape.MODEL;
                return RenderShape.INVISIBLE;
        }

        @Override
        protected int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
                if (state.getValue(PART) == Part.CHID)
                        return 0;
                return super.getLightBlock(state, level, pos);
        }

        @Override
        protected boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
                if (state.getValue(PART) == Part.CHID)
                        return true;
                return super.propagatesSkylightDown(state, level, pos);
        }

        @Override
        protected boolean useShapeForLightOcclusion(BlockState state) {
                return state.getValue(PART) == Part.MAIN;
        }

        @Override
        protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
                if (state.getValue(PART) == Part.CHID)
                        return 0;
                return super.getDestroyProgress(state, player, level, pos);
        }
        
}
