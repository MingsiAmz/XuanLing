package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class GuiComponentGroup implements IGuiComponent {

        public enum Layout {
                VERTICAL, HORIZONTAL
        }

        private int x, y;
        private int padding = 0;
        private int spacing = 8;
        private Layout layout = Layout.VERTICAL;
        private boolean enabled = true;
        private boolean visible = true;
        private final List<IGuiComponent> children = new ArrayList<>();

        public GuiComponentGroup setSpacing(int spacing) {
                this.spacing = spacing;
                return this;
        }

        public GuiComponentGroup setPadding(int padding) {
                this.padding = padding;
                return this;
        }

        public GuiComponentGroup setLayout(Layout layout) {
                this.layout = layout;
                return this;
        }

        public GuiComponentGroup setEnabled(boolean enabled) {
                this.enabled = enabled;
                return this;
        }

        public GuiComponentGroup setVisible(boolean visible) {
                this.visible = visible;
                return this;
        }

        public GuiComponentGroup add(IGuiComponent component) {
                children.add(component);
                return this;
        }

        public GuiComponentGroup addAll(IGuiComponent... components) {
                for (IGuiComponent c : components)
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

                int originX = x + padding;
                int originY = y + padding;
                int cursor = (layout == Layout.VERTICAL) ? originY : originX;

                for (IGuiComponent child : children) {
                        if (layout == Layout.VERTICAL) {
                                child.setPosition(originX, cursor);
                                cursor += child.getHeight() + spacing;
                        } else {
                                child.setPosition(cursor, originY);
                                cursor += child.getWidth() + spacing;
                        }
                        child.render(g, mouseX, mouseY, partialTick);
                }
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (!enabled || !visible)
                        return false;
                for (int i = children.size() - 1; i >= 0; i--) {
                        IGuiComponent child = children.get(i);
                        if (child.isMouseOver(mx, my) && child.mouseClicked(mx, my, button)) {
                                return true;
                        }
                }
                return false;
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (!enabled || !visible)
                        return false;
                for (int i = children.size() - 1; i >= 0; i--) {
                        if (children.get(i).mouseReleased(mx, my, button))
                                return true;
                }
                return false;
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (!enabled || !visible)
                        return false;
                for (int i = children.size() - 1; i >= 0; i--) {
                        if (children.get(i).mouseDragged(mx, my, button, dx, dy))
                                return true;
                }
                return false;
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (!enabled || !visible)
                        return false;
                if (!isMouseOver(mx, my))
                        return false;
                for (int i = children.size() - 1; i >= 0; i--) {
                        if (children.get(i).mouseScrolled(mx, my, sx, sy))
                                return true;
                }
                return false;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (!enabled || !visible)
                        return false;
                for (IGuiComponent child : children) {
                        if (child.keyPressed(keyCode, scanCode, modifiers))
                                return true;
                }
                return false;
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
                if (!enabled || !visible)
                        return false;
                for (IGuiComponent child : children) {
                        if (child.charTyped(codePoint, modifiers))
                                return true;
                }
                return false;
        }

        @Override
        public int getWidth() {
                if (children.isEmpty())
                        return padding * 2;
                if (layout == Layout.HORIZONTAL) {
                        int total = 0;
                        for (int i = 0; i < children.size(); i++) {
                                total += children.get(i).getWidth();
                                if (i < children.size() - 1)
                                        total += spacing;
                        }
                        return total + padding * 2;
                }
                int max = 0;
                for (IGuiComponent c : children)
                        max = Math.max(max, c.getWidth());
                return max + padding * 2;
        }

        @Override
        public int getHeight() {
                if (children.isEmpty())
                        return padding * 2;
                if (layout == Layout.VERTICAL) {
                        int total = 0;
                        for (int i = 0; i < children.size(); i++) {
                                total += children.get(i).getHeight();
                                if (i < children.size() - 1)
                                        total += spacing;
                        }
                        return total + padding * 2;
                }
                int max = 0;
                for (IGuiComponent c : children)
                        max = Math.max(max, c.getHeight());
                return max + padding * 2;
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
