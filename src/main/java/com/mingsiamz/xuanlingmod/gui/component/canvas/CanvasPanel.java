package com.mingsiamz.xuanlingmod.gui.component.canvas;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class CanvasPanel {
        private final EditorState state;

        private int x, y, width, height;

        private CanvasNode draggingNode;
        private float dragOffsetX, dragOffsetY;

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

        public int toScreenX(float cx) {
                return x + (int) ((cx + state.panX) * state.scale);
        }

        public int toScreenY(float cy) {
                return y + (int) ((cy + state.panY) * state.scale);
        }

        public float toCanvasX(double sx) {
                return (float) ((sx - x) / state.scale) - state.panX;
        }

        public float toCanvasY(double sy) {
                return (float) ((sy - y) / state.scale) - state.panY;
        }

        public void centerOnContent() {
                if (state.nodes.isEmpty()) {
                        state.panX = 0;
                        state.panY = 0;
                        return;
                }

                int minX = Integer.MAX_VALUE;
                int minY = Integer.MAX_VALUE;
                int maxX = Integer.MIN_VALUE;
                int maxY = Integer.MIN_VALUE;

                for (CanvasNode n : state.nodes) {
                        minX = Math.min(minX, n.getCanvasX());
                        minY = Math.min(minY, n.getCanvasY());
                        maxX = Math.max(maxX, n.getCanvasX() + n.getWidth());
                        maxY = Math.max(maxY, n.getCanvasY() + n.getHeight());
                }

                float contentCenterX = (minX + maxX) / 2f;
                float contentCenterY = (minY + maxY) / 2f;

                state.panX = (int) ((width / 2f / state.scale) - contentCenterX);
                state.panY = (int) ((height / 2f / state.scale) - contentCenterY);
        }

        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                g.fill(x, y, x + width, y + height, 0xF005080C);

                g.enableScissor(x + 1, y + 1, x + width - 1, y + height - 1);

                g.pose().pushPose();
                g.pose().translate(x, y, 0);
                g.pose().scale(state.scale, state.scale, 1f);
                g.pose().translate(state.panX, state.panY, 0);

                float mouseCanvasX = toCanvasX(mouseX);
                float mouseCanvasY = toCanvasY(mouseY);

                for (Connection c : state.connections) {
                        CanvasNode f = c.getFrom(), t = c.getTo();
                        int fx = f.getCanvasX() + f.getWidth();
                        int fy = f.getCanvasY() + f.getHeight() / 2;
                        int tx = t.getCanvasX();
                        int ty = t.getCanvasY() + t.getHeight() / 2;
                        c.render(g, fx, fy, tx, ty);
                }

                for (CanvasNode n : state.nodes) {
                        n.render(g, n.getCanvasX(), n.getCanvasY(), (int) mouseCanvasX, (int) mouseCanvasY, false);
                }

                g.pose().popPose();
                g.disableScissor();

                GuiRenderUtil.border(g, x, y, x + width, y + height, CultivationTheme.BORDER_CYAN);

                var font = Minecraft.getInstance().font;
                Component tips = XuanLing.trans(XuanlingLangPro.GUI_STRING, "make_tips");
                g.drawString(font, Component.literal(String.format("Scale: %d%%", (int) (state.scale * 100))), x + 6,
                                y + height - 22, CultivationTheme.TEXT_SECONDARY, false);
                g.drawString(font, tips, x + 6, y + height - 12, CultivationTheme.TEXT_SECONDARY, false);
        }

        public boolean mouseClicked(double mx, double my, int button) {
                if (!contains(mx, my))
                        return false;

                if (button == 2) {
                        centerOnContent();
                        return true;
                }

                if (button == 1 && connectSource != null) {
                        connectSource.setSelected(false);
                        connectSource = null;
                        return true;
                }

                boolean shift = Screen.hasShiftDown();
                boolean ctrl = Screen.hasControlDown();

                float cx = toCanvasX(mx);
                float cy = toCanvasY(my);

                for (int i = state.nodes.size() - 1; i >= 0; i--) {
                        CanvasNode n = state.nodes.get(i);
                        int nx = n.getCanvasX();
                        int ny = n.getCanvasY();

                        if (n.hitTest(nx, ny, cx, cy)) {
                                if (button == 0 && n.isGroup()) {
                                        for (int j = n.children.size() - 1; j >= 0; j--) {
                                                CanvasNode child = n.children.get(j);
                                                int childSx = nx + (n.getWidth() - child.getWidth()) / 2;
                                                int childSy = ny + 22 + getChildYOffset(n, j);

                                                if (child.hitTest(childSx, childSy, cx, cy)
                                                                && child.hitConfigButton(childSx, childSy, cx, cy)) {
                                                        child.triggerConfigClick();
                                                        return true;
                                                }
                                        }
                                } else if (button == 0 && !n.isGroup()) {
                                        if (n.hitConfigButton(nx, ny, cx, cy)) {
                                                n.triggerConfigClick();
                                                return true;
                                        }
                                }

                                if (shift) {
                                        state.nodes.remove(i);
                                        state.connections.removeIf(c -> c.getFrom() == n || c.getTo() == n);
                                        if (connectSource == n) {
                                                n.setSelected(false);
                                                connectSource = null;
                                        }
                                        return true;
                                }

                                if (ctrl) {
                                        if (connectSource == null) {
                                                connectSource = n;
                                                n.setSelected(true);
                                        } else {
                                                if (connectSource != n) {
                                                        boolean exists = false;
                                                        for (Connection c : state.connections) {
                                                                if ((c.getFrom() == connectSource && c.getTo() == n) ||
                                                                                (c.getFrom() == n && c
                                                                                                .getTo() == connectSource)) {
                                                                        exists = true;
                                                                        break;
                                                                }
                                                        }
                                                        if (!exists) {
                                                                state.connections.add(new Connection(connectSource, n,
                                                                                CultivationTheme.TEXT_SECONDARY));
                                                        }
                                                }
                                                connectSource.setSelected(false);
                                                connectSource = null;
                                        }
                                        return true;
                                }

                                if (button == 0) {
                                        if (connectSource != null) {
                                                connectSource.setSelected(false);
                                                connectSource = null;
                                        }

                                        if (n.isGroup()) {
                                                for (int j = n.children.size() - 1; j >= 0; j--) {
                                                        CanvasNode child = n.children.get(j);
                                                        int childSx = nx + (n.getWidth() - child.getWidth()) / 2;
                                                        int childSy = ny + 22 + getChildYOffset(n, j);

                                                        if (child.hitTest(childSx, childSy, cx, cy)) {
                                                                n.children.remove(j);
                                                                child.setCanvasPosition(childSx, childSy);
                                                                state.nodes.add(child);

                                                                draggingNode = child;
                                                                dragOffsetX = cx - childSx;
                                                                dragOffsetY = cy - childSy;
                                                                return true;
                                                        }
                                                }
                                        }

                                        draggingNode = n;
                                        dragOffsetX = cx - nx;
                                        dragOffsetY = cy - ny;
                                        return true;
                                }
                                return true;
                        }
                }

                if (button == 0) {
                        if (connectSource != null) {
                                connectSource.setSelected(false);
                                connectSource = null;
                        }

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
                        float cx = toCanvasX(mx);
                        float cy = toCanvasY(my);
                        draggingNode.setCanvasPosition((int) (cx - dragOffsetX), (int) (cy - dragOffsetY));
                        return true;
                }
                if (panning && button == 0) {
                        float panDeltaX = (float) ((mx - panStartMX) / state.scale);
                        float panDeltaY = (float) ((my - panStartMY) / state.scale);
                        state.panX = (int) (panStartX + panDeltaX);
                        state.panY = (int) (panStartY + panDeltaY);
                        return true;
                }
                return false;
        }

        public boolean mouseReleased(double mx, double my, int button) {
                if (draggingNode != null) {
                        if (!contains(mx, my)) {
                                state.nodes.remove(draggingNode);
                                state.connections.removeIf(
                                                c -> c.getFrom() == draggingNode || c.getTo() == draggingNode);
                        } else {
                                float cx = toCanvasX(mx);
                                float cy = toCanvasY(my);
                                CanvasNode targetGroup = null;
                                int magnet = 20;

                                for (int i = state.nodes.size() - 1; i >= 0; i--) {
                                        CanvasNode n = state.nodes.get(i);
                                        if (n == draggingNode || !n.isGroup())
                                                continue;

                                        int nx = n.getCanvasX();
                                        int ny = n.getCanvasY();
                                        int expandW = n.getWidth() + magnet * 2;
                                        int expandH = n.getHeight() + magnet * 2;

                                        if (n.hitTest(nx, ny, cx, cy) ||
                                                        intersects((int) cx - 55, (int) cy - 23, 110, 46, nx - magnet,
                                                                        ny - magnet, expandW, expandH)) {
                                                targetGroup = n;
                                                break;
                                        }
                                }

                                if (targetGroup != null && draggingNode.getDef() != null) {
                                        String groupType = targetGroup.getGroupName();
                                        String compType = draggingNode.getDef().transType().getString();

                                        if (groupType.equals(compType)) {
                                                state.nodes.remove(draggingNode);
                                                draggingNode.setCanvasPosition(0, 0);
                                                targetGroup.children.add(draggingNode);

                                                for (int i = 0; i < state.connections.size(); i++) {
                                                        Connection c = state.connections.get(i);
                                                        CanvasNode newFrom = c.getFrom() == draggingNode ? targetGroup
                                                                        : c.getFrom();
                                                        CanvasNode newTo = c.getTo() == draggingNode ? targetGroup
                                                                        : c.getTo();

                                                        if (newFrom != c.getFrom() || newTo != c.getTo()) {
                                                                if (newFrom != newTo) {
                                                                        boolean exists = false;
                                                                        for (Connection oldC : state.connections) {
                                                                                if (oldC != c && ((oldC
                                                                                                .getFrom() == newFrom
                                                                                                && oldC.getTo() == newTo)
                                                                                                ||
                                                                                                (oldC.getFrom() == newTo
                                                                                                                && oldC.getTo() == newFrom))) {
                                                                                        exists = true;
                                                                                        break;
                                                                                }
                                                                        }
                                                                        if (exists) {
                                                                                state.connections.remove(i);
                                                                                i--;
                                                                        } else {
                                                                                state.connections.set(i, new Connection(
                                                                                                newFrom, newTo,
                                                                                                CultivationTheme.TEXT_SECONDARY));
                                                                        }
                                                                } else {
                                                                        state.connections.remove(i);
                                                                        i--;
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                        draggingNode = null;
                        return true;
                }

                if (panning) {
                        panning = false;
                        return true;
                }
                return false;
        }

        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (!contains(mx, my))
                        return false;

                float oldScale = state.scale;
                state.scale = Mth.clamp(state.scale + (float) sy * 0.1f, 0.5f, 2.5f);

                if (state.scale != oldScale) {
                        float oldCx = (float) ((mx - x) / oldScale) - state.panX;
                        float oldCy = (float) ((my - y) / oldScale) - state.panY;

                        state.panX = (int) (((mx - x) / state.scale) - oldCx);
                        state.panY = (int) (((my - y) / state.scale) - oldCy);
                }
                return true;
        }

        private int getChildYOffset(CanvasNode parent, int index) {
                int offset = 0;
                for (int i = 0; i < index; i++) {
                        offset += parent.children.get(i).getHeight() + 5;
                }
                return offset;
        }

        private boolean intersects(int ax, int ay, int aw, int ah, int bx, int by, int bw, int bh) {
                return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
        }
}
