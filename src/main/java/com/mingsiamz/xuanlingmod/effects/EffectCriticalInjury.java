package com.mingsiamz.xuanlingmod.effects;

import java.awt.Color;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectCriticalInjury extends MobEffect {

    public EffectCriticalInjury() {
        super(MobEffectCategory.HARMFUL, Color.RED.getRGB());
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return true;
    }

}
