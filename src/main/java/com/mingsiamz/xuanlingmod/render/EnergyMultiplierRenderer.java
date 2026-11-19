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
        private static final int OUTLINE_SIDES = 8;

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
                MultiBufferSource buffers = event.getMultiBufferSource();

                pose.pushPose();
                renderOutline(pose, buffers, player, time, multiplier);
                renderRipple(pose, buffers, time, multiplier);
                pose.popPose();
        }

        private static void renderOutline(PoseStack pose, MultiBufferSource buffers, Player player,
                        float time, float multiplier) {
                float pulse = 0.5f + 0.5f * Mth.sin(time * (0.10f + 0.20f * multiplier));
                float intensity = 0.45f + 0.55f * multiplier;
                int alpha = Mth.clamp((int) ((65f + 125f * pulse) * intensity), 0, 255);

                float radius = player.getBbWidth() * 0.5f + 0.25f;
                float height = player.getBbHeight();
                Matrix4f matrix = pose.last().pose();

                VertexConsumer vc = buffers.getBuffer(RenderType.lines());
                drawCage(vc, matrix, radius * 1.10f, height, auraColor(alpha / 3));
                drawCage(vc, matrix, radius, height, auraColor(alpha));
        }

        private static void drawCage(VertexConsumer vc, Matrix4f matrix, float radius, float height, int color) {
                int verticals = OUTLINE_SIDES;
                for (int i = 0; i < verticals; i++) {
                        float angle = i * TWO_PI / verticals;
                        float x = Mth.cos(angle) * radius;
                        float z = Mth.sin(angle) * radius;
                        line(vc, matrix, x, 0.03f, z, x, height, z, x, 0f, z, color);
                }
                ringLine(vc, matrix, 0.03f, radius, color);
                ringLine(vc, matrix, height * 0.5f, radius, color);
                ringLine(vc, matrix, height * 0.97f, radius, color);
        }

        private static void renderRipple(PoseStack pose, MultiBufferSource buffers, float time, float multiplier) {
                Matrix4f matrix = pose.last().pose();
                float speed = 0.009f + 0.015f * multiplier;
                float maxRadius = 0.75f + 1.0f * multiplier;
                int rings = 3;

                VertexConsumer quads = buffers.getBuffer(RenderType.debugQuads());
                for (int i = 0; i < rings; i++) {
                        float phase = (time * speed + (float) i / rings) % 1f;
                        float radius = 0.32f + maxRadius * phase;
                        int alpha = (int) ((1f - phase) * (40f + 100f * multiplier));
                        ringBand(quads, matrix, 0.02f, radius, radius + 0.06f, auraColor(alpha));
                }

                VertexConsumer lines = buffers.getBuffer(RenderType.lines());
                for (int i = 0; i < rings; i++) {
                        float phase = (time * speed + (float) i / rings) % 1f;
                        float radius = 0.32f + maxRadius * phase;
                        int alpha = (int) ((1f - phase) * (40f + 100f * multiplier));
                        ringLine(lines, matrix, 0.025f, radius + 0.06f, auraColor(alpha + 40));
                }
        }

        private static void ringLine(VertexConsumer vc, Matrix4f matrix, float y, float radius, int color) {
                for (int i = 0; i < SEGMENTS; i++) {
                        float a0 = i * TWO_PI / SEGMENTS;
                        float a1 = (i + 1) * TWO_PI / SEGMENTS;
                        line(vc, matrix,
                                        Mth.cos(a0) * radius, y, Mth.sin(a0) * radius,
                                        Mth.cos(a1) * radius, y, Mth.sin(a1) * radius,
                                        0f, 1f, 0f, color);
                }
        }

        private static void ringBand(VertexConsumer vc, Matrix4f matrix, float y,
                        float inner, float outer, int color) {
                for (int i = 0; i < SEGMENTS; i++) {
                        float a0 = i * TWO_PI / SEGMENTS;
                        float a1 = (i + 1) * TWO_PI / SEGMENTS;

                        vc.addVertex(matrix, Mth.cos(a0) * inner, y, Mth.sin(a0) * inner).setColor(color);
                        vc.addVertex(matrix, Mth.cos(a0) * outer, y, Mth.sin(a0) * outer).setColor(color);
                        vc.addVertex(matrix, Mth.cos(a1) * outer, y, Mth.sin(a1) * outer).setColor(color);
                        vc.addVertex(matrix, Mth.cos(a1) * inner, y, Mth.sin(a1) * inner).setColor(color);
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
