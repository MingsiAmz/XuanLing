package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.util.gui.StatBar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

public final class PlayerVitalsHud {

        private PlayerVitalsHud() {
        }

        private static final int BAR_WIDTH = 80;
        private static final int BAR_HEIGHT = 7;
        private static final int CENTER_GAP = 14;
        private static final int BOTTOM = 42;

        private static final int C_HEALTH = 0xFFE53935;
        private static final int C_FOOD = 0xFFC77F3F;

        private static final StatBar HEALTH = new StatBar(BAR_WIDTH)
                        .showText(false).showTicks(false)
                        .setBarHeight(BAR_HEIGHT).setColor(C_HEALTH);

        private static final StatBar FOOD = new StatBar(BAR_WIDTH)
                        .showText(false).showTicks(false)
                        .setBarHeight(BAR_HEIGHT).setColor(C_FOOD);

        public static void render(GuiGraphics g, DeltaTracker delta) {
                Minecraft mc = Minecraft.getInstance();
                LocalPlayer p = mc.player;
                if (p == null || mc.options.hideGui)
                        return;
                if (p.isCreative() || p.isSpectator() || !p.isAlive())
                        return;

                int cx = g.guiWidth() / 2;
                int baseY = g.guiHeight() - BOTTOM;
                int leftX = cx - CENTER_GAP / 2 - BAR_WIDTH;
                int rightX = cx + CENTER_GAP / 2;

                int health = (int) Math.ceil(p.getHealth());
                int maxH = (int) p.getMaxHealth();
                int food = p.getFoodData().getFoodLevel();

                HEALTH.setValue(health, maxH).setPosition(leftX, baseY);
                FOOD.setValue(food, 20).setPosition(rightX, baseY);

                HEALTH.render(g, 0, 0, 0);
                FOOD.render(g, 0, 0, 0);

                drawValue(g, leftX, baseY, health, maxH);
                drawValue(g, rightX, baseY, food, 20);
        }

        private static void drawValue(GuiGraphics g, int x, int y, int cur, int max) {
                var font = Minecraft.getInstance().font;
                String txt = cur + "/" + max;
                g.drawString(font, txt,
                                x + (BAR_WIDTH - font.width(txt)) / 2,
                                y + (BAR_HEIGHT - 8) / 2 + 1,
                                0xFFFFFFFF, true);
        }
}
