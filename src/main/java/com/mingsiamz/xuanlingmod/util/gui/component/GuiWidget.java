package com.mingsiamz.xuanlingmod.util.gui.component;

import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;
import com.mingsiamz.xuanlingmod.util.gui.theme.ColorPalette;
import com.mingsiamz.xuanlingmod.util.gui.theme.GuiTheme;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public abstract class GuiWidget implements IGuiComponent {

        protected int x;
        protected int y;
        protected int width;
        protected int height;
        protected boolean visible = true;
        protected boolean enabled = true;
        protected boolean hovered = false;

        protected ColorPalette palette = GuiTheme.active();

        protected GuiWidget(int width, int height) {
                this.width = width;
                this.height = height;
        }

        protected <T extends GuiWidget> T self() {
                return (T) this;
        }

        public GuiWidget setPalette(ColorPalette p) {
                this.palette = (p != null) ? p : GuiTheme.active();
                return this;
        }

        public ColorPalette palette() {
                return palette;
        }

        public GuiWidget setVisible(boolean v) {
                this.visible = v;
                return this;
        }

        public boolean isVisible() {
                return visible;
        }

        public GuiWidget setEnabled(boolean e) {
                this.enabled = e;
                return this;
        }

        public boolean isHovered() {
                return hovered;
        }

        protected static Font font() {
                return Minecraft.getInstance().font;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                if (!visible)
                        return;
                this.hovered = isMouseOver(mouseX, mouseY);
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
