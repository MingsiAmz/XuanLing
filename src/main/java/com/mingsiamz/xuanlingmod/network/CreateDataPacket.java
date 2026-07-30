package com.mingsiamz.xuanlingmod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;

public class CreateDataPacket extends CustomPackageImp {

    public CreateDataPacket(RegistryFriendlyByteBuf buf) {
	
    }

    public static final CustomPacketPayload.Type<CreateDataPacket> TYPE = CustomPacketPayload.createType("create_data_packet_type");
    public static final StreamCodec<RegistryFriendlyByteBuf,CreateDataPacket> CODEC = StreamCodec.ofMember(CreateDataPacket::encode, CreateDataPacket::new);

    private String dataType = "";
    private String jsonPayload = "";

    @Override
    public Type<? extends CustomPacketPayload> type() {
	return TYPE;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
	
    }

    @Override
    void serverRun(MinecraftServer server, LivingEntity entity) {
	super.serverRun(server, entity);
    }

}
