package com.mingsiamz.xuanlingmod.render;

import java.util.Optional;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = XuanLing.MODID, value = Dist.CLIENT)
public final class EnergyMultiplierRenderer {

        private static final int OUTLINE_RED = 180;
        private static final int OUTLINE_GREEN = 77;
        private static final int OUTLINE_BLUE = 255;
        private static final double EFFECT_RANGE = 64.0;
        private static final double PARTICLE_RANGE = 32.0;

        private static final MultiBufferSource.BufferSource OUTLINE_BACKING = MultiBufferSource
                        .immediate(new ByteBufferBuilder(1536));
        private static final OutlineBufferSource OUTLINE_SOURCE = new OutlineBufferSource(OUTLINE_BACKING);
        private static final VertexConsumer DISCARD = new VertexConsumer() {
                @Override
                public VertexConsumer addVertex(float x, float y, float z) {
                        return this;
                }

                @Override
                public VertexConsumer setColor(int red, int green, int blue, int alpha) {
                        return this;
                }

                @Override
                public VertexConsumer setUv(float u, float v) {
                        return this;
                }

                @Override
                public VertexConsumer setUv1(int u, int v) {
                        return this;
                }

                @Override
                public VertexConsumer setUv2(int u, int v) {
                        return this;
                }

                @Override
                public VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
                        return this;
                }
        };

        private static boolean renderingOutline;

        private EnergyMultiplierRenderer() {
        }

        @SubscribeEvent
        public static void onRenderPlayer(RenderPlayerEvent.Post event) {
                if (renderingOutline)
                        return;

                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.screen != null)
                        return;
                if (!(event.getEntity() instanceof AbstractClientPlayer player))
                        return;
                if (player.isSpectator() || !player.isAlive() || player.isInvisible())
                        return;

                float multiplier = multiplierOf(player);
                if (multiplier <= 0f)
                        return;

                Vec3 camera = minecraft.gameRenderer.getMainCamera().getPosition();
                if (player.position().distanceToSqr(camera) > EFFECT_RANGE * EFFECT_RANGE)
                        return;
                if (!minecraft.levelRenderer.shouldShowEntityOutlines())
                        return;

                float pulse = 0.82f + 0.18f * Mth.sin((player.tickCount + event.getPartialTick())
                                * (0.08f + 0.12f * multiplier));
                float strength = 0.55f + 0.45f * multiplier;
                OUTLINE_SOURCE.setColor(
                                Math.round(OUTLINE_RED * pulse * strength),
                                Math.round(OUTLINE_GREEN * pulse * strength),
                                Math.round(OUTLINE_BLUE * pulse * strength),
                                255);

                minecraft.levelRenderer.requestOutlineEffect();
                renderingOutline = true;
                try {
                        event.getRenderer().render(player, player.getYRot(), event.getPartialTick(),
                                        event.getPoseStack(), EnergyMultiplierRenderer::outlineBuffer,
                                        event.getPackedLight());
                } finally {
                        OUTLINE_SOURCE.endOutlineBatch();
                        renderingOutline = false;
                }
        }

        private static VertexConsumer outlineBuffer(RenderType renderType) {
                Optional<RenderType> outline = renderType.outline();
                return outline.isPresent() ? OUTLINE_SOURCE.getBuffer(outline.get()) : DISCARD;
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.level == null || minecraft.player == null || minecraft.isPaused())
                        return;

                for (AbstractClientPlayer player : minecraft.level.players()) {
                        float multiplier = multiplierOf(player);
                        if (multiplier <= 0f || player.isInvisible())
                                continue;
                        if (player.distanceToSqr(minecraft.player) > PARTICLE_RANGE * PARTICLE_RANGE)
                                continue;
                        spawnParticles(minecraft, player, multiplier);
                }
        }

        private static void spawnParticles(Minecraft minecraft, Player player, float multiplier) {
                RandomSource random = player.getRandom();
                float spawnCount = multiplier * 3.0f;
                int count = (int) spawnCount;
                if (random.nextFloat() < spawnCount - count)
                        count++;

                for (int i = 0; i < count; i++) {
                        double angle = random.nextDouble() * Math.PI * 2.0;
                        double radius = player.getBbWidth() * 0.5 + random.nextDouble() * 0.15;
                        double x = player.getX() + Math.cos(angle) * radius;
                        double y = player.getY() + 0.15 + random.nextDouble() * (player.getBbHeight() - 0.3);
                        double z = player.getZ() + Math.sin(angle) * radius;
                        double speed = 0.015 + 0.05 * multiplier;
                        double vy = (random.nextDouble() - 0.5) * 0.02;

                        ParticleOptions particle = multiplier > 0.5f && random.nextFloat() < 0.3f
                                        ? ParticleTypes.DRAGON_BREATH
                                        : ParticleTypes.PORTAL;
                        minecraft.level.addParticle(particle, x, y, z,
                                        Math.cos(angle) * speed, vy, Math.sin(angle) * speed);
                }
        }

        private static float multiplierOf(Player player) {
                BasicData data = RegisterAttachments.getBasicData(player);
                return data != null ? Mth.clamp(data.getEnergyMultiplier(), 0f, 1f) : 0f;
        }
}
