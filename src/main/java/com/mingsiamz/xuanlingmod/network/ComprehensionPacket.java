package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.Config;
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
        private float intensity;

        public ComprehensionPacket() {

        }

        public ComprehensionPacket(boolean start, String target, float intensity) {
                this.start = start;
                this.target = target;
                this.intensity = intensity;
        }

        public ComprehensionPacket(RegistryFriendlyByteBuf buf) {
                this.start = buf.readBoolean();
                this.target = buf.readUtf(128);
                this.intensity = buf.readFloat();
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
                        int duration = Config.getComprehensionDurationTick();
                        basicData.setComprehending(true);
                        basicData.setComprehensionTick(duration);
                        basicData.setComprehensionMaxTick(duration);
                        basicData.setComprehensionTarget(target);
                        basicData.setComprehensionIntensity(intensity);
                } else {
                        basicData.setComprehending(false);
                        basicData.setComprehensionTick(0);
                        basicData.setComprehensionTarget("");
                        basicData.setComprehensionIntensity(0f);
                        basicData.setComprehensionMaxTick(0);
                }
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                buf.writeBoolean(start);
                buf.writeUtf(target, 128);
                buf.writeFloat(intensity);
        }

        public void setStart(boolean start) {
                this.start = start;
        }

        public void setTarget(String target) {
                this.target = target;
        }

        public void setIntensity(float intensity) {
                this.intensity = intensity;
        }

}
