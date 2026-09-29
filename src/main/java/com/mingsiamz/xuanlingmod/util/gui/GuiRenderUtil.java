package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.gui.GuiGraphics;

public final class GuiRenderUtil {
    private GuiRenderUtil() {}

    public static void border(GuiGraphics g, int x1, int y1, int x2, int y2, int color) {
        g.fill(x1, y1, x2, y1 + 1, color); g.fill(x1, y2 - 1, x2, y2, color);
        g.fill(x1, y1, x1 + 1, y2, color); g.fill(x2 - 1, y1, x2, y2, color);
    }

    public static void glow(GuiGraphics g, int x1, int y1, int x2, int y2,
                            int rgb, int layers, int baseAlpha) {
        for (int i = layers; i >= 1; i--) {
            int alpha = Math.min(255, baseAlpha + (layers - i) * 10);
            g.fill(x1 - i, y1 - i, x2 + i, y2 + i, (alpha << 24) | (rgb & 0xFFFFFF));
        }
    }

    public static void cornerAccents(GuiGraphics g, int x1, int y1, int x2, int y2, int len, int color) {
        g.fill(x1 - 2, y1 - 2, x1 + len, y1, color); g.fill(x1 - 2, y1 - 2, x1, y1 + len, color);
        g.fill(x2 - len, y1 - 2, x2 + 2, y1, color); g.fill(x2, y1 - 2, x2 + 2, y1 + len, color);
        g.fill(x1 - 2, y2, x1 + len, y2 + 2, color); g.fill(x1 - 2, y2 - len, x1, y2 + 2, color);
        g.fill(x2 - len, y2, x2 + 2, y2 + 2, color); g.fill(x2, y2 - len, x2 + 2, y2 + 2, color);
    }

    public static void diamond(GuiGraphics g, int cx, int cy, int size, int color) {
        for (int i = 0; i <= size; i++) {
            int w = size - i;
            g.fill(cx - w, cy - i, cx + w + 1, cy - i + 1, color);
            g.fill(cx - w, cy + i, cx + w + 1, cy + i + 1, color);
        }
    }

    public static void grid(GuiGraphics g, int x1, int y1, int x2, int y2, int step, int color) {
        for (int y = y1; y < y2; y += step) g.fill(x1, y, x2, y + 1, color);
    }

    public static void progressFill(GuiGraphics g, int x, int y, int width, int height, float ratio, int color) {
        ratio = Math.max(0f, Math.min(1f, ratio));
        int fillW = (int) (width * ratio);
        if (fillW <= 0) return;

        g.fill(x, y, x + fillW, y + height, color);
        g.fill(x, y, x + fillW, y + 2, 0x66FFFFFF); // 顶部高光
        g.fill(x, y + height - 2, x + fillW, y + height, 0x40000000); // 底部阴影

        if (fillW < width) {
            g.fill(x + fillW - 1, y - 1, x + fillW + 1, y + height + 1, 0xFFFFFFFF);
            g.fill(x + fillW, y, x + fillW + 1, y + height, color);
        }
    }

    public static void ticks(GuiGraphics g, int x, int y, int width, int height, int divisions, int color) {
        for (int i = 1; i < divisions; i++) {
            int sx = x + width * i / divisions;
            g.fill(sx, y, sx + 1, y + height, color);
        }
    }
}
