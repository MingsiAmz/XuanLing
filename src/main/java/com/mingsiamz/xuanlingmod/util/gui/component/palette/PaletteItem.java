package com.mingsiamz.xuanlingmod.util.gui.component.palette;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class PaletteItem {
        private ImpComponent def;
        private int x, y;
        private final int width, height;

        private DragStart onDragStart;

        public PaletteItem(ImpComponent def, int width, int height) {
                this.def = def;
                this.width = width;
                this.height = height;
        }

        public Object getDef() {
                return def;
        }

        public PaletteItem setOnDragStart(DragStart l) {
                this.onDragStart = l;
                return this;
        }

        public void setPosition(int x, int y) {
                this.x = x;
                this.y = y;
        }

        public int getX() {
                return x;
        }

        public int getY() {
                return y;
        }

        public int getWidth() {
                return width;
        }

        public int getHeight() {
                return height;
        }

        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                boolean hovered = isMouseOver(mouseX, mouseY);
                int bg = hovered ? CultivationTheme.HOVER_CYAN : CultivationTheme.PALETTE_ITEM_BG;
                int color = CultivationTheme.TEXT_INFO;
                g.fill(x, y, x + width, y + height, bg);
                g.fill(x, y, x + 3, y + height, color);

                var font = Minecraft.getInstance().font;
                g.drawString(font, def.trans(),
                                x + 10, y + (height - 8) / 2,
                                hovered ? CultivationTheme.TEXT_GOLD : CultivationTheme.TEXT_PRIMARY, false);
        }

        public boolean isMouseOver(double mx, double my) {
                return mx >= x && mx < x + width && my >= y && my < y + height;
        }

        public boolean mouseClicked(double mx, double my, int button) {
                if (button == 0 && isMouseOver(mx, my)) {
                        if (onDragStart != null)
                                onDragStart.start(def, (int) mx, (int) my);
                        return true;
                }
                return false;
        }

        @FunctionalInterface
        public interface DragStart {
                void start(ImpComponent def, int mouseX, int mouseY);
        }
}
