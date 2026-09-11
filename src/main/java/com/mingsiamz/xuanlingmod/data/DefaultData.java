package com.mingsiamz.xuanlingmod.data;

import java.util.Map;

import com.electronwill.nightconfig.core.ConfigSpec;
import com.google.gson.annotations.SerializedName;
import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class DefaultData {
    private double physicalDefense = 0.0;
    private double magicDefense = 0.0;
    private double energy = 0.0;

    public static final Codec<DefaultData> CODEC = RecordCodecBuilder.create(builder -> builder
									     .group(
										    Codec.DOUBLE.fieldOf("physical_defense").forGetter(d -> d.physicalDefense),
										    Codec.DOUBLE.fieldOf("magic_defense").forGetter(d -> d.magicDefense),
										    Codec.DOUBLE.fieldOf("energy").forGetter(d -> d.energy))
										    .apply(builder, DefaultData::new));

    public DefaultData() {
	
    }

    public DefaultData(double physicalDefense,
		       double magicDefense,
		       double energy
		       )
    {
	this.physicalDefense = physicalDefense;
	this.magicDefense = magicDefense;
	this.energy = energy;
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

    public static DefaultData init() {
	DefaultData data = new DefaultData();
	return data;
    }

}
