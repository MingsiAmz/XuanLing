package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class DefaultData {
    private double physicalDefense = 0.0;
    private double magicDefense = 0.0;
    private double normalLife = 100.0;
    private double soulLife = 100.0;
    private double moveSpeedMof = 0.0;
    private double flySpeedMof = 0.0;
    private double energy = 0.0;
    private double TPAO = 0.0; // The Primordial Aperture of the One -> 先天一窍
    private double insight = 0.0;
    private double inspiration = 0.0;
    private double lucky = 0.0;
    private double charm = 0.0;

    public static final Codec<DefaultData> CODEC = RecordCodecBuilder.create(builder -> builder.group(Codec.DOUBLE.fieldOf("physical_defense").forGetter(d -> d.physicalDefense),Codec.DOUBLE.fieldOf("magic_defense").forGetter(d -> d.magicDefense),Codec.DOUBLE.fieldOf("normalLife").forGetter(d -> d.normalLife),Codec.DOUBLE.fieldOf("soul_life").forGetter(d -> d.soulLife),Codec.DOUBLE.fieldOf("move_speed_mof").forGetter(d -> d.moveSpeedMof),Codec.DOUBLE.fieldOf("fly_speed_mof").forGetter(d -> d.flySpeedMof),Codec.DOUBLE.fieldOf("energy").forGetter(d -> d.energy),Codec.DOUBLE.fieldOf("TPAO").forGetter(d -> d.TPAO),Codec.DOUBLE.fieldOf("insight").forGetter(d -> d.insight),Codec.DOUBLE.fieldOf("inspiration").forGetter(d -> d.inspiration),Codec.DOUBLE.fieldOf("lucky").forGetter(d -> d.lucky),Codec.DOUBLE.fieldOf("charm").forGetter(d -> d.charm)).apply(builder, DefaultData::new));

    public DefaultData() {
	
    }

    public DefaultData(double physicalDefense,
		       double magicDefense,
		       double normalLife,
		       double soulLife,
		       double moveSpeedMof,
		       double flySpeedMof,
		       double energy,
		       double TPAO,
		       double insight,
		       double inspiration,
		       double lucky,
		       double charm)
    {
	
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
    public double getNormalLife() {
	return normalLife;
    }
    public void setNormalLife(double normalLife) {
	this.normalLife = normalLife;
    }
    public double getSoulLife() {
	return soulLife;
    }
    public void setSoulLife(double soulLife) {
	this.soulLife = soulLife;
    }
    public double getMoveSpeedMof() {
	return moveSpeedMof;
    }
    public void setMoveSpeedMof(double moveSpeedMof) {
	this.moveSpeedMof = moveSpeedMof;
    }
    public double getFlySpeedMof() {
	return flySpeedMof;
    }
    public void setFlySpeedMof(double flySpeedMof) {
	this.flySpeedMof = flySpeedMof;
    }
    public double getEnergy() {
	return energy;
    }
    public void setEnergy(double energy) {
	this.energy = energy;
    }
    public double getTPAO() {
	return TPAO;
    }
    public void setTPAO(double tPAO) {
	TPAO = tPAO;
    }
    public double getInsight() {
	return insight;
    }
    public void setInsight(double insight) {
	this.insight = insight;
    }
    public double getInspiration() {
	return inspiration;
    }
    public void setInspiration(double inspiration) {
	this.inspiration = inspiration;
    }
    public double getLucky() {
	return lucky;
    }
    public void setLucky(double lucky) {
	this.lucky = lucky;
    }
    public double getCharm() {
	return charm;
    }
    public void setCharm(double charm) {
	this.charm = charm;
    }

    public static DefaultData initData() {
	DefaultData data = new DefaultData();
	double randTPAO = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_TPAO), Config.getMaxValue(Config.RANGE_TPAO));
	data.setTPAO(randTPAO);
	double randInsight = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_INSIGHT), Config.getMaxValue(Config.RANGE_INSIGHT));
	data.setInsight(randInsight);
	double randLucky = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_LUCKY), Config.getMaxValue(Config.RANGE_LUCKY));
	data.setLucky(randLucky);
	double randCharm = XuanLing.RANDOM.nextDouble(Config.getMinValue(Config.RANGE_CHARM), Config.getMaxValue(Config.RANGE_CHARM));
	data.setCharm(randCharm);
	return data;
    }
    
}
