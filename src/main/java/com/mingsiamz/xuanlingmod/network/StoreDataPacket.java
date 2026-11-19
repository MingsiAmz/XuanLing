package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;

public class StoreDataPacket extends CustomPackageImp {

    public static final CustomPacketPayload.Type<StoreDataPacket> TYPE = reType("store_data_packet");
    public static final StreamCodec<RegistryFriendlyByteBuf, StoreDataPacket> CODEC = StreamCodec
            .ofMember(StoreDataPacket::encode, StoreDataPacket::new);

    private String type = "";
    private String name = "";

    public StoreDataPacket() {
    }

    public StoreDataPacket(XuanLingTypeEnum type, String name) {
        this.type = type == null ? "" : type.type;
        this.name = name == null ? "" : name;
    }

    public StoreDataPacket(RegistryFriendlyByteBuf buf) {
        this.type = buf.readUtf(32);
        this.name = buf.readUtf(128);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    void serverRun(MinecraftServer server, LivingEntity entity) {
        super.serverRun(server, entity);
        if (!(entity instanceof net.minecraft.server.level.ServerPlayer player))
            return;
        if (this.type.isEmpty() || this.name.isEmpty())
            return;

        XuanLingTypeEnum dataType = switch (this.type) {
            case "gong_fa" -> XuanLingTypeEnum.ART;
            case "shen_tong" -> XuanLingTypeEnum.SPELL;
            default -> null;
        };
        if (dataType == null)
            return;

        com.mingsiamz.xuanlingmod.data.ComponentGroup group = com.mingsiamz.xuanlingmod.data.ComponentCodec
                        .loadGroup(dataType, player.getStringUUID(), this.name);
        if (group == null || group.isEmpty())
            return;

        net.minecraft.world.item.ItemStack held = player.getMainHandItem();
        if (held.getItem() != com.mingsiamz.xuanlingmod.register.RegisterItems.JADE_SLIP.get())
            return;

        String payload = com.mingsiamz.xuanlingmod.data.ComponentCodec.encodeGroup(group).toString();
        com.mingsiamz.xuanlingmod.items.JadeSlipItem.setStoredData(held, dataType.type, payload);
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, held);

        com.mingsiamz.xuanlingmod.XuanLing.LOGGER.info("{} 将 {} 存入玉简", player.getName().getString(), this.name);
    }

    @Override
    void encode(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(this.type, 32);
        buf.writeUtf(this.name, 128);
    }
}
