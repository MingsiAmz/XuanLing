package com.mingsiamz.xuanlingmod.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;


public class CastSpellPacket extends CustomPackageImp {

        public static final CustomPacketPayload.Type<CastSpellPacket> TYPE = reType("cast_spell_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, CastSpellPacket> CODEC = StreamCodec
                        .ofMember(CastSpellPacket::encode, CastSpellPacket::new);

        private String name;

        public CastSpellPacket() {
        }

        public CastSpellPacket(String name) {
                this.name = name;
        }

        public CastSpellPacket(RegistryFriendlyByteBuf buf) {
                this.name = buf.readUtf(128);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        void serverRun(MinecraftServer server, LivingEntity entity) {
                super.serverRun(server, entity);
                if (entity == null || this.name == null || this.name.isEmpty())
                        return;
                SkillCastPacket.cast(entity, this.name);
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                buf.writeUtf(this.name == null ? "" : this.name, 128);
        }

}
