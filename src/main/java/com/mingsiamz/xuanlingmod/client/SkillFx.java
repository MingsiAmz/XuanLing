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

public final class SkillFx {

    private SkillFx() {
    }

    public static void play(Minecraft minecraft, int entityId) {
        play(minecraft, entityId, false);
    }

    public static void play(Minecraft minecraft, int entityId, boolean trail) {
        if (minecraft.level == null) {
            return;
        }
        Entity entity = minecraft.level.getEntity(entityId);
        if (entity == null) {
            return;
        }

        // B1：弹射物使用 photon2 圆形特效加拖尾，在碰撞箱中心渲染
        if (entity instanceof com.mingsiamz.xuanlingmod.entities.ProjectileSkillEntity) {
            playProjectile(minecraft, entity, trail);
            return;
        }

        // 按特效等级分桶渲染 photon2 特效
        Config.SpecialEffectLevel level = Config.SPECIAL_EFFECT_LEVEL.get();
        switch (level) {
            case LOW -> playHalo(minecraft, entity, trail);
            case MEDIUM -> playSkillMedium(minecraft, entity, trail);
            case HIGH -> playSkillFull(minecraft, entity, trail);
        }
    }

    /**
     * HIGH 等级：完整的 photon2 神通特效。
     */
    private static void playSkillFull(Minecraft minecraft, Entity entity, boolean trail) {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.config.setDuration(trail ? 8 : 40);
        emitter.config.setLooping(false);
        emitter.config.setStartLifetime(NumberFunction.constant(trail ? 12 : 20));
        emitter.config.setStartSpeed(NumberFunction.constant(trail ? 0.02 : 0.08));
        emitter.config.setStartSize(new NumberFunction3(trail ? 0.08f : 0.15f, trail ? 0.08f : 0.15f,
                trail ? 0.08f : 0.15f));
        emitter.config.setStartColor(NumberFunction.color(0xFF55E6FF));
        emitter.config.setMaxParticles(trail ? 32 : 128);
        emitter.config.emission.setEmissionRate(NumberFunction.constant(trail ? 4 : 8));
        FX fx = new FX();
        fx.getFxData().objects().add(emitter);
        EntityEffectExecutor effect = new EntityEffectExecutor(fx, minecraft.level, entity,
                EntityEffectExecutor.AutoRotate.FORWARD);
        effect.setOffset(new Vector3f(0, entity.getBbHeight() * 0.5f, 0));
        effect.setAllowMulti(trail);
        effect.start();
    }

    /**
     * MEDIUM 等级：降低粒子数的简化神通特效。
     */
    private static void playSkillMedium(Minecraft minecraft, Entity entity, boolean trail) {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.config.setDuration(trail ? 6 : 20);
        emitter.config.setLooping(false);
        emitter.config.setStartLifetime(NumberFunction.constant(trail ? 8 : 10));
        emitter.config.setStartSpeed(NumberFunction.constant(trail ? 0.02 : 0.06));
        emitter.config.setStartSize(new NumberFunction3(trail ? 0.06f : 0.1f, trail ? 0.06f : 0.1f,
                trail ? 0.06f : 0.1f));
        emitter.config.setStartColor(NumberFunction.color(0xFF55E6FF));
        emitter.config.setMaxParticles(trail ? 16 : 64);
        emitter.config.emission.setEmissionRate(NumberFunction.constant(trail ? 2 : 4));
        FX fx = new FX();
        fx.getFxData().objects().add(emitter);
        EntityEffectExecutor effect = new EntityEffectExecutor(fx, minecraft.level, entity,
                EntityEffectExecutor.AutoRotate.FORWARD);
        effect.setOffset(new Vector3f(0, entity.getBbHeight() * 0.5f, 0));
        effect.setAllowMulti(trail);
        effect.start();
    }

    /**
     * LOW 等级：特效简化为简单光环，降低低配机压力。
     */
    private static void playHalo(Minecraft minecraft, Entity entity, boolean trail) {
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.config.setDuration(trail ? 8 : 20);
        emitter.config.setLooping(!trail);
        emitter.config.setStartLifetime(NumberFunction.constant(trail ? 6 : 10));
        emitter.config.setStartSpeed(NumberFunction.constant(0.04));
        emitter.config.setStartSize(new NumberFunction3(0.08f, 0.08f, 0.08f));
        emitter.config.setStartColor(NumberFunction.color(0xFF8888FF));
        emitter.config.setMaxParticles(trail ? 16 : 16);
        emitter.config.emission.setEmissionRate(NumberFunction.constant(trail ? 2 : 2));
        FX fx = new FX();
        fx.getFxData().objects().add(emitter);
        EntityEffectExecutor effect = new EntityEffectExecutor(fx, minecraft.level, entity,
                EntityEffectExecutor.AutoRotate.FORWARD);
        effect.setOffset(new Vector3f(0, entity.getBbHeight() * 0.5f, 0));
        effect.setAllowMulti(trail);
        effect.start();
    }

    /**
     * B1：弹射物专用圆形特效 + 拖尾，粒子围绕碰撞箱中心低速环绕，LOW/MEDIUM/HIGH 均简化为不同粒子数。
     */
    private static void playProjectile(Minecraft minecraft, Entity entity, boolean trail) {
        Config.SpecialEffectLevel level = Config.SPECIAL_EFFECT_LEVEL.get();
        int maxParticles = switch (level) {
            case LOW -> 12;
            case MEDIUM -> 24;
            case HIGH -> 48;
        };
        int rate = switch (level) {
            case LOW -> 2;
            case MEDIUM -> 4;
            case HIGH -> 8;
        };
        ParticleEmitter emitter = new ParticleEmitter();
        emitter.config.setDuration(trail ? 8 : 40);
        emitter.config.setLooping(trail);
        emitter.config.setStartLifetime(NumberFunction.constant(trail ? 10 : 20));
        emitter.config.setStartSpeed(NumberFunction.constant(0.04));
        emitter.config.setStartSize(new NumberFunction3(0.1f, 0.1f, 0.1f));
        emitter.config.setStartColor(NumberFunction.color(0xFF55E6FF));
        emitter.config.setMaxParticles(maxParticles);
        emitter.config.emission.setEmissionRate(NumberFunction.constant(rate));
        FX fx = new FX();
        fx.getFxData().objects().add(emitter);
        EntityEffectExecutor effect = new EntityEffectExecutor(fx, minecraft.level, entity,
                EntityEffectExecutor.AutoRotate.FORWARD);
        effect.setOffset(new Vector3f(0, entity.getBbHeight() * 0.5f, 0));
        effect.setAllowMulti(trail);
        effect.start();
    }
}
