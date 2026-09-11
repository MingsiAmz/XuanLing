package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

@EventBusSubscriber(modid = XuanLing.MODID)
public class NormalPlayerEvent {
    @SubscribeEvent
    public static void playerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
	Player player = event.getEntity();
	BasicData data = player.getData(RegisterAttachments.BASIC_DATA);
	if (data == null) {
	    BasicData targetData = BasicData.initData();
	    player.setData(RegisterAttachments.BASIC_DATA, targetData);
	}
    }

    @SubscribeEvent
    public static void playerIsDeath(EntityTickEvent.Post event) {
	Entity entity = event.getEntity();
	if (!(entity instanceof LivingEntity)) return;
	LivingEntity living = (LivingEntity) entity;

    }
}
