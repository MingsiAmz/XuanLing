package com.mingsiamz.xuanlingmod.gui.component.canvas;

import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class CanvasNode {
        private final IComponent def; // 这里是你的 IComponent 接口
        private int canvasX, canvasY;
        private boolean selected;

        // 配置按钮的回调
        private Runnable onConfigClick;

        public CanvasNode(IComponent def, int x, int y) {
                this.def = def;
                this.canvasX = x;
                this.canvasY = y;
        }

        public IComponent getDef() {
                return def;
        }

        public int getCanvasX() {
                return canvasX;
        }

        public int getCanvasY() {
                return canvasY;
        }

        // 暂时用固定大小，如果你 IComponent 里没有宽高，可以写死或扩展接口
        public int getWidth() {
                return 110;
        }

        public int getHeight() {
                return 46;
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

        public void render(GuiGraphics g, int sx, int sy, int mouseX, int mouseY) {
                int w = getWidth(), h = getHeight();
                boolean hovered = mouseX >= sx && mouseX < sx + w && mouseY >= sy && mouseY < sy + h;

                // ... 保持你原有的节点绘制 ...
                g.fill(sx + 2, sy + 2, sx + w + 2, sy + h + 2, 0x40000000);
                g.fill(sx, sy, sx + w, sy + h, 0xF0101820);
                g.fill(sx, sy, sx + w, sy + 12, 0x5000E5FF);
                GuiRenderUtil.border(g, sx, sy, sx + w, sy + h, CultivationTheme.BORDER_CYAN);

                var font = Minecraft.getInstance().font;
                g.drawString(font, def.trans(), sx + 6, sy + 3, CultivationTheme.TEXT_PRIMARY, false);

                // --- 绘制右上角的齿轮配置按钮 ---
                int btnX = sx + w - 14;
                int btnY = sy + 2;
                boolean hoverBtn = mouseX >= btnX && mouseX <= btnX + 12 && mouseY >= btnY && mouseY <= btnY + 10;
                g.fill(btnX, btnY, btnX + 12, btnY + 10, hoverBtn ? 0xCC00E5FF : 0x4000E5FF);
                g.drawString(font, Component.literal("⚙"), btnX + 3, btnY + 1, 0xFFFFFFFF, false);

                // 左右端口
                GuiRenderUtil.diamond(g, sx + 5, sy + h / 2, 2, CultivationTheme.BORDER_CYAN);
                GuiRenderUtil.diamond(g, sx + w - 5, sy + h / 2, 2, CultivationTheme.BORDER_CYAN);
        }

        public boolean hitTest(int sx, int sy, double mx, double my) {
                return mx >= sx && mx < sx + getWidth() && my >= sy && my < sy + getHeight();
        }

        // 检测是否点击了齿轮按钮
        public boolean hitConfigButton(int sx, int sy, double mx, double my) {
                int btnX = sx + getWidth() - 14;
                int btnY = sy + 2;
                return mx >= btnX && mx <= btnX + 12 && my >= btnY && my <= btnY + 10;
        }

        public void triggerConfigClick() {
                if (onConfigClick != null)
                        onConfigClick.run();
        }
}
