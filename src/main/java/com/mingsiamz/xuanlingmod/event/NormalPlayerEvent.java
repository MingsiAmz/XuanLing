package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy.Type;
import com.mingsiamz.xuanlingmod.gui.ChangeGui;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = XuanLing.MODID)
public class NormalPlayerEvent {
        @SubscribeEvent
        public static void playerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {

        }

        @SubscribeEvent
        public static void playerIsDeath(EntityTickEvent.Post event) {

        }

        @SubscribeEvent 
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
                // for (Field fields : EntityType.class.getFields()) {
                //     // event.registerEntity(Capabilities.EnergyStorage.ENTITY, fields, (entity, ctx) -> entity.);
                // }
        }

        @SubscribeEvent 
        public static void onRightClickItem(RightClickItem event) {
                Player player = event.getEntity();
                Level level = player.level();
                InteractionHand hand = event.getHand();
                DefaultEnergy energy = RegisterAttachments.getDefaultEnergy(player);
                ItemStack item = player.getItemInHand(hand);
                if (level.isClientSide) {
                        
                } else {
                        pushEnergyItem(item, energy);
                }
        }

        @SubscribeEvent
        public static void onRightClickBlock(RightClickBlock event) {
                Level level = event.getLevel();
                Player player = event.getEntity();
                BlockPos blockPos = event.getPos();
                useBodyStale(level, blockPos);
        }

        // @SubscribeEvent 
        // public static void test(RightClickEmpty event) {
        //         Player player = event.getEntity();
        //         Level level = player.level();
        //         DefaultEnergy energy = RegisterAttachments.getDefaultEnergy(player);
        // }

        private static void useBodyStale(Level level, BlockPos pos) {
                BlockState state = level.getBlockState(pos);
                if (state.is(RegisterBlocks.BODY_EX_STALE_BLOCK)) {
                        if (level.isClientSide)
                                Minecraft.getInstance().setScreen(ChangeGui.current());
                }
        }

        private static void pushEnergyItem(ItemStack item, DefaultEnergy energy) {
                if ((item.getItem() instanceof IEnergyStorage iitem) && energy != null) {
                        if (!iitem.canReceive()) return; 
                        int sum = iitem.getEnergyStored() + (int)energy.getNowValue(Type.PARRENT);
                        int toExtract = sum >= iitem.getMaxEnergyStored() ? iitem.getMaxEnergyStored() : sum;
                        iitem.extractEnergy(toExtract, false);
                }
        }
}
