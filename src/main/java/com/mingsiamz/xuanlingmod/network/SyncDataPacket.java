package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SyncDataPacket extends CustomPackageImp{

        public static final CustomPacketPayload.Type<SyncDataPacket> TYPE = reType("sync_data_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncDataPacket> CODEC = StreamCodec.ofMember(SyncDataPacket::encode, SyncDataPacket::new);

        public CompoundTag nbt = null; 

        public SyncDataPacket(CompoundTag nbt) {
                this.nbt = nbt;
        }

        public SyncDataPacket(RegistryFriendlyByteBuf buf) {
                this.nbt = buf.readNbt();
        }

        @Override
        void clientRun(Minecraft minecraft) {

        }

        @Override
        void serverRun(MinecraftServer server, LivingEntity entity) {

        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                if (this.nbt == null)
                        return;
                buf.writeNbt(this.nbt);
        }
    
}
