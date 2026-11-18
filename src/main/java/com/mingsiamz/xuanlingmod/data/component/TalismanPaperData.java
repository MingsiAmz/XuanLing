package com.mingsiamz.xuanlingmod.data.component;

import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record TalismanPaperData(
                String type,
                String nickname,
                String description,
                double speed) {

        public static final int MAX_NICKNAME_LENGTH = 64;
        public static final int MAX_DESCRIPTION_LENGTH = 1024;

        public TalismanPaperData {

                type = type == null ? "" : type;
                nickname = nickname == null ? ""
                                : (nickname.length() > MAX_NICKNAME_LENGTH ? nickname.substring(0, MAX_NICKNAME_LENGTH)
                                                : nickname);
                description = description == null ? ""
                                : (description.length() > MAX_DESCRIPTION_LENGTH
                                                ? description.substring(0, MAX_DESCRIPTION_LENGTH)
                                                : description);
                speed = Math.max(1.0, speed);
        }

        public static final TalismanPaperData EMPTY = new TalismanPaperData("", "", "", 1.0);

        public boolean hasData() {
                return !type.isEmpty() && !nickname.isEmpty();
        }

        public XuanLingTypeEnum getTypeEnum() {
                for (XuanLingTypeEnum e : XuanLingTypeEnum.values()) {
                        if (e.type.equals(this.type))
                                return e;
                }
                return null;
        }

        public Component getTypeDisplayName() {
                XuanLingTypeEnum typeEnum = getTypeEnum();
                if (typeEnum != null) {
                        return Component.translatable("xuanling.type." + typeEnum.type);
                }
                return Component.literal("Unknown");
        }

        public static final Codec<TalismanPaperData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codec.STRING.fieldOf("type").forGetter(TalismanPaperData::type),
                        Codec.STRING.fieldOf("nickname").forGetter(TalismanPaperData::nickname),
                        Codec.STRING.fieldOf("description").forGetter(TalismanPaperData::description),
                        Codec.DOUBLE.fieldOf("speed").forGetter(TalismanPaperData::speed))
                        .apply(instance, TalismanPaperData::new));

        public static class Builder {
                private String type = "";
                private String nickname = "";
                private String description = "";
                private double speed = 1.0;

                public Builder type(XuanLingTypeEnum type) {
                        this.type = type == null ? "" : type.type;
                        return this;
                }

                public Builder type(String type) {
                        this.type = type == null ? "" : type;
                        return this;
                }

                public Builder nickname(String nickname) {
                        this.nickname = nickname == null ? "" : nickname;
                        return this;
                }

                public Builder description(String description) {
                        this.description = description == null ? "" : description;
                        return this;
                }

                public Builder speed(double speed) {
                        this.speed = Math.max(1.0, speed);
                        return this;
                }

                public TalismanPaperData build() {
                        return new TalismanPaperData(type, nickname, description, speed);
                }
        }

        public static Builder builder() {
                return new Builder();
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, TalismanPaperData> STREAM_CODEC = new StreamCodec<>() {
                @Override
                public TalismanPaperData decode(RegistryFriendlyByteBuf buf) {
                        String type = buf.readUtf(32);
                        String nickname = buf.readUtf(MAX_NICKNAME_LENGTH);
                        String description = buf.readUtf(MAX_DESCRIPTION_LENGTH);
                        double speed = buf.readDouble();
                        return new TalismanPaperData(type, nickname, description, speed);
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, TalismanPaperData value) {
                        buf.writeUtf(value.type, 32);
                        buf.writeUtf(value.nickname, MAX_NICKNAME_LENGTH);
                        buf.writeUtf(value.description, MAX_DESCRIPTION_LENGTH);
                        buf.writeDouble(value.speed);
                }
        };

        public static final net.minecraft.core.component.DataComponentType<TalismanPaperData> TYPE = net.minecraft.core.component.DataComponentType
                        .<TalismanPaperData>builder()
                        .persistent(TalismanPaperData.CODEC)
                        .networkSynchronized(TalismanPaperData.STREAM_CODEC)
                        .build();

        public TalismanPaperData withType(XuanLingTypeEnum type) {
                return new TalismanPaperData(type == null ? "" : type.type, nickname, description, speed);
        }

        public TalismanPaperData withNickname(String nickname) {
                return new TalismanPaperData(type, nickname, description, speed);
        }

        public TalismanPaperData withDescription(String description) {
                return new TalismanPaperData(type, nickname, description, speed);
        }

        public TalismanPaperData withSpeed(double speed) {
                return new TalismanPaperData(type, nickname, description, Math.max(1.0, speed));
        }

        public TalismanPaperData copy() {
                return new TalismanPaperData(type, nickname, description, speed);
        }
}
