package com.mingsiamz.xuanlingmod.gui.weight;

import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class GuiButton implements IGuiComponent {

        private int x, y, width, height;
        private String text;
        private Runnable onClick;
        private boolean hovered;

        private int bgColor = 0x3000E5FF;
        private int hoverBgColor = 0x7000E5FF;
        private int borderColor = CultivationTheme.BORDER_CYAN;
        private int hoverBorder = CultivationTheme.BORDER_GOLD;
        private int textColor = CultivationTheme.TEXT_PRIMARY;
        private int hoverTextColor = CultivationTheme.TEXT_GOLD;

        public GuiButton(int width, int height, String text) {
                this.width = width;
                this.height = height;
                this.text = text;
        }

        public GuiButton onClick(Runnable action) {
                this.onClick = action;
                return this;
        }

        public GuiButton setColors(int bg, int hoverBg, int border, int hoverBorder) {
                this.bgColor = bg;
                this.hoverBgColor = hoverBg;
                this.borderColor = border;
                this.hoverBorder = hoverBorder;
                return this;
        }

        public GuiButton setTextColors(int normal, int hover) {
                this.textColor = normal;
                this.hoverTextColor = hover;
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                this.hovered = isMouseOver(mouseX, mouseY);
                int bg = hovered ? hoverBgColor : bgColor;
                int brd = hovered ? hoverBorder : borderColor;
                int fg = hovered ? hoverTextColor : textColor;

                g.fill(x, y, x + width, y + height, bg);
                GuiRenderUtil.border(g, x, y, x + width, y + height, brd);

                Font font = Minecraft.getInstance().font;
                g.drawString(font, Component.literal(text),
                                x + (width - font.width(text)) / 2,
                                y + (height - 8) / 2,
                                fg, false);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (button == 0 && isMouseOver(mx, my)) {
                        if (onClick != null)
                                onClick.run();
                        return true;
                }
                return false;
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
