package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class DefaultData {
        private static String name = "normal";
        private double physicalDefense = 0.0;
        private double magicDefense = 0.0;
        private Component literal = XuanLing.trans("misc", "default_data");
        private DefaultEnergy energy = new DefaultEnergy();

        public static final Codec<DefaultData> CODEC = RecordCodecBuilder.create(builder -> builder
                                                                                 .group(
                                                                                        Codec.DOUBLE.fieldOf("physical_defense").forGetter(d -> d.physicalDefense),
                                                                                        Codec.DOUBLE.fieldOf("magic_defense").forGetter(d -> d.magicDefense),
                                                                                        Codec.DOUBLE.fieldOf("energy").forGetter(d -> d.energy.getEnergy()))
                                                                                 .apply(builder, DefaultData::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DefaultData> STREAM_CODEC = StreamCodec.ofMember(DefaultData::encode, DefaultData::new);

        public DefaultData() {

        }

        public DefaultData(RegistryFriendlyByteBuf buf) {
                
        }

        public DefaultData(double physicalDefense,
                           double magicDefense,
                           double defaultEnergy) {
                this.physicalDefense = physicalDefense;
                this.magicDefense = magicDefense;
                this.energy.setEnergy(defaultEnergy);
        }

        public void encode(RegistryFriendlyByteBuf buf) {
                
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

        public static DefaultData init() {
                DefaultData data = new DefaultData();
                return data;
        }

}
