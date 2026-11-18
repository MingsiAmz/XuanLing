package com.mingsiamz.xuanlingmod.register;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.effects.EffectCriticalInjury;
import com.mingsiamz.xuanlingmod.effects.EffectDeviation;
import com.mingsiamz.xuanlingmod.effects.EffectMeditation;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterEffects {
        public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, XuanLing.MODID);

        public static final DeferredHolder<MobEffect, EffectMeditation> EFFECT_MEDITATION = EFFECTS.register("effect_meditation", EffectMeditation::new);

        public static final DeferredHolder<MobEffect, EffectDeviation> EFFECT_DEVIATION = EFFECTS.register("effect_deviation", EffectDeviation::new);

        public static final DeferredHolder<MobEffect, EffectCriticalInjury> EFFECT_CRITICAL_INJURY = EFFECTS.register("effect_critical_injury", EffectCriticalInjury::new);

        public static void register(IEventBus modBus){
                EFFECTS.register(modBus);
        }
}
