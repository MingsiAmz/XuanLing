package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class BindSkillPacket extends CustomPackageImp {

        public static final String BIND_KEY = XuanLing.MODID + XuanLingTypeEnum.SPELL.type + "_bind";

        public static final String ALLOWED_KEYS = "RGCVB";
        public static final int MAX_COMBO = 4;

        public static final CustomPacketPayload.Type<BindSkillPacket> TYPE = reType("bind_skill_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, BindSkillPacket> CODEC = StreamCodec
                        .ofMember(BindSkillPacket::encode, BindSkillPacket::new);

        private String name;
        private String combo;

        public BindSkillPacket() {
        }

        public BindSkillPacket(String name, String combo) {
                this.name = name;
                this.combo = sanitize(combo);
        }

        public BindSkillPacket(RegistryFriendlyByteBuf buf) {
                this.name = buf.readUtf(128);
                this.combo = buf.readUtf(16);
        }

        public static String sanitize(String combo) {
                if (combo == null)
                        return "";
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < combo.length() && sb.length() < MAX_COMBO; i++) {
                        char c = Character.toUpperCase(combo.charAt(i));
                        if (ALLOWED_KEYS.indexOf(c) >= 0)
                                sb.append(c);
                }
                return sb.toString();
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        void serverRun(MinecraftServer server, LivingEntity entity) {
                super.serverRun(server, entity);
                if (!(entity instanceof ServerPlayer player))
                        return;
                if (this.name == null || this.name.isEmpty())
                        return;

                CompoundTag root = player.getPersistentData();
                if (!root.contains(BIND_KEY, Tag.TAG_COMPOUND))
                        root.put(BIND_KEY, new CompoundTag());
                CompoundTag binds = root.getCompound(BIND_KEY);

                if (this.combo.isEmpty())
                        binds.remove(this.name);
                else
                        binds.putString(this.name, this.combo);

                Networking.syncPersistentData(player, BIND_KEY);
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                buf.writeUtf(this.name == null ? "" : this.name, 128);
                buf.writeUtf(this.combo == null ? "" : this.combo, 16);
        }

}
