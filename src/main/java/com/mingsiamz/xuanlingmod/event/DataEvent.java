package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.network.BindSkillPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.server.level.ServerPlayer;
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

        
        private static final String[] SYNC_KEYS = {
                        XuanLing.MODID + XuanLingTypeEnum.SPELL.type,
                        XuanLing.MODID + XuanLingTypeEnum.ART.type,
                        BindSkillPacket.BIND_KEY
        };

        
        private static void syncToClient(Player player) {
                if (!(player instanceof ServerPlayer serverPlayer))
                        return;
                for (String key : SYNC_KEYS)
                        Networking.syncPersistentData(serverPlayer, key);
        }

        @SubscribeEvent
        public static void onPlayerJoinGame(PlayerEvent.PlayerLoggedInEvent event) {
                Player player = event.getEntity();
                if (!player.hasData(RegisterAttachments.BASIC_DATA)) {
                        BasicData basicData = BasicData.initData();
                        basicData.setNormalLife(90);
                        basicData.setSoulLife(90);
                        player.setData(RegisterAttachments.BASIC_DATA, basicData);
                        
                }
                if (!player.hasData(RegisterAttachments.DEFAULT_DATA)) {
                        player.setData(RegisterAttachments.DEFAULT_DATA, new DefaultData());
                }
                if (!player.hasData(RegisterAttachments.DEFAULT_ENERGY)) {
                        player.setData(RegisterAttachments.DEFAULT_ENERGY, new DefaultEnergy());
                }

                syncToClient(player);
        }

        
        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
                syncToClient(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
                syncToClient(event.getEntity());
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
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {
        }
}

