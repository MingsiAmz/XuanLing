package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.gui.GuiGraphics;

public class CultivationPanel implements IGuiComponent {
        private int x, y, width, height;

        private boolean enableGlow = true;
        private boolean enableGrid = true;
        private boolean enableCorners = true;
        private int glowColor = CultivationTheme.AURA_CYAN;
        private int bgColor = CultivationTheme.PANEL_BG;
        private int borderOuterColor = CultivationTheme.BORDER_GOLD;
        private int borderInnerColor = CultivationTheme.BORDER_CYAN;

        public CultivationPanel(int width, int height) {
                this.width = width;
                this.height = height;
        }

        public CultivationPanel setStyle(boolean glow, boolean grid, boolean corners) {
                this.enableGlow = glow;
                this.enableGrid = grid;
                this.enableCorners = corners;
                return this;
        }

        public CultivationPanel setColors(int bg, int outerBorder, int innerBorder, int glow) {
                this.bgColor = bg;
                this.borderOuterColor = outerBorder;
                this.borderInnerColor = innerBorder;
                this.glowColor = glow;
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                int x1 = x, y1 = y, x2 = x + width, y2 = y + height;

                if (enableGlow) {
                        GuiRenderUtil.glow(g, x1, y1, x2, y2, glowColor, CultivationTheme.GLOW_LAYERS, 12);
                }
                g.fill(x1, y1, x2, y2, bgColor);

                if (enableGrid) {
                        GuiRenderUtil.grid(g, x1 + 3, y1 + 6, x2 - 3, y2 - 6, 10, 0x0800E5FF);
                }

                GuiRenderUtil.border(g, x1, y1, x2, y2, borderOuterColor);
                GuiRenderUtil.border(g, x1 + 2, y1 + 2, x2 - 2, y2 - 2, borderInnerColor);

                if (enableCorners) {
                        GuiRenderUtil.cornerAccents(g, x1, y1, x2, y2, 14, borderOuterColor);
                }
        }

        public int getInnerX() {
                return x + CultivationTheme.PADDING;
        }

        public int getInnerY() {
                return y + CultivationTheme.PADDING;
        }

        public int getInnerWidth() {
                return width - CultivationTheme.PADDING * 2;
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
                return height;
        }
}
