package com.mingsiamz.xuanlingmod.client;

import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.gameobject.emitter.data.number.NumberFunction3;
import com.lowdragmc.photon.client.fx.EntityEffectExecutor;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.gameobject.emitter.particle.ParticleEmitter;

import org.joml.Vector3f;

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

}
