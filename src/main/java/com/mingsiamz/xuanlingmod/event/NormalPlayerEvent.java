package com.mingsiamz.xuanlingmod.event;

import java.lang.reflect.Field;

import java.lang.reflect.Field;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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

}
