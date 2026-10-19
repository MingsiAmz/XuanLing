package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class DefaultData {
        private String realmType = "normal";
        private double physicalDefense = 0.0;
        private double magicDefense = 0.0;
        private int realmLevel = 0;
        private Component literal = XuanLing.trans(XuanlingLangPro.MISC_STRING, "default_data");

        public static final Codec<DefaultData> CODEC = RecordCodecBuilder.create(builder -> builder
                                                                                 .group(
                                                                                        Codec.DOUBLE.fieldOf("physical_defense").forGetter(d -> d.physicalDefense),
                                                                                        Codec.DOUBLE.fieldOf("magic_defense").forGetter(d -> d.magicDefense),
                                                                                        Codec.STRING.fieldOf("realm_type").forGetter(d -> d.realmType),
                                                                                        Codec.INT.fieldOf("realmLevel").forGetter(d -> d.realmLevel))
                                                                                 .apply(builder, DefaultData::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DefaultData> STREAM_CODEC = StreamCodec.ofMember(DefaultData::encode, DefaultData::new);

        public DefaultData() {

        }

        public DefaultData(RegistryFriendlyByteBuf buf) {
                this.physicalDefense = buf.readDouble();
                this.magicDefense = buf.readDouble();
                this.realmType = buf.readUtf();
                this.realmLevel = buf.readInt();
        }

        public DefaultData(double physicalDefense,
                           double magicDefense,
                           String realmType,
                           int realmLevel) {
                this.physicalDefense = physicalDefense;
                this.magicDefense = magicDefense;
                this.realmLevel = realmLevel;
                this.realmType = realmType;
        }

        public void encode(RegistryFriendlyByteBuf buf) {
                buf.writeDouble(this.physicalDefense);
                buf.writeDouble(this.magicDefense);
                buf.writeUtf(this.realmType);
                buf.writeInt(this.realmLevel);
        }

        public double getPhysicalDefense() {
                return physicalDefense;
        }

        public void setPhysicalDefense(double physicalDefense) {
                this.physicalDefense = physicalDefense;
        }

        public double getMagicDefense() {
                return magicDefense;
        }

        public void setMagicDefense(double magicDefense) {
                this.magicDefense = magicDefense;
        }

        public int getRealmLevel() {
                return realmLevel;
        }

        public void setRealmLevel(int realmLevel) {
                this.realmLevel = realmLevel;
        }

        public String getRealmType() {
                return realmType;
        }

        public static DefaultData init() {
                DefaultData data = new DefaultData();
                return data;
        }

}
