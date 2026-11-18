package com.mingsiamz.xuanlingmod.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.ArtComponent;
import com.mingsiamz.xuanlingmod.data.ComponentCodec;
import com.mingsiamz.xuanlingmod.data.ComponentGroup;
import com.mingsiamz.xuanlingmod.data.SpellComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class CreateDataPacket extends CustomPackageImp {

        public static final int MAX_NAME_LENGTH = 64;
        private static final int MAX_TAG_LENGTH = 128;
        public static final int MAX_PAYLOAD_LENGTH = 65536;

        public static boolean isValidName(String name) {
                if (name == null || name.isEmpty() || name.length() > MAX_NAME_LENGTH)
                        return false;
                for (int i = 0; i < name.length(); i++) {
                        char c = name.charAt(i);
                        if (c == '/' || c == '\\' || c == ':' || c == '*' || c == '?' || c == '"' || c == '<'
                                        || c == '>' || c == '|')
                                return false;
                }
                return true;
        }

        public static final CustomPacketPayload.Type<CreateDataPacket> TYPE = reType("create_data_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, CreateDataPacket> CODEC = StreamCodec
                        .ofMember(CreateDataPacket::encode, CreateDataPacket::new);

        private final XuanLingTypeEnum type;
        private final String name;
        private final String payload;

        public CreateDataPacket(XuanLingTypeEnum type, String name, String payload) {
                this.type = type;
                this.name = name;
                this.payload = payload == null ? "" : payload;
        }

        public CreateDataPacket(RegistryFriendlyByteBuf buf) {
                this.type = typeOf(buf.readUtf(MAX_TAG_LENGTH));
                this.name = buf.readUtf(MAX_NAME_LENGTH);
                this.payload = buf.readUtf(MAX_PAYLOAD_LENGTH);
        }

        private static XuanLingTypeEnum typeOf(String tag) {
                for (XuanLingTypeEnum e : XuanLingTypeEnum.values())
                        if (e.type.equals(tag))
                                return e;
                return null;
        }

        private String key() {
                return XuanLing.MODID + this.type.type;
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf) {
                buf.writeUtf(this.type == null ? "" : this.type.type, MAX_TAG_LENGTH);
                buf.writeUtf(this.name == null ? "" : this.name, MAX_NAME_LENGTH);
                buf.writeUtf(this.payload, MAX_PAYLOAD_LENGTH);
        }

        @Override
        void serverRun(MinecraftServer server, LivingEntity entity) {
                super.serverRun(server, entity);
                if (!(entity instanceof ServerPlayer player))
                        return;
                if (this.type == null || !isValidName(this.name))
                        return;

                if (this.payload.isEmpty())
                        return;

                ComponentGroup group = decodePayload();
                if (group == null || group.isEmpty())
                        return;

                boolean saved = switch (this.type) {
                        case SPELL -> new SpellComponent(this.name, group).toJson(player);
                        case ART -> new ArtComponent(this.name, group).toJson(player);
                        default -> false;
                };
                if (!saved)
                        return;

                CompoundTag root = player.getPersistentData();
                if (!root.contains(key(), Tag.TAG_COMPOUND))
                        root.put(key(), new CompoundTag());
                root.getCompound(key()).putString(this.name, this.name);

                Networking.syncPersistentData(player, key());
        }

        private ComponentGroup decodePayload() {
                try {
                        JsonObject root = JsonParser.parseString(this.payload).getAsJsonObject();
                        return ComponentCodec.decodeGroup(this.type, root);
                } catch (JsonParseException | IllegalStateException e) {
                        XuanLing.LOGGER.warn("CreateDataPacket: bad payload for '{}'", this.name, e);
                        return null;
                }
        }

}
