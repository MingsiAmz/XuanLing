package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.entities.EjectEntity;
import com.mingsiamz.xuanlingmod.entities.SkillEntity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

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
                if (level.isClientSide) return;
                if ((item.getItem() instanceof IEnergyStorage iitem) && energy != null) {
                        if (!iitem.canReceive()) return; 
                        int sum = iitem.getEnergyStored() + energy.getEnergyStored();
                        int toExtract = sum >= iitem.getMaxEnergyStored() ? iitem.getMaxEnergyStored() : sum;
                        iitem.extractEnergy(toExtract, false);
                }
                if (item.getItem() == Items.DIAMOND) {
                        EjectEntity entity = new EjectEntity(level, player, 0);
                        level.addFreshEntity(entity);
                }
        }

        @SubscribeEvent 
        public static void test(RightClickEmpty event) {
                Player player = event.getEntity();
                Level level = player.level();
                DefaultEnergy energy = RegisterAttachments.getDefaultEnergy(player);
        }
}
