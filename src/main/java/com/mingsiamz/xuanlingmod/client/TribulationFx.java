package com.mingsiamz.xuanlingmod.client;

import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunction3;
import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.gameobject.emitter.particle.ParticleEmitter;

import org.joml.Vector3f;

import com.mingsiamz.xuanlingmod.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public final class TribulationFx {

    private TribulationFx() {
    }

    public static void playLightning(Minecraft minecraft, int entityId) {
        if (minecraft.level == null) {
            return;
        }
        Entity entity = minecraft.level.getEntity(entityId);
        if (entity == null) {
            return;
        }

        // 按特效等级分桶渲染 photon2 特效
        Config.SpecialEffectLevel level = Config.SPECIAL_EFFECT_LEVEL.get();
        switch (level) {
            case LOW -> playHalo(minecraft, entity);
            case MEDIUM -> playLightningMedium(minecraft, entity);
            case HIGH -> playLightningFull(minecraft, entity);
        }
    }

    /**
     * HIGH 等级：完整的 photon2 雷电特效。
     */
    private static void playLightningFull(Minecraft minecraft, Entity entity) {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.config.setDuration(20);
        emitter.config.setLooping(false);
        emitter.config.setStartLifetime(NumberFunction.constant(20));
        emitter.config.setStartSpeed(NumberFunction.constant(0.1));
        emitter.config.setStartSize(new NumberFunction3(0.2f, 0.2f, 0.2f));
        emitter.config.setStartColor(NumberFunction.color(0xFFEEEEFF));
        emitter.config.setMaxParticles(64);
        emitter.config.emission.setEmissionRate(NumberFunction.constant(8));
        FX fx = new FX();
        fx.getFxData().objects().add(emitter);
        EntityEffectExecutor effect = new EntityEffectExecutor(fx, minecraft.level, entity,
                EntityEffectExecutor.AutoRotate.FORWARD);
        effect.setOffset(new Vector3f(0, entity.getBbHeight() * 0.5f, 0));
        effect.setAllowMulti(true);
        effect.start();
    }

    /**
     * MEDIUM 等级：降低粒子数与持续时间的简化雷电特效。
     */
    private static void playLightningMedium(Minecraft minecraft, Entity entity) {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.config.setDuration(10);
        emitter.config.setLooping(false);
        emitter.config.setStartLifetime(NumberFunction.constant(10));
        emitter.config.setStartSpeed(NumberFunction.constant(0.08));
        emitter.config.setStartSize(new NumberFunction3(0.15f, 0.15f, 0.15f));
        emitter.config.setStartColor(NumberFunction.color(0xFFDDDDFF));
        emitter.config.setMaxParticles(32);
        emitter.config.emission.setEmissionRate(NumberFunction.constant(4));
        FX fx = new FX();
        fx.getFxData().objects().add(emitter);
        EntityEffectExecutor effect = new EntityEffectExecutor(fx, minecraft.level, entity,
                EntityEffectExecutor.AutoRotate.FORWARD);
        effect.setOffset(new Vector3f(0, entity.getBbHeight() * 0.5f, 0));
        effect.setAllowMulti(true);
        effect.start();
    }

    /**
     * LOW 等级：特效简化为围绕实体的简单光环，降低低配机压力。
     */
    private static void playHalo(Minecraft minecraft, Entity entity) {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.config.setDuration(20);
        emitter.config.setLooping(true);
        emitter.config.setStartLifetime(NumberFunction.constant(10));
        emitter.config.setStartSpeed(NumberFunction.constant(0.05));
        emitter.config.setStartSize(new NumberFunction3(0.1f, 0.1f, 0.1f));
        emitter.config.setStartColor(NumberFunction.color(0xFF8888FF));
        emitter.config.setMaxParticles(16);
        emitter.config.emission.setEmissionRate(NumberFunction.constant(2));
        FX fx = new FX();
        fx.getFxData().objects().add(emitter);
        EntityEffectExecutor effect = new EntityEffectExecutor(fx, minecraft.level, entity,
                EntityEffectExecutor.AutoRotate.FORWARD);
        effect.setOffset(new Vector3f(0, entity.getBbHeight() * 0.5f, 0));
        effect.setAllowMulti(false);
        effect.start();
    }
}
