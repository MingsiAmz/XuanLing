package com.mingsiamz.xuanlingmod.render;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import org.joml.Matrix4f;

@EventBusSubscriber(modid = XuanLing.MODID, value = Dist.CLIENT)
public final class EnergyMultiplierRenderer {

        private static final int AURA_RGB = 0xB44DFF;
        private static final float TWO_PI = (float) (Math.PI * 2.0);
        private static final int SEGMENTS = 48;

        private EnergyMultiplierRenderer() {
        }

        @SubscribeEvent
        public static void onRenderPlayer(RenderPlayerEvent.Post event) {
                Player player = event.getEntity();
                if (player.isSpectator() || !player.isAlive())
                        return;

                BasicData data = RegisterAttachments.getBasicData(player);
                if (data == null)
                        return;

                float multiplier = Mth.clamp(data.getEnergyMultiplier(), 0f, 1f);
                if (multiplier <= 0f)
                        return;

                Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
                if (player.position().distanceToSqr(camera) > 64.0 * 64.0)
                        return;

                float time = player.tickCount + event.getPartialTick();
                PoseStack pose = event.getPoseStack();

                pose.pushPose();
                renderBodyAura(pose, event.getMultiBufferSource(), player, time, multiplier);
                pose.popPose();
        }

        private static void renderBodyAura(PoseStack pose, MultiBufferSource buffers, Player player,
                        float time, float multiplier) {
                float height = player.getBbHeight();
                float radiusX = player.getBbWidth() * 0.5f + 0.08f;
                float radiusZ = radiusX * 0.70f;

                float breath = 0.72f + 0.28f * Mth.sin(time * (0.08f + 0.12f * multiplier));
                float strength = 0.35f + 0.65f * multiplier;
                float phase = (time * (0.004f + 0.010f * multiplier)) % 1f;
                float pulseScale = 1f + 0.04f * (breath - 0.5f);

                VertexConsumer lines = buffers.getBuffer(RenderType.lines());
                Matrix4f matrix = pose.last().pose();
                int rings = 32;

                for (int i = 0; i < rings; i++) {
                        float bodyT = (i + 0.5f) / rings;
                        float headWave = Math.max(0f, Mth.cos(TWO_PI * (bodyT - phase)));
                        float secondWave = Math.max(0f, Mth.cos(TWO_PI * (bodyT - phase - 0.5f))) * 0.55f;
                        float wave = Math.max(headWave, secondWave);
                        int alpha = Mth.clamp((int) ((10f + 200f * wave * wave) * strength * breath), 0, 255);
                        float y = 0.04f + (height - 0.08f) * (1f - bodyT);

                        ringEllipse(lines, matrix, y, radiusX * pulseScale, radiusZ * pulseScale, auraColor(alpha));
                }
        }

        private static void ringEllipse(VertexConsumer vc, Matrix4f matrix, float y,
                        float radiusX, float radiusZ, int color) {
                for (int i = 0; i < SEGMENTS; i++) {
                        float a0 = i * TWO_PI / SEGMENTS;
                        float a1 = (i + 1) * TWO_PI / SEGMENTS;
                        line(vc, matrix,
                                        Mth.cos(a0) * radiusX, y, Mth.sin(a0) * radiusZ,
                                        Mth.cos(a1) * radiusX, y, Mth.sin(a1) * radiusZ,
                                        0f, 1f, 0f, color);
                }
        }

        private static void line(VertexConsumer vc, Matrix4f matrix,
                        float x1, float y1, float z1, float x2, float y2, float z2,
                        float nx, float ny, float nz, int color) {
                vc.addVertex(matrix, x1, y1, z1).setColor(color).setNormal(nx, ny, nz);
                vc.addVertex(matrix, x2, y2, z2).setColor(color).setNormal(nx, ny, nz);
        }

        private static int auraColor(int alpha) {
                return (Mth.clamp(alpha, 0, 255) << 24) | AURA_RGB;
        }
}
