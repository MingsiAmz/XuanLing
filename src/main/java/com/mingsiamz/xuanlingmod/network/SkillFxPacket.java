package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.client.SkillFx;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public class SkillFxPacket extends CustomPackageImp {

    public static final CustomPacketPayload.Type<SkillFxPacket> TYPE = reType("skill_fx_packet");
    public static final StreamCodec<RegistryFriendlyByteBuf, SkillFxPacket> CODEC = StreamCodec
            .ofMember(SkillFxPacket::encode, SkillFxPacket::new);

    private int entityId;
    private boolean trail;

    public SkillFxPacket() {
        this(0, false);
    }

    public SkillFxPacket(int entityId) {
        this(entityId, false);
    }

    public SkillFxPacket(int entityId, boolean trail) {
        this.entityId = entityId;
        this.trail = trail;
    }

    public SkillFxPacket(RegistryFriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.trail = buf.readBoolean();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    void clientRun(Minecraft minecraft) {
        minecraft.execute(() -> SkillFx.play(minecraft, this.entityId, this.trail));
    }

    @Override
    void encode(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.entityId);
        buf.writeBoolean(this.trail);
    }

    public static void broadcast(Entity entity) {
        broadcast(entity, false);
    }

    public static void broadcast(Entity entity, boolean trail) {
        if (!entity.level().isClientSide) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new SkillFxPacket(entity.getId(), trail));
        }
    }
}
