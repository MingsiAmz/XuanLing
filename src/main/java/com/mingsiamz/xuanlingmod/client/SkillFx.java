package com.mingsiamz.xuanlingmod.client;

import org.joml.Vector3f;

import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunction3;
import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.gameobject.emitter.particle.ParticleEmitter;

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
}
