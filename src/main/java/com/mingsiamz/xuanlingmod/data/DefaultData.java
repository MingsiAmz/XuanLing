package com.mingsiamz.xuanlingmod.data;

import com.electronwill.nightconfig.core.ConfigSpec;
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
	if (normalLife >= Config.getMaxValue(Config.RANGE_NORMAL_LIFE)) {
	    normalLife = Config.getMaxValue(Config.RANGE_NORMAL_LIFE);
	} else if (normalLife <= Config.getMinValue(Config.RANGE_NORMAL_LIFE)) {
	    normalLife = Config.getMinValue(Config.RANGE_NORMAL_LIFE);
	}
	this.normalLife = normalLife;
    }
    public double getSoulLife() {
	return soulLife;
    }
    public void setSoulLife(double soulLife) {
	if (soulLife >= Config.getMaxValue(Config.RANGE_SOUL_LIFE)) {
	    soulLife = Config.getMaxValue(Config.RANGE_SOUL_LIFE);
	} else if (soulLife <= Config.getMinValue(Config.RANGE_SOUL_LIFE)) {
	    soulLife = Config.getMinValue(Config.RANGE_SOUL_LIFE);
	}
	this.soulLife = soulLife;
    }
    public double getMoveSpeedMof() {
	return moveSpeedMof;
    }
    public void setMoveSpeedMof(double moveSpeedMof) {
	if (moveSpeedMof >= Config.getMaxValue(Config.RANGE_MOVE_SPEED)) {
	    moveSpeedMof = Config.getMaxValue(Config.RANGE_MOVE_SPEED);
	} else if (moveSpeedMof <= Config.getMinValue(Config.RANGE_MOVE_SPEED)) {
	    moveSpeedMof = Config.getMinValue(Config.RANGE_MOVE_SPEED);
	}
	this.moveSpeedMof = moveSpeedMof;
    }
    public double getFlySpeedMof() {
	return flySpeedMof;
    }
    public void setFlySpeedMof(double flySpeedMof) {
	if (flySpeedMof >= Config.getMaxValue(Config.RANGE_FLY_SPEED)) {
	    flySpeedMof = Config.getMaxValue(Config.RANGE_FLY_SPEED);
	} else if (flySpeedMof <= Config.getMinValue(Config.RANGE_FLY_SPEED)) {
	    flySpeedMof = Config.getMinValue(Config.RANGE_FLY_SPEED);
	}
	this.flySpeedMof = flySpeedMof;
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
    public double getTPAO() {
	return TPAO;
    }
    public void setTPAO(double tPAO) {
	if (tPAO >= Config.getMaxValue(Config.RANGE_TPAO)) {
	    tPAO = Config.getMaxValue(Config.RANGE_TPAO);
	} else if (tPAO <= Config.getMinValue(Config.RANGE_TPAO)) {
	    tPAO = Config.getMinValue(Config.RANGE_TPAO);
	}
	TPAO = tPAO;
    }
    public double getInsight() {
	return insight;
    }
    public void setInsight(double insight) {
	if (insight >= Config.getMaxValue(Config.RANGE_INSIGHT)) {
	    insight = Config.getMaxValue(Config.RANGE_INSIGHT);
	} else if (insight <= Config.getMinValue(Config.RANGE_INSIGHT)) {
	    insight = Config.getMinValue(Config.RANGE_INSIGHT);
	}
	this.insight = insight;
    }
    public double getInspiration() {
	return inspiration;
    }
    public void setInspiration(double inspiration) {
	if (inspiration >= Config.getMaxValue(Config.RANGE_INSPIRATION)) {
	    inspiration = Config.getMaxValue(Config.RANGE_INSPIRATION);
	} else if (inspiration <= Config.getMinValue(Config.RANGE_INSPIRATION)) {
	    inspiration = Config.getMinValue(Config.RANGE_INSPIRATION);
	}
	this.inspiration = inspiration;
    }
    public double getLucky() {
	return lucky;
    }
    public void setLucky(double lucky) {
	if (lucky >= Config.getMaxValue(Config.RANGE_LUCKY)) {
	    lucky = Config.getMaxValue(Config.RANGE_LUCKY);
	} else if (lucky <= Config.getMinValue(Config.RANGE_LUCKY)) {
	    lucky = Config.getMinValue(Config.RANGE_LUCKY);
	}
	this.lucky = lucky;
    }
    public double getCharm() {
	return charm;
    }
    public void setCharm(double charm) {
	if (charm >= Config.getMaxValue(Config.RANGE_CHARM)) {
	    charm = Config.getMaxValue(Config.RANGE_CHARM);
	} else if (charm <= Config.getMinValue(Config.RANGE_CHARM)) {
	    charm = Config.getMinValue(Config.RANGE_CHARM);
	}
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
