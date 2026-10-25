package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber (modid = XuanLing.MODID)
public class DataEvent {
        @SubscribeEvent 
        public static void onPlayerJoinGame(PlayerEvent.PlayerLoggedInEvent event) {
                Player player = event.getEntity();
                if (!player.hasData(RegisterAttachments.BASIC_DATA)) {
                        BasicData basicData = BasicData.initData();
                        player.setData(RegisterAttachments.BASIC_DATA, basicData);
                }
                if (!player.hasData(RegisterAttachments.DEFAULT_DATA)) {
                        player.setData(RegisterAttachments.DEFAULT_DATA, new DefaultData());
                }
                if (!player.hasData(RegisterAttachments.DEFAULT_ENERGY)) {
                        player.setData(RegisterAttachments.DEFAULT_ENERGY, new DefaultEnergy());
                }
        }

        @SubscribeEvent 
        public static void onEntityWasDeath(EntityTickEvent.Post event) {
                if (event.getEntity() instanceof LivingEntity entity) {
                        if (!entity.isAlive()) {
                                entity.getData(RegisterAttachments.DEFAULT_DATA);
                        }
                }
        }

        @SubscribeEvent
        public static void onEntityDeath(LivingDeathEvent event){
                LivingEntity entity = event.getEntity();
                // if ((entity instanceof Player player) && !player.isAlive()) {
                //         DefaultData data = DefaultData.init();
                //         BasicData basic = BasicData.initData();
                //         player.setData(RegisterAttachments.DEFAULT_DATA, data);
                //         player.setData(RegisterAttachments.BASIC_DATA, basic);
                // }
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {
                Player player = event.getEntity();
                
        }

        // @SubscribeEvent
        // public static void onEntityChangedDimesion() {
	
        // }

        // @SubscribeEvent 
        // public static void onEntityJoinGame() {
	
        // }
}
