package com.mingsiamz.xuanlingmod.gui.component.palette;

import com.mingsiamz.xuanlingmod.gui.component.data.ComponentDef;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class PaletteItem {
        private final ComponentDef def;
        private int x, y;
        private final int width, height;

        private DragStart onDragStart;

        public PaletteItem(ComponentDef def, int width, int height) {
                this.def = def;
                this.width = width;
                this.height = height;
        }

        public ComponentDef getDef() {
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
                int bg = hovered ? 0x6000E5FF : 0x30101820;
                g.fill(x, y, x + width, y + height, bg);
                g.fill(x, y, x + 3, y + height, def.color());

                var font = Minecraft.getInstance().font;
                g.drawString(font, Component.literal(def.displayName()),
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
                void start(ComponentDef def, int mouseX, int mouseY);
        }
}
