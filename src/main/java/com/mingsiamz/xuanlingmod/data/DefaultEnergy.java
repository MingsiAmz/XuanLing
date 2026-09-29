package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class DefaultEnergy implements IEnergyStorage {

        private double energy = 0.0;
        private String name = "LingLi";
        private Component trans = XuanLing.trans("misc", "energy");

        public DefaultEnergy() {

        }

        public DefaultEnergy(RegistryFriendlyByteBuf buf) {
                
        }

        public DefaultEnergy(double energy, String name) {
                this.energy = energy;
                this.name = name;
        }

        public static final Codec<DefaultEnergy> CODEC = RecordCodecBuilder.create(builder -> builder
                        .group(
                                        Codec.DOUBLE.fieldOf("energy").forGetter(de -> de.energy),
                                        Codec.STRING.fieldOf("energy_name").forGetter(de -> de.name))
                        .apply(builder, DefaultEnergy::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DefaultEnergy> STREAM_CODEC = StreamCodec.ofMember(DefaultEnergy::encode, DefaultEnergy::new);

        public void encode(RegistryFriendlyByteBuf buf){
                
        }

        public double getEnergy() {
                return energy;
        }

        public void setEnergy(double energy) {
                if (energy >= Config.getMaxValue(Config.RANGE_ENERGY)) {
                        energy = Config.getMaxValue(Config.RANGE_ENERGY);
                } else if (energy <= Config.getMinValue(Config.RANGE_ENERGY)) {
                        energy = Config.getMinValue(Config.RANGE_ENERGY);
                }
                this.energy = energy;
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
                if (!simulate) {
                        if (this.energy + toReceive >= Config.getMaxValue(Config.RANGE_ENERGY))
                                this.energy = Config.getMaxValue(Config.RANGE_ENERGY);
                        else
                                this.energy += toReceive;
                }
                return (int) this.energy;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
                if (!simulate) {
                        if (this.energy - toExtract <= Config.getMinValue(Config.RANGE_ENERGY))
                                this.energy = Config.getMinValue(Config.RANGE_ENERGY);
                        else
                                this.energy -= toExtract;
                }
                return (int) this.energy;
        }

        @Override
        public int getEnergyStored() {
                return (int) this.energy;
        }

        @Override
        public int getMaxEnergyStored() {
                return Config.getMaxValue(Config.RANGE_ENERGY).intValue();
        }

        @Override
        public boolean canExtract() {
                return true;
        }

        @Override
        public boolean canReceive() {
                return true;
        }

        public String getName() {
                return name;
        }

}
