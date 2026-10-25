package com.mingsiamz.xuanlingmod.network;

import java.util.UUID;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.effects.EffectMeditation;
import com.mingsiamz.xuanlingmod.event.NormalPlayerEvent;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterEffects;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MeditationPacket extends CustomPackageImp {

        public static final CustomPacketPayload.Type<MeditationPacket> TYPE = reType("meditation_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, MeditationPacket> CODEC = StreamCodec.ofMember(MeditationPacket::encode, MeditationPacket::new);

        public MeditationPacket() {
                
        }

        public MeditationPacket(RegistryFriendlyByteBuf buf) {
                
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        void serverRun(MinecraftServer server, LivingEntity entity) {
                super.serverRun(server, entity);
                if (entity == null)
                        return;
                BasicData basicData = RegisterAttachments.getBasicData(entity);
                if (basicData == null)
                        return;
                Vec3 pos = entity.position();
                XuanLing.LOGGER.info("not null");
                basicData.setMeditation(true);
                basicData.setLastPos(pos);
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
        }

}
