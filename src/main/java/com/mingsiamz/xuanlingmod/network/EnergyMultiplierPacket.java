package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class EnergyMultiplierPacket extends CustomPackageImp {

        public static final CustomPacketPayload.Type<EnergyMultiplierPacket> TYPE = reType("energy_multiplier_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, EnergyMultiplierPacket> CODEC = StreamCodec
                        .ofMember(EnergyMultiplierPacket::encode, EnergyMultiplierPacket::new);

        private final float multiplier;

        public EnergyMultiplierPacket(float multiplier) {
                this.multiplier = multiplier;
        }

        public EnergyMultiplierPacket(RegistryFriendlyByteBuf buf) {
                this.multiplier = buf.readFloat();
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        void clientRun(Minecraft minecraft) {
                if (minecraft.player != null) {
                        BasicData data = RegisterAttachments.getBasicData(minecraft.player);
                        if (data != null) {
                                data.setEnergyMultiplier(multiplier);
                        }
                }
        }

        @Override
        void serverRun(MinecraftServer server, LivingEntity entity) {
                if (!(entity instanceof ServerPlayer player))
                        return;
                BasicData data = RegisterAttachments.getBasicData(player);
                if (data == null)
                        return;
                data.setEnergyMultiplier(multiplier);
                // 重新附加触发附件同步，将倍率同步至客户端
                player.setData(RegisterAttachments.BASIC_DATA, BasicData.copy(data));
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                buf.writeFloat(multiplier);
        }

}
