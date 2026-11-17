package com.mingsiamz.xuanlingmod.util.gui.component;

import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import com.mingsiamz.xuanlingmod.util.gui.theme.ColorPalette;
import com.mingsiamz.xuanlingmod.util.gui.theme.GuiTheme;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class Tooltip {

        private Tooltip() {
        }

        public static void draw(GuiGraphics g, Font font, int mouseX, int mouseY, Component text) {
                if (text == null)
                        return;
                drawLines(g, font, mouseX, mouseY, List.of(text), GuiTheme.active());
        }

        public static void drawLines(GuiGraphics g, Font font, int mouseX, int mouseY, List<Component> lines) {
                drawLines(g, font, mouseX, mouseY, lines, GuiTheme.active());
        }

        public static void drawLines(GuiGraphics g, Font font, int mouseX, int mouseY, List<Component> lines,
                        ColorPalette p) {
                if (lines == null || lines.isEmpty())
                        return;

                int w = 0;
                for (Component c : lines)
                        w = Math.max(w, font.width(c));
                int lineH = 10;
                int h = lines.size() * lineH + 4;

                int bx = mouseX + 12;
                int by = mouseY - h - 4;
                if (by < 4)
                        by = mouseY + 16;

                g.fill(bx, by, bx + w + 8, by + h, p.panelBg());
                GuiRenderUtil.border(g, bx, by, bx + w + 8, by + h, p.borderGold());

                int ty = by + 4;
                for (Component c : lines) {
                        g.drawString(font, c, bx + 4, ty, p.textPrimary(), false);
                        ty += lineH;
                }
        }
}
