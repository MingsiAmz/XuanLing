package com.mingsiamz.xuanlingmod.gui.component.canvas;

import java.util.ArrayList;
import java.util.List;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class CanvasNode {
        private final ImpComponent def;
        private final String groupName;
        private int canvasX, canvasY;
        private boolean selected;
        private Runnable onConfigClick;

        public final List<CanvasNode> children = new ArrayList<>();

        public CanvasNode(String groupName, int x, int y) {
                this.groupName = groupName;
                this.def = null;
                this.canvasX = x;
                this.canvasY = y;
        }

        public CanvasNode(ImpComponent def, int x, int y) {
                this.def = def;
                this.groupName = null;
                this.canvasX = x;
                this.canvasY = y;
        }

        public boolean isGroup() {
                return groupName != null;
        }

        public ImpComponent getDef() {
                return def;
        }

        public int getCanvasX() {
                return canvasX;
        }

        public int getCanvasY() {
                return canvasY;
        }

        public int getWidth() {
                if (!isGroup())
                        return 110;
                int maxChildW = 0;
                for (CanvasNode child : children)
                        maxChildW = Math.max(maxChildW, child.getWidth());
                return Math.max(140, maxChildW + 24);
        }

        public int getHeight() {
                if (!isGroup())
                        return 46;
                int totalChildH = 0;
                for (CanvasNode child : children)
                        totalChildH += child.getHeight() + 5;
                return 30 + totalChildH + 10;
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

        public void setOnConfigClick(Runnable r) {
                this.onConfigClick = r;
        }

        public void render(GuiGraphics g, int sx, int sy, int mouseX, int mouseY, boolean isInsideGroup) {
                int w = getWidth(), h = getHeight();

                int highlightColor = 0xFFFFE082;
                if (selected) {
                        float pulse = (float) (Math.sin(System.currentTimeMillis() / 150.0) * 0.3f + 0.7f);
                        int alpha = (int) (pulse * 255) << 24;
                        highlightColor = alpha | (0xFFFFE082 & 0x00FFFFFF);
                }

                if (isGroup()) {
                        g.fill(sx, sy, sx + w, sy + h, 0x800A1218);
                        GuiRenderUtil.border(g, sx, sy, sx + w, sy + h, CultivationTheme.BORDER_GOLD);

                        Font font = Minecraft.getInstance().font;
                        g.fill(sx, sy, sx + w, sy + 16, 0x40FFC94D);
                        g.drawString(font, groupName, sx + 6, sy + 4, CultivationTheme.TEXT_GOLD, false);

                        GuiRenderUtil.diamond(g, sx + 5, sy + h / 2, 2, CultivationTheme.BORDER_GOLD);
                        GuiRenderUtil.diamond(g, sx + w - 5, sy + h / 2, 2, CultivationTheme.BORDER_GOLD);

                        if (selected) {
                                GuiRenderUtil.border(g, sx - 2, sy - 2, sx + w + 2, sy + h + 2, highlightColor);
                                GuiRenderUtil.border(g, sx - 1, sy - 1, sx + w + 1, sy + h + 1, highlightColor);
                        }

                        int cy = sy + 22;
                        for (CanvasNode child : children) {
                                int childSx = sx + (w - child.getWidth()) / 2;
                                child.render(g, childSx, cy, mouseX, mouseY, true);
                                cy += child.getHeight() + 5;
                        }
                        return;
                }

                g.fill(sx + 2, sy + 2, sx + w + 2, sy + h + 2, 0x40000000);
                g.fill(sx, sy, sx + w, sy + h, 0xF0101820);
                g.fill(sx, sy, sx + w, sy + 12, 0x5000E5FF);
                GuiRenderUtil.border(g, sx, sy, sx + w, sy + h, CultivationTheme.BORDER_CYAN);

                Font font = Minecraft.getInstance().font;
                if (def != null) {
                        g.drawString(font, def.trans(), sx + 6, sy + 3, CultivationTheme.TEXT_PRIMARY, false);
                }

                int btnX = sx + w - 14;
                int btnY = sy + 2;
                boolean hoverBtn = mouseX >= btnX && mouseX <= btnX + 12 && mouseY >= btnY && mouseY <= btnY + 10;
                g.fill(btnX, btnY, btnX + 12, btnY + 10, hoverBtn ? 0xCC00E5FF : 0x4000E5FF);
                g.drawString(font, Component.literal("⚙"), btnX + 3, btnY + 1, 0xFFFFFFFF, false);

                if (!isInsideGroup) {
                        GuiRenderUtil.diamond(g, sx + 5, sy + h / 2, 2, CultivationTheme.BORDER_CYAN);
                        GuiRenderUtil.diamond(g, sx + w - 5, sy + h / 2, 2, CultivationTheme.BORDER_CYAN);
                }

                if (selected) {
                        GuiRenderUtil.border(g, sx - 2, sy - 2, sx + w + 2, sy + h + 2, highlightColor);
                        GuiRenderUtil.border(g, sx - 1, sy - 1, sx + w + 1, sy + h + 1, highlightColor);
                }
        }

        public boolean hitTest(int nx, int ny, double cx, double cy) {
                return cx >= nx && cx < nx + getWidth() && cy >= ny && cy < ny + getHeight();
        }

        public boolean hitConfigButton(int nx, int ny, double cx, double cy) {
                if (isGroup())
                        return false;
                int btnX = nx + getWidth() - 14;
                int btnY = ny + 2;
                return cx >= btnX && cx <= btnX + 12 && cy >= btnY && cy <= btnY + 10;
        }

        public void triggerConfigClick() {
                if (onConfigClick != null && !isGroup()) {
                        onConfigClick.run();
                }
        }

        public String getGroupName() {
                return groupName;
        }
}
