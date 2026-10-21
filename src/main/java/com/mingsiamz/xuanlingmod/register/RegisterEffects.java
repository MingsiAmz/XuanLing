package com.mingsiamz.xuanlingmod.register;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.effects.EffectMeditation;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterEffects {
        public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, XuanLing.MODID);

        public static final DeferredHolder<MobEffect, EffectMeditation> EFFECT_MEDITATION = EFFECTS.register("effect_meditation", EffectMeditation::new);
}
