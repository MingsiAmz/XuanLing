package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

import com.mingsiamz.xuanlingmod.util.gui.layout.ColumnLayout;
import com.mingsiamz.xuanlingmod.util.gui.layout.Layout;

public class GuiComponentGroup implements IGuiComponent {

        private int x, y;
        private int padding = 0;
        private int fixedWidth = -1;
        private int fixedHeight = -1;

        private Layout layout = new ColumnLayout();
        private boolean enabled = true;
        private boolean visible = true;
        private final List<IGuiComponent> children = new ArrayList<>();

        public GuiComponentGroup setLayout(Layout layout) {
                this.layout = layout;
                return this;
        }

        public Layout getLayout() {
                return layout;
        }

        public GuiComponentGroup setPadding(int padding) {
                this.padding = padding;
                return this;
        }

        public GuiComponentGroup setFixedWidth(int w) {
                this.fixedWidth = w;
                return this;
        }

        public GuiComponentGroup setFixedHeight(int h) {
                this.fixedHeight = h;
                return this;
        }

        public GuiComponentGroup setEnabled(boolean e) {
                this.enabled = e;
                return this;
        }

        public GuiComponentGroup setVisible(boolean v) {
                this.visible = v;
                return this;
        }

        public GuiComponentGroup add(IGuiComponent c) {
                children.add(c);
                return this;
        }

        public GuiComponentGroup addAll(IGuiComponent... cs) {
                for (var c : cs)
                        children.add(c);
                return this;
        }

        public GuiComponentGroup clear() {
                children.clear();
                return this;
        }

        public List<IGuiComponent> getChildren() {
                return children;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                if (!visible)
                        return;

                int cx = x + padding;
                int cy = y + padding;
                int cw = getWidth() - padding * 2;
                int ch = getHeight() - padding * 2;

                layout.apply(cx, cy, cw, ch, children);

                for (IGuiComponent c : children) {
                        c.render(g, mouseX, mouseY, partialTick);
                }
        }

        @Override
        public boolean mouseClicked(double mx, double my, int b) {
                if (!enabled || !visible)
                        return false;
                for (int i = children.size() - 1; i >= 0; i--)
                        if (children.get(i).isMouseOver(mx, my) && children.get(i).mouseClicked(mx, my, b))
                                return true;
                return false;
        }

        @Override
        public boolean mouseReleased(double mx, double my, int b) {
                if (!enabled || !visible)
                        return false;
                for (int i = children.size() - 1; i >= 0; i--)
                        if (children.get(i).mouseReleased(mx, my, b))
                                return true;
                return false;
        }

        @Override
        public boolean mouseDragged(double mx, double my, int b, double dx, double dy) {
                if (!enabled || !visible)
                        return false;
                for (int i = children.size() - 1; i >= 0; i--)
                        if (children.get(i).mouseDragged(mx, my, b, dx, dy))
                                return true;
                return false;
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (!enabled || !visible || !isMouseOver(mx, my))
                        return false;
                for (int i = children.size() - 1; i >= 0; i--)
                        if (children.get(i).mouseScrolled(mx, my, sx, sy))
                                return true;
                return false;
        }

        @Override
        public boolean keyPressed(int kc, int sc, int m) {
                if (!enabled || !visible)
                        return false;
                for (var c : children)
                        if (c.keyPressed(kc, sc, m))
                                return true;
                return false;
        }

        @Override
        public boolean charTyped(char cp, int m) {
                if (!enabled || !visible)
                        return false;
                for (var c : children)
                        if (c.charTyped(cp, m))
                                return true;
                return false;
        }

        @Override
        public int getWidth() {
                return fixedWidth >= 0 ? fixedWidth : layout.measureWidth(children) + padding * 2;
        }

        @Override
        public int getHeight() {
                return fixedHeight >= 0 ? fixedHeight : layout.measureHeight(children) + padding * 2;
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
}
