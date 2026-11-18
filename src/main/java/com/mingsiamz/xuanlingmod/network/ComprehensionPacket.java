package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;

public class ComprehensionPacket extends CustomPackageImp {

        public static final CustomPacketPayload.Type<ComprehensionPacket> TYPE = reType("comprehension_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, ComprehensionPacket> CODEC = StreamCodec.ofMember(ComprehensionPacket::encode, ComprehensionPacket::new);

        private boolean start;
        private String target;

        public ComprehensionPacket() {

        }

        public ComprehensionPacket(RegistryFriendlyByteBuf buf) {
                this.start = buf.readBoolean();
                this.target = buf.readUtf(128);
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
                if (start) {
                        basicData.setComprehending(true);
                        basicData.setComprehensionTick(400);
                        basicData.setComprehensionTarget(target);
                } else {
                        basicData.setComprehending(false);
                        basicData.setComprehensionTick(0);
                        basicData.setComprehensionTarget("");
                }
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                buf.writeBoolean(start);
                buf.writeUtf(target, 128);
        }

        public void setStart(boolean start) {
                this.start = start;
        }

        public void setTarget(String target) {
                this.target = target;
        }

}
