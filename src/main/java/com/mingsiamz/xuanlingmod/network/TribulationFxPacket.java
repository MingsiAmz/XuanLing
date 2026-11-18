package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.client.TribulationFx;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

public class TribulationFxPacket extends CustomPackageImp {

    public static final CustomPacketPayload.Type<TribulationFxPacket> TYPE = reType("tribulation_fx_packet");
    public static final StreamCodec<RegistryFriendlyByteBuf, TribulationFxPacket> CODEC = StreamCodec
            .ofMember(TribulationFxPacket::encode, TribulationFxPacket::new);

    private int entityId;

    public TribulationFxPacket() {
        this(0);
    }

    public TribulationFxPacket(int entityId) {
        this.entityId = entityId;
    }

    public TribulationFxPacket(RegistryFriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    void clientRun(Minecraft minecraft) {
        minecraft.execute(() -> TribulationFx.playLightning(minecraft, this.entityId));
    }

    @Override
    void encode(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(this.entityId);
    }

    public static void send(Entity entity) {
        if (!entity.level().isClientSide) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new TribulationFxPacket(entity.getId()));
        }
    }

}
