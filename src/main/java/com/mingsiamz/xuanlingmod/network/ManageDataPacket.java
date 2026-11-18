package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.controller.DataManager;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class ManageDataPacket extends CustomPackageImp {

    public static final String ACTIVE_ART_KEY = XuanLing.MODID + XuanLingTypeEnum.ART.type + "_active";
    public static final CustomPacketPayload.Type<ManageDataPacket> TYPE = reType("manage_data_packet");
    public static final StreamCodec<RegistryFriendlyByteBuf, ManageDataPacket> CODEC = StreamCodec
            .ofMember(ManageDataPacket::encode, ManageDataPacket::new);

    private String type = "";
    private String name = "";
    private boolean delete;

    public ManageDataPacket() {
    }

    public ManageDataPacket(XuanLingTypeEnum type, String name, boolean delete) {
        this.type = type == null ? "" : type.type;
        this.name = name == null ? "" : name;
        this.delete = delete;
    }

    public ManageDataPacket(RegistryFriendlyByteBuf buf) {
        this.type = buf.readUtf(32);
        this.name = buf.readUtf(128);
        this.delete = buf.readBoolean();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    void serverRun(MinecraftServer server, LivingEntity entity) {
        super.serverRun(server, entity);
        if (!(entity instanceof ServerPlayer player) || this.name.isEmpty()) {
            return;
        }
        XuanLingTypeEnum dataType = switch (this.type) {
            case "gong_fa" -> XuanLingTypeEnum.ART;
            case "shen_tong" -> XuanLingTypeEnum.SPELL;
            default -> null;
        };
        if (dataType == null) {
            return;
        }
        String dataKey = XuanLing.MODID + dataType.type;
        CompoundTag root = player.getPersistentData();
        CompoundTag learned = root.getCompound(dataKey);
        if (!this.name.equals(learned.getString(this.name))) {
            return;
        }
        if (this.delete) {
            learned.remove(this.name);
            root.put(dataKey, learned);
            DataManager.deleteComponent(dataType, player.getStringUUID(), this.name);
            if (dataType == XuanLingTypeEnum.SPELL) {
                CompoundTag binds = root.getCompound(BindSkillPacket.BIND_KEY);
                binds.remove(this.name);
                root.put(BindSkillPacket.BIND_KEY, binds);
                Networking.syncPersistentData(player, BindSkillPacket.BIND_KEY);
            } else if (this.name.equals(root.getCompound(ACTIVE_ART_KEY).getString("name"))) {
                root.put(ACTIVE_ART_KEY, new CompoundTag());
                Networking.syncPersistentData(player, ACTIVE_ART_KEY);
            }
            Networking.syncPersistentData(player, dataKey);
            return;
        }
        if (dataType == XuanLingTypeEnum.ART) {
            CompoundTag active = root.getCompound(ACTIVE_ART_KEY);
            CompoundTag updated = new CompoundTag();
            if (!this.name.equals(active.getString("name"))) {
                updated.putString("name", this.name);
            }
            root.put(ACTIVE_ART_KEY, updated);
            Networking.syncPersistentData(player, ACTIVE_ART_KEY);
        }
    }

    @Override
    void encode(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(this.type, 32);
        buf.writeUtf(this.name, 128);
        buf.writeBoolean(this.delete);
    }
}
