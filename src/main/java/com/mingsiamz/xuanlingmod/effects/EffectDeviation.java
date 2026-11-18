package com.mingsiamz.xuanlingmod.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class EffectDeviation extends MobEffect {

    public EffectDeviation() {
        super(MobEffectCategory.HARMFUL, 0xFF444444);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        // 走火入魔叠加挖掘疲劳、缓慢、失明，但 UI 仅显示“走火入魔”
        if (!livingEntity.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 1, false, false));
        }
        if (!livingEntity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1, false, false));
        }
        if (!livingEntity.hasEffect(MobEffects.BLINDNESS)) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0, false, false));
        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return tickCount % 20 == 0;
    }

}
