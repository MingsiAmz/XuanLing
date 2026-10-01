package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import com.mingsiamz.xuanlingmod.util.gui.HudDataProvider;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

public final class TopLeftQiHud {

        private TopLeftQiHud() {
        }

        private static final int X = 14;
        private static final int Y = 14;

        private static final int QI_WIDTH = 180;
        private static final int QI_HEIGHT = 8;

        private static final int NEILI_WIDTH = 180;
        private static final int NEILI_HEIGHT = 3;
        private static final int NEILI_GAP = 3;

        private static final int C_QI = 0xFF26C6DA;
        private static final int C_QI_BRIGHT = 0xFFB2EBF2;
        private static final int C_NEILI = 0xFF9C27B0;
        private static final int C_NEILI_HI = 0xFFE1BEE7;
        private static final int C_BG = 0xFF061018;

        private static float dispQi = 0f;
        private static float dispNeili = 0f;

        public static void render(GuiGraphics g, DeltaTracker delta) {
                Minecraft mc = Minecraft.getInstance();
                LocalPlayer p = mc.player;
                if (p == null || mc.options.hideGui)
                        return;
                if (p.isCreative() || p.isSpectator() || !p.isAlive())
                        return;

                int qi = HudDataProvider.getQi(p);
                int maxQi = HudDataProvider.getMaxQi(p);
                int neili = HudDataProvider.getNeili(p);
                int maxNeili = HudDataProvider.getMaxNeili(p);

                float tQi = maxQi > 0 ? (float) qi / maxQi : 0f;
                float tNeili = maxNeili > 0 ? (float) neili / maxNeili : 0f;

                dispQi += (tQi - dispQi) * 0.2f;
                dispNeili += (tNeili - dispNeili) * 0.2f;

                long t = (mc.level != null) ? mc.level.getGameTime() : 0L;

                drawQiBar(g, X, Y, QI_WIDTH, QI_HEIGHT, dispQi, t);
                drawNeiliBar(g, X, Y + QI_HEIGHT + NEILI_GAP,
                                NEILI_WIDTH, NEILI_HEIGHT, dispNeili, t);
        }

        private static void drawQiBar(GuiGraphics g, int x, int y, int w, int h, float ratio, long t) {
                ratio = clamp01(ratio);
                int fillW = (int) (w * ratio);

                GuiRenderUtil.glow(g, x, y, x + w, y + h, 0x00E5FF, 2, 10);

                g.fill(x, y, x + w, y + h, C_BG);

                for (int px = 0; px < w; px += 4) {
                        int x1 = x + px;
                        if (x1 + 1 > x + w)
                                break;
                        g.fill(x1, y + 1, x1 + 1, y + h - 1, 0x0AFFFFFF);
                }

                if (fillW > 0) {
                        for (int px = 0; px < fillW; px++) {
                                float f = (float) px / Math.max(1, w);
                                int color = lerpColor(C_QI, C_QI_BRIGHT, 0.2f + f * 0.6f);
                                g.fill(x + px, y + 1, x + px + 1, y + h - 1, color);
                        }
                        g.fill(x, y + 1, x + fillW, y + 2, 0x99FFFFFF);
                        g.fill(x, y + h - 2, x + fillW, y + h - 1, 0x50000000);

                        int bandW = 24;
                        int travel = (int) ((t * 1.2) % (w + bandW * 2)) - bandW;
                        int bStart = Math.max(x, x + travel);
                        int bEnd = Math.min(x + fillW, x + travel + bandW);
                        if (bStart < bEnd) {
                                g.fill(bStart, y + 1, bEnd, y + 2, 0xAAFFFFFF);
                                g.fill(bStart, y + 2, bEnd, y + 3, 0x33FFFFFF);
                        }

                        if (fillW < w) {
                                g.fill(x + fillW - 1, y - 1, x + fillW + 1, y + h + 1, 0xFFFFFFFF);
                        }
                }

                for (int i = 1; i < 5; i++) {
                        int sx = x + w * i / 5;
                        g.fill(sx, y + 2, sx + 1, y + h - 2, 0x25FFFFFF);
                }

                GuiRenderUtil.border(g, x, y, x + w, y + h, CultivationTheme.BORDER_CYAN);
        }

        private static void drawNeiliBar(GuiGraphics g, int x, int y, int w, int h, float ratio, long t) {
                ratio = clamp01(ratio);
                int cy = y + h / 2;

                g.fill(x, cy, x + w, cy + 1, 0x40AB47BC);

                int beadCount = w / 6;
                float litF = ratio * beadCount;

                for (int i = 0; i < beadCount; i++) {
                        int bx = x + 3 + i * 6;
                        boolean lit = (i + 0.5f) <= litF;

                        if (lit) {

                                GuiRenderUtil.diamond(g, bx, cy, 2, C_NEILI);
                                GuiRenderUtil.diamond(g, bx, cy, 1, C_NEILI_HI);
                                g.fill(bx, cy - 1, bx + 1, cy, 0xFFFFFFFF);
                        } else {
                                drawHollowDiamond(g, bx, cy, 2, 0x55AB47BC);
                        }
                }

                g.fill(x, y - 1, x + w, y, CultivationTheme.BORDER_DIM);
                g.fill(x, y + h, x + w, y + h + 1, CultivationTheme.BORDER_DIM);
        }

        private static void drawHollowDiamond(GuiGraphics g, int cx, int cy, int size, int color) {
                for (int i = 0; i <= size; i++) {
                        int yUp = cy - size + i;
                        g.fill(cx - i, yUp, cx - i + 1, yUp + 1, color);
                        g.fill(cx + i, yUp, cx + i + 1, yUp + 1, color);
                        int yDn = cy + size - i;
                        g.fill(cx - i, yDn, cx - i + 1, yDn + 1, color);
                        g.fill(cx + i, yDn, cx + i + 1, yDn + 1, color);
                }
        }

        private static int lerpColor(int c1, int c2, float t) {
                t = clamp01(t);
                int a = lerp((c1 >> 24) & 0xFF, (c2 >> 24) & 0xFF, t);
                int r = lerp((c1 >> 16) & 0xFF, (c2 >> 16) & 0xFF, t);
                int gg = lerp((c1 >> 8) & 0xFF, (c2 >> 8) & 0xFF, t);
                int b = lerp(c1 & 0xFF, c2 & 0xFF, t);
                return (a << 24) | (r << 16) | (gg << 8) | b;
        }

        private static int lerp(int a, int b, float t) {
                return (int) (a + (b - a) * t);
        }

        private static float clamp01(float v) {
                return Math.max(0f, Math.min(1f, v));
        }
}
