package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import com.mingsiamz.xuanlingmod.util.gui.HudDataProvider;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

public final class TopLeftQiHud {

    private TopLeftQiHud() {}

    /* ---------- 布局 ---------- */
    private static final int X = 14;
    private static final int Y = 14;

    private static final int QI_WIDTH   = 180;
    private static final int QI_HEIGHT  = 8;

    private static final int NEILI_WIDTH  = 140;   // 比灵气条短，左边对齐
    private static final int NEILI_HEIGHT = 2;     // 更纤细
    private static final int NEILI_GAP    = 1;     // 略微留一点缝


    /* ---------- 颜色 ---------- */
    private static final int C_QI        = 0xFF26C6DA;
    private static final int C_QI_BRIGHT = 0xFFB2EBF2;
    private static final int C_NEILI     = 0xFF9C27B0;
    private static final int C_NEILI_HI  = 0xFFE1BEE7;
    private static final int C_BG        = 0xFF061018;

    /* ---------- 平滑插值 ---------- */
    private static float dispQi    = 0f;
    private static float dispNeili = 0f;

    public static void render(GuiGraphics g, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null || mc.options.hideGui) return;
        if (p.isCreative() || p.isSpectator() || !p.isAlive()) return;

        int qi       = HudDataProvider.getQi(p);
        int maxQi    = HudDataProvider.getMaxQi(p);
        int neili    = HudDataProvider.getNeili(p);
        int maxNeili = HudDataProvider.getMaxNeili(p);

        float tQi    = maxQi    > 0 ? (float) qi / maxQi       : 0f;
        float tNeili = maxNeili > 0 ? (float) neili / maxNeili : 0f;

        dispQi    += (tQi    - dispQi)    * 0.2f;
        dispNeili += (tNeili - dispNeili) * 0.2f;

        long t = (mc.level != null) ? mc.level.getGameTime() : 0L;

        drawQiBar(g, X, Y, QI_WIDTH, QI_HEIGHT, dispQi, t);
        drawNeiliBar(g, X, Y + QI_HEIGHT + NEILI_GAP,
                     NEILI_WIDTH, NEILI_HEIGHT, dispNeili, t);
    }

    /* ============================================================
     *  灵气条：玉简质感
     * ============================================================ */
    private static void drawQiBar(GuiGraphics g, int x, int y, int w, int h, float ratio, long t) {
        ratio = clamp01(ratio);
        int fillW = (int)(w * ratio);

        /* 1. 轻柔外发光 */
        GuiRenderUtil.glow(g, x, y, x + w, y + h, 0x00E5FF, 2, 10);

        /* 2. 底色 */
        g.fill(x, y, x + w, y + h, C_BG);

        /* 3. 斜纹灵纹 */
        for (int px = 0; px < w; px += 4) {
            int x1 = x + px;
            if (x1 + 1 > x + w) break;
            g.fill(x1, y + 1, x1 + 1, y + h - 1, 0x0AFFFFFF);
        }

        /* 4. 渐变填充 */
        if (fillW > 0) {
            for (int px = 0; px < fillW; px++) {
                float f = (float) px / Math.max(1, w);
                int color = lerpColor(C_QI, C_QI_BRIGHT, 0.2f + f * 0.6f);
                g.fill(x + px, y + 1, x + px + 1, y + h - 1, color);
            }
            g.fill(x, y + 1, x + fillW, y + 2, 0x99FFFFFF);
            g.fill(x, y + h - 2, x + fillW, y + h - 1, 0x50000000);

            /* 流动光带 */
            int bandW  = 24;
            int travel = (int)((t * 1.2) % (w + bandW * 2)) - bandW;
            int bStart = Math.max(x, x + travel);
            int bEnd   = Math.min(x + fillW, x + travel + bandW);
            if (bStart < bEnd) {
                g.fill(bStart, y + 1, bEnd, y + 2, 0xAAFFFFFF);
                g.fill(bStart, y + 2, bEnd, y + 3, 0x33FFFFFF);
            }

            /* 端点白点 */
            if (fillW < w) {
                g.fill(x + fillW - 1, y - 1, x + fillW + 1, y + h + 1, 0xFFFFFFFF);
            }
        }

        /* 5. 分段刻度 */
        for (int i = 1; i < 5; i++) {
            int sx = x + w * i / 5;
            g.fill(sx, y + 2, sx + 1, y + h - 2, 0x25FFFFFF);
        }

        /* 6. 青色边框 */
        GuiRenderUtil.border(g, x, y, x + w, y + h, CultivationTheme.BORDER_CYAN);
    }

    /* ============================================================
     *  内力条：纯净细线 + 渐变 + 流动微光
     * ============================================================ */
    private static void drawNeiliBar(GuiGraphics g, int x, int y, int w, int h, float ratio, long t) {
        ratio = clamp01(ratio);
        int fillW = (int)(w * ratio);

        /* 1. 底色 */
        g.fill(x, y, x + w, y + h, C_BG);

        /* 2. 渐变填充 */
        if (fillW > 0) {
            for (int px = 0; px < fillW; px++) {
                float f = (float) px / Math.max(1, w);
                int color = lerpColor(C_NEILI, C_NEILI_HI, 0.2f + f * 0.5f);
                g.fill(x + px, y, x + px + 1, y + h, color);
            }
            g.fill(x, y, x + fillW, y + 1, 0x99FFFFFF);

            /* 流动微光 */
            int bandW  = 16;
            int travel = (int)((t * 1.0) % (w + bandW * 2)) - bandW;
            int bStart = Math.max(x, x + travel);
            int bEnd   = Math.min(x + fillW, x + travel + bandW);
            if (bStart < bEnd) {
                g.fill(bStart, y, bEnd, y + 1, 0xCCFFFFFF);
            }
        }

        /* 3. 边框（左右不封，保持轻盈） */
        g.fill(x, y - 1, x + w, y,         CultivationTheme.BORDER_DIM);
        g.fill(x, y + h, x + w, y + h + 1, CultivationTheme.BORDER_DIM);
    }

    /* ---------- 工具 ---------- */

    private static int lerpColor(int c1, int c2, float t) {
        t = clamp01(t);
        int a  = lerp((c1 >> 24) & 0xFF, (c2 >> 24) & 0xFF, t);
        int r  = lerp((c1 >> 16) & 0xFF, (c2 >> 16) & 0xFF, t);
        int gg = lerp((c1 >> 8)  & 0xFF, (c2 >> 8)  & 0xFF, t);
        int b  = lerp( c1        & 0xFF,  c2        & 0xFF, t);
        return (a << 24) | (r << 16) | (gg << 8) | b;
    }
    private static int lerp(int a, int b, float t) { return (int)(a + (b - a) * t); }
    private static float clamp01(float v) { return Math.max(0f, Math.min(1f, v)); }
}
