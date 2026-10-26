package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class StateBar implements IGuiComponent {
        private int x, y;
        private int width;
        private int current, max;
        private int barColor;

        // custom configure
        private String label = "";
        private boolean showText = true;
        private boolean showTicks = true;
        private int barHeight = CultivationTheme.BAR_HEIGHT;
        private int textColor = CultivationTheme.TEXT_PRIMARY;
        private int valueColor = CultivationTheme.TEXT_SECONDARY;

        public StateBar(int width) {
                this.width = width;
                this.barColor = CultivationTheme.BORDER_CYAN;
        }

        public StateBar setLabel(String label) {
                this.label = label;
                return this;
        }

        public StateBar setColor(int color) {
                this.barColor = color;
                return this;
        }

        public StateBar setValue(int current, int max) {
                this.current = current;
                this.max = max;
                return this;
        }

        public StateBar setBarHeight(int height) {
                this.barHeight = height;
                return this;
        }

        public StateBar showText(boolean show) {
                this.showText = show;
                return this;
        }

        public StateBar showTicks(boolean show) {
                this.showTicks = show;
                return this;
        }

        public StateBar setTextColors(int labelColor, int valueColor) {
                this.textColor = labelColor;
                this.valueColor = valueColor;
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                Font font = net.minecraft.client.Minecraft.getInstance().font;

                if (showText) {
                        g.drawString(font, Component.literal(label), x, y, textColor, false);
                        String valueText = current + " / " + max;
                        g.drawString(font, Component.literal(valueText),
                                        x + width - font.width(valueText), y, valueColor, false);
                }

                int barY = showText ? y + 12 : y;

                g.fill(x - 1, barY - 1, x + width + 1, barY + barHeight + 1, 0x80000000);
                g.fill(x, barY, x + width, barY + barHeight, CultivationTheme.PANEL_BG_DARK);

                float ratio = max <= 0 ? 0f : (float) current / max;
                GuiRenderUtil.progressFill(g, x, barY, width, barHeight, ratio, barColor);

                if (showTicks) {
                        GuiRenderUtil.ticks(g, x, barY, width, barHeight, 10, 0x30FFFFFF);
                }
                GuiRenderUtil.border(g, x - 1, barY - 1, x + width + 1, barY + barHeight + 1,
                                CultivationTheme.BORDER_DIM);
        }

        @Override
        public void setPosition(int x, int y) {
                this.x = x;
                this.y = y;
        }

        @Override
        public int getX() {
                return x;
        }

        @Override
        public int getY() {
                return y;
        }

        @Override
        public int getWidth() {
                return width;
        }

        @Override
        public int getHeight() {
                return showText ? barHeight + 12 : barHeight;
        }
}
