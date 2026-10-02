package com.mingsiamz.xuanlingmod.gui.component.canvas;

import net.minecraft.client.gui.GuiGraphics;

public class Connection {
        private final CanvasNode from, to;
        private final int color;

        public Connection(CanvasNode from, CanvasNode to, int color) {
                this.from = from;
                this.to = to;
                this.color = color;
        }

        public CanvasNode getFrom() {
                return from;
        }

        public CanvasNode getTo() {
                return to;
        }

        /** 传入两个端点的屏幕坐标 */
        public void render(GuiGraphics g, int x1, int y1, int x2, int y2) {
                int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1);
                int sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1;
                int err = dx - dy;
                while (true) {
                        g.fill(x1, y1, x1 + 1, y1 + 1, color);
                        if (x1 == x2 && y1 == y2)
                                break;
                        int e2 = 2 * err;
                        if (e2 > -dy) {
                                err -= dy;
                                x1 += sx;
                        }
                        if (e2 < dx) {
                                err += dx;
                                y1 += sy;
                        }
                }
        }
}
