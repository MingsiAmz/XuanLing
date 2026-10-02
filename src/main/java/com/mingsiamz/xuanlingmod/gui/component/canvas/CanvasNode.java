package com.mingsiamz.xuanlingmod.gui.component.canvas;

import com.mingsiamz.xuanlingmod.gui.component.data.ComponentDef;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class CanvasNode {
        private final ComponentDef def;
        private int canvasX, canvasY;
        private boolean selected;

        public CanvasNode(ComponentDef def, int x, int y) {
                this.def = def;
                this.canvasX = x;
                this.canvasY = y;
        }

        public ComponentDef getDef() {
                return def;
        }

        public int getCanvasX() {
                return canvasX;
        }

        public int getCanvasY() {
                return canvasY;
        }

        public int getWidth() {
                return def.width();
        }

        public int getHeight() {
                return def.height();
        }

        public boolean isSelected() {
                return selected;
        }

        public void setSelected(boolean s) {
                this.selected = s;
        }

        public void setCanvasPosition(int x, int y) {
                this.canvasX = x;
                this.canvasY = y;
        }

        public void render(GuiGraphics g, int sx, int sy, int mouseX, int mouseY) {
                int w = def.width(), h = def.height();
                boolean hovered = mouseX >= sx && mouseX < sx + w && mouseY >= sy && mouseY < sy + h;

                g.fill(sx + 2, sy + 2, sx + w + 2, sy + h + 2, 0x40000000);
                g.fill(sx, sy, sx + w, sy + h, 0xF0101820);
                g.fill(sx, sy, sx + w, sy + 12, (def.color() & 0x00FFFFFF) | 0x50000000);
                GuiRenderUtil.border(g, sx, sy, sx + w, sy + h, def.color());

                if (selected) {
                        GuiRenderUtil.border(g, sx - 2, sy - 2, sx + w + 2, sy + h + 2, 0xFFFFC94D);
                        GuiRenderUtil.border(g, sx - 1, sy - 1, sx + w + 1, sy + h + 1, 0xFFFFC94D);
                } else if (hovered) {
                        GuiRenderUtil.border(g, sx - 1, sy - 1, sx + w + 1, sy + h + 1, CultivationTheme.BORDER_GOLD);
                }

                var font = Minecraft.getInstance().font;
                g.drawString(font, Component.literal(def.displayName()),
                                sx + 6, sy + 3, CultivationTheme.TEXT_PRIMARY, false);

                GuiRenderUtil.diamond(g, sx + 5, sy + h / 2, 2, def.color());
                GuiRenderUtil.diamond(g, sx + w - 5, sy + h / 2, 2, def.color());
        }

        public boolean hitTest(int sx, int sy, double mx, double my) {
                return mx >= sx && mx < sx + def.width() && my >= sy && my < sy + def.height();
        }
}
