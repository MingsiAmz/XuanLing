package com.mingsiamz.xuanlingmod.gui.component.canvas;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.gui.component.data.ComponentDef;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CanvasPanel {
        private final EditorState state;

        private int x, y, width, height;

        private CanvasNode draggingNode;
        private int dragOffsetX, dragOffsetY;

        private boolean panning;
        private int panStartMX, panStartMY, panStartX, panStartY;

        private CanvasNode connectSource;

        public CanvasPanel(EditorState state, int w, int h) {
                this.state = state;
                this.width = w;
                this.height = h;
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

        public int getPanX() {
                return state.panX;
        }

        public int getPanY() {
                return state.panY;
        }

        public void resize(int w, int h) {
                this.width = w;
                this.height = h;
        }

        public boolean contains(double mx, double my) {
                return mx >= x && mx < x + width && my >= y && my < y + height;
        }

        public void addNode(IComponent def, int canvasX, int canvasY) {
                state.nodes.add(new CanvasNode(def, canvasX, canvasY));
        }

        public int toScreenX(int cx) {
                return x + cx + state.panX;
        }

        public int toScreenY(int cy) {
                return y + cy + state.panY;
        }

        public int toCanvasX(int sx) {
                return sx - x - state.panX;
        }

        public int toCanvasY(int sy) {
                return sy - y - state.panY;
        }

        public void centerOnContent() {
                if (state.nodes.isEmpty()) {
                        state.panX = 0;
                        state.panY = 0;
                        return;
                }

                int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
                int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
                for (CanvasNode n : state.nodes) {
                        minX = Math.min(minX, n.getCanvasX());
                        minY = Math.min(minY, n.getCanvasY());
                        maxX = Math.max(maxX, n.getCanvasX() + n.getWidth());
                        maxY = Math.max(maxY, n.getCanvasY() + n.getHeight());
                }

                int contentCenterX = (minX + maxX) / 2;
                int contentCenterY = (minY + maxY) / 2;

                state.panX = width / 2 - contentCenterX;
                state.panY = height / 2 - contentCenterY;
        }

        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                g.fill(x, y, x + width, y + height, 0xF005080C);

                g.enableScissor(x + 1, y + 1, x + width - 1, y + height - 1);

                int step = 24;
                int offX = ((state.panX % step) + step) % step;
                int offY = ((state.panY % step) + step) % step;
                for (int gx = x + offX; gx < x + width; gx += step)
                        g.fill(gx, y, gx + 1, y + height, 0x1400E5FF);
                for (int gy = y + offY; gy < y + height; gy += step)
                        g.fill(x, gy, x + width, gy + 1, 0x1400E5FF);

                for (Connection c : state.connections) {
                        CanvasNode f = c.getFrom(), t = c.getTo();
                        int fx = toScreenX(f.getCanvasX() + f.getWidth());
                        int fy = toScreenY(f.getCanvasY() + f.getHeight() / 2);
                        int tx = toScreenX(t.getCanvasX());
                        int ty = toScreenY(t.getCanvasY() + t.getHeight() / 2);
                        c.render(g, fx, fy, tx, ty);
                }

                for (CanvasNode n : state.nodes) {
                        int sx = toScreenX(n.getCanvasX());
                        int sy = toScreenY(n.getCanvasY());
                        n.render(g, sx, sy, mouseX, mouseY);
                }

                g.disableScissor();

                GuiRenderUtil.border(g, x, y, x + width, y + height, CultivationTheme.BORDER_CYAN);

                var font = Minecraft.getInstance().font;
                Component tips = XuanLing.trans(XuanlingLangPro.GUI_STRING, "make_tips");
                g.drawString(font, tips, x + 6, y + height - 12, CultivationTheme.TEXT_SECONDARY, false);
        }

        public boolean mouseClicked(double mx, double my, int button) {
                if (!contains(mx, my))
                        return false;

                if (button == 2) {
                        centerOnContent();
                        return true;
                }

                boolean shift = Screen.hasShiftDown();
                boolean ctrl = Screen.hasControlDown();

                for (int i = state.nodes.size() - 1; i >= 0; i--) {
                        CanvasNode n = state.nodes.get(i);
                        int sx = toScreenX(n.getCanvasX());
                        int sy = toScreenY(n.getCanvasY());
                        if (!n.hitTest(sx, sy, mx, my))
                                continue;

                        if (shift) {
                                state.nodes.remove(i);
                                state.connections.removeIf(c -> c.getFrom() == n || c.getTo() == n);
                                return true;
                        }
                        if (ctrl) {
                                if (connectSource == null) {
                                        connectSource = n;
                                        n.setSelected(true);
                                } else if (connectSource == n) {
                                        n.setSelected(false);
                                        connectSource = null;
                                } else {
                                        boolean exists = false;
                                        for (Connection c : state.connections) {
                                                if ((c.getFrom() == connectSource && c.getTo() == n)
                                                                || (c.getFrom() == n && c.getTo() == connectSource)) {
                                                        exists = true;
                                                        break;
                                                }
                                        }
                                        if (!exists)
                                                state.connections.add(new Connection(connectSource, n, 0xFF4DD0E1));
                                        connectSource.setSelected(false);
                                        connectSource = null;
                                }
                                return true;
                        }
                        if (button == 0) {
                                draggingNode = n;
                                dragOffsetX = (int) mx - sx;
                                dragOffsetY = (int) my - sy;
                                return true;
                        }
                }

                if (button == 0) {
                        panning = true;
                        panStartMX = (int) mx;
                        panStartMY = (int) my;
                        panStartX = state.panX;
                        panStartY = state.panY;
                        return true;
                }
                return true;
        }

        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (draggingNode != null && button == 0) {
                        int newSX = (int) mx - dragOffsetX;
                        int newSY = (int) my - dragOffsetY;
                        draggingNode.setCanvasPosition(toCanvasX(newSX), toCanvasY(newSY));
                        return true;
                }
                if (panning && button == 0) {
                        state.panX = panStartX + ((int) mx - panStartMX);
                        state.panY = panStartY + ((int) my - panStartMY);
                        return true;
                }
                return false;
        }

        public boolean mouseReleased(double mx, double my, int button) {
                if (draggingNode != null) {
                        draggingNode = null;
                        return true;
                }
                if (panning) {
                        panning = false;
                        return true;
                }
                return false;
        }
}
