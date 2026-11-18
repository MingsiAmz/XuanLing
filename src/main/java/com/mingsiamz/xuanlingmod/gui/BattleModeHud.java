package com.mingsiamz.xuanlingmod.gui;

import java.util.ArrayList;
import java.util.List;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

public final class BattleModeHud {

        private BattleModeHud() {
        }

        public static final int MAX_COMBO = 4;

        private static final int CELL = 16;
        private static final int CELL_GAP = 3;
        private static final int FRAME_PAD = 5;

        private static final float SCALE = 0.4f;

        private static final long PRESS_HIGHLIGHT_MS = 250;
        private static final long RELEASE_FLASH_MS = 300;

        public record ComboEntry(char key, long pressedAt) {
        }

        private static boolean active = false;
        private static final List<ComboEntry> combo = new ArrayList<>();
        private static long releaseFlashUntil = 0;

        private static final Minecraft mc = Minecraft.getInstance();

        public static boolean isActive() {
                return active;
        }

        public static boolean isEmpty() {
                return combo.isEmpty();
        }

        public static void toggle() {
                active = !active;
                if (!active)
                        combo.clear();
        }

        public static void pressKey(char key) {
                if (combo.size() >= MAX_COMBO)
                        combo.remove(0);
                combo.add(new ComboEntry(key, System.currentTimeMillis()));
        }


        public static String release() {
                StringBuilder sb = new StringBuilder();
                for (ComboEntry e : combo)
                        sb.append(e.key());
                combo.clear();
                releaseFlashUntil = System.currentTimeMillis() + RELEASE_FLASH_MS;
                return sb.toString();
        }

        public static void render(GuiGraphics g, DeltaTracker delta) {
                LocalPlayer p = mc.player;
                if (!active || p == null || mc.options.hideGui || p.isSpectator() || !p.isAlive())
                        return;

                var pose = g.pose();
                pose.pushPose();
                pose.scale(SCALE, SCALE, 1f);
                try {
                        renderFrame(g);
                } finally {
                        pose.popPose();
                }
        }

        private static void renderFrame(GuiGraphics g) {
                Font font = mc.font;
                int cx = Math.round(g.guiWidth() / SCALE / 2);
                int cy = Math.round(g.guiHeight() / SCALE / 2);

                int boxW = MAX_COMBO * CELL + (MAX_COMBO - 1) * CELL_GAP + FRAME_PAD * 2;
                int boxH = CELL + FRAME_PAD * 2;
                int x1 = cx - boxW / 2;
                int y1 = cy + 22;

                long t = System.currentTimeMillis();
                float pulse = (float) (Math.sin(t * 0.003) * 0.5 + 0.5);
                boolean flashing = t < releaseFlashUntil;
                int alpha = (int) ((0.65f + 0.35f * pulse) * 255);
                int rgb = flashing ? CultivationTheme.BORDER_GOLD : CultivationTheme.BORDER_CYAN;
                int border = (rgb & 0x00FFFFFF) | (alpha << 24);

                g.fill(x1, y1, x1 + boxW, y1 + boxH, CultivationTheme.PANEL_BG_DARK);
                GuiRenderUtil.border(g, x1, y1, x1 + boxW, y1 + boxH, border);
                GuiRenderUtil.diamond(g, x1 + 5, y1 + boxH / 2, 2, border);
                GuiRenderUtil.diamond(g, x1 + boxW - 5, y1 + boxH / 2, 2, border);

                int startX = x1 + FRAME_PAD;
                int startY = y1 + FRAME_PAD;
                for (int i = 0; i < MAX_COMBO; i++) {
                        int cellX = startX + i * (CELL + CELL_GAP);
                        ComboEntry e = i < combo.size() ? combo.get(i) : null;
                        boolean filled = e != null;
                        boolean fresh = filled && t - e.pressedAt() < PRESS_HIGHLIGHT_MS;

                        g.fill(cellX, startY, cellX + CELL, startY + CELL,
                                        fresh ? 0x7000E5FF : filled ? 0x3800E5FF : 0x18000000);
                        GuiRenderUtil.border(g, cellX, startY, cellX + CELL, startY + CELL,
                                        fresh ? CultivationTheme.ACCENT : filled ? 0x8000E5FF
                                                        : CultivationTheme.BORDER_DIM);

                        if (filled) {
                                String s = String.valueOf(e.key());
                                g.drawString(font, s, cellX + (CELL - font.width(s)) / 2,
                                                startY + (CELL - 8) / 2,
                                                fresh ? CultivationTheme.WHITE : CultivationTheme.TEXT_PRIMARY,
                                                false);
                        }
                }

                if (!combo.isEmpty()) {
                        String hint = XuanLing.trans(XuanlingLangPro.GUI_STRING, "battle_release_hint").getString();
                        int hintColor = (CultivationTheme.TEXT_SECONDARY & 0x00FFFFFF)
                                        | (((int) (alpha * 0.8f)) << 24);
                        g.drawString(font, hint, cx - font.width(hint) / 2, y1 + boxH + 3, hintColor, false);
                }
        }
}
