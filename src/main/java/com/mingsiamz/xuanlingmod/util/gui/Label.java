package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import java.util.List;

public class Label implements IGuiComponent {

        public enum Align {
                LEFT, CENTER, RIGHT
        }

        private Component text;
        @Nullable
        private List<Component> tooltip;

        private int x, y;
        private int width = -1;
        private int height = 12;
        private Align align = Align.LEFT;

        private int color = CultivationTheme.TEXT_PRIMARY;
        private boolean shadow = false;
        private float scale = 1.0f;

        private boolean prefixDiamond = false;
        private int prefixColor = CultivationTheme.BORDER_CYAN;
        private int prefixSize = 3;

        private boolean underline = false;
        private int underlineColor = CultivationTheme.BORDER_DIM;
        private int underlineGap = 2;

        private boolean hoverHighlight = false;
        private int hoverColor = CultivationTheme.BORDER_GOLD;
        private boolean hovered;

        private boolean typewriter = false;
        private long typewriterStart = 0L;
        private long typewriterSpeed = 40L;

        public Label(String text, Object... args) {
                this(XuanLing.trans(XuanlingLangPro.GUI_STRING, text, args));
        }

        public Label(Component text) {
                this.text = text;
        }

        public Label(String text) {
                this.text = Component.literal(text);
        }

        public Label setText(String text) {
                this.text = Component.literal(text);
                return this;
        }

        public Label setText(Component text) {
                this.text = text;
                return this;
        }

        public Label setTooltip(List<Component> t) {
                this.tooltip = t;
                return this;
        }

        public Label setColor(int color) {
                this.color = color;
                return this;
        }

        public Label setShadow(boolean shadow) {
                this.shadow = shadow;
                return this;
        }

        public Label setScale(float scale) {
                this.scale = scale;
                return this;
        }

        public Label setAlign(Align align) {
                this.align = align;
                return this;
        }

        public Label setWidth(int width) {
                this.width = width;
                return this;
        }

        public Label setHeight(int height) {
                this.height = height;
                return this;
        }

        public Label setPrefixDiamond(boolean en) {
                this.prefixDiamond = en;
                return this;
        }

        public Label setPrefixColor(int color) {
                this.prefixColor = color;
                return this;
        }

        public Label setPrefixSize(int size) {
                this.prefixSize = size;
                return this;
        }

        public Label setUnderline(boolean en) {
                this.underline = en;
                return this;
        }

        public Label setUnderlineColor(int color) {
                this.underlineColor = color;
                return this;
        }

        public Label setUnderlineGap(int gap) {
                this.underlineGap = gap;
                return this;
        }

        public Label setHoverHighlight(boolean en) {
                this.hoverHighlight = en;
                return this;
        }

        public Label setHoverColor(int color) {
                this.hoverColor = color;
                return this;
        }

        public Label setTypewriter(boolean en) {
                this.typewriter = en;
                this.typewriterStart = System.currentTimeMillis();
                return this;
        }

        public Label setTypewriterSpeed(long msPerChar) {
                this.typewriterSpeed = msPerChar;
                return this;
        }

        public Label restartTypewriter() {
                this.typewriterStart = System.currentTimeMillis();
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                Font font = Minecraft.getInstance().font;

                Component display = text;
                if (typewriter) {
                        String full = text.getString();
                        long elapsed = System.currentTimeMillis() - typewriterStart;
                        int chars = (int) Math.min(full.length(), elapsed / typewriterSpeed);
                        display = Component.literal(full.substring(0, chars));
                }

                int textW = font.width(display);
                int boxW = (width >= 0) ? width : textW;

                int baseX = switch (align) {
                        case CENTER -> x + (boxW - textW) / 2;
                        case RIGHT -> x + boxW - textW;
                        default -> x;
                };

                int prefixOffset = prefixDiamond ? (prefixSize * 2 + 4) : 0;

                int textY = y + (height - 8) / 2;

                this.hovered = isMouseOver(mouseX, mouseY);
                int finalColor = (hoverHighlight && hovered) ? hoverColor : color;

                if (prefixDiamond) {
                        GuiRenderUtil.diamond(g, x + prefixSize, textY + 3,
                                        prefixSize, prefixColor);
                }

                g.drawString(font, display, baseX + prefixOffset, textY, finalColor, shadow);

                if (underline) {
                        int ux = baseX + prefixOffset;
                        int uy = textY + 10 + underlineGap;
                        g.fill(ux, uy, ux + textW, uy + 1, underlineColor);
                }
        }

        public boolean isHovered() {
                return hovered;
        }

        @Nullable
        public List<Component> getTooltip() {
                return tooltip;
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
                if (width >= 0)
                        return width;
                int w = Minecraft.getInstance().font.width(text);
                if (prefixDiamond)
                        w += prefixSize * 2 + 4;
                return w;
        }

        @Override
        public int getHeight() {
                return height;
        }
}
