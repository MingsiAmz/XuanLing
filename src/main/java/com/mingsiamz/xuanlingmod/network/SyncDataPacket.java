package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.client.ArtSpellCache;
import com.mingsiamz.xuanlingmod.gui.ArcaneMasteryMenuGui;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


public class SyncDataPacket extends CustomPackageImp {

        private static final int MAX_KEY_LENGTH = 128;

        public static final CustomPacketPayload.Type<SyncDataPacket> TYPE = reType("sync_data_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, SyncDataPacket> CODEC = StreamCodec
                        .ofMember(SyncDataPacket::encode, SyncDataPacket::new);

        private final String key;
        private final CompoundTag data;

        public SyncDataPacket(String key, CompoundTag data) {
                this.key = key;
                this.data = data == null ? new CompoundTag() : data;
        }

        public SyncDataPacket(RegistryFriendlyByteBuf buf) {
                this.key = buf.readUtf(MAX_KEY_LENGTH);
                CompoundTag read = buf.readNbt();
                this.data = read == null ? new CompoundTag() : read;
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        void clientRun(Minecraft minecraft) {
                super.clientRun(minecraft);
                if (minecraft.player == null || this.key == null || this.key.isEmpty())
                        return;
                minecraft.player.getPersistentData().put(this.key, this.data.copy());
                
                ArtSpellCache.updateCache(this.key, this.data);
                if (minecraft.screen instanceof ArcaneMasteryMenuGui menu) {
                        menu.refreshData();
                }
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                buf.writeUtf(this.key == null ? "" : this.key, MAX_KEY_LENGTH);
                buf.writeNbt(this.data.copy());
        }

}
