package com.mingsiamz.xuanlingmod.util.gui.component;

import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ProgressBar extends GuiWidget {

        private float value = 0f;
        private float target = 0f;
        private float speed = 0.2f;
        private int barColor;
        private String label = "";
        private boolean showText = true;

        public ProgressBar(int width, int height) {
                super(width, height);
                this.barColor = palette.accent();
        }

        public ProgressBar setBarColor(int c) {
                this.barColor = c;
                return this;
        }

        public ProgressBar setLabel(String l) {
                this.label = l;
                return this;
        }

        public ProgressBar showText(boolean b) {
                this.showText = b;
                return this;
        }

        public ProgressBar setValue(float v) {
                this.target = Mth.clamp(v, 0f, 1f);
                return this;
        }

        public ProgressBar setInstant(float v) {
                this.target = this.value = Mth.clamp(v, 0f, 1f);
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                if (!visible)
                        return;

                this.value = Mth.clamp(value + (target - value) * speed, 0f, 1f);

                int textH = showText ? 10 : 0;
                int barY = y + textH;
                int barH = height - textH;

                if (showText) {
                        g.drawString(font(), Component.literal(label), x, y, palette.textPrimary(), false);
                        String pct = (int) (value * 100) + "%";
                        g.drawString(font(), pct, x + width - font().width(pct), y, palette.textSecondary(), false);
                }

                g.fill(x, barY, x + width, barY + barH, palette.panelBgDark());
                GuiRenderUtil.progressFill(g, x, barY, width, barH, value, barColor);
                GuiRenderUtil.border(g, x, barY, x + width, barY + barH, palette.borderDim());
        }
}
