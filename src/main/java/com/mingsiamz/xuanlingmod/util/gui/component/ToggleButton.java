package com.mingsiamz.xuanlingmod.util.gui.component;

import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class ToggleButton extends GuiWidget {

        private Component label;
        private boolean selected = false;
        private Consumer<Boolean> onToggle;

        public ToggleButton(int width, int height, String label) {
                super(width, height);
                this.label = Component.literal(label);
        }

        public ToggleButton setSelected(boolean s) {
                this.selected = s;
                return this;
        }

        public boolean isSelected() {
                return selected;
        }

        public ToggleButton onToggle(Consumer<Boolean> c) {
                this.onToggle = c;
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                if (!visible)
                        return;

                int bg = selected ? palette.buttonHoverBg()
                                : (hovered ? (palette.buttonBg() & 0xFFFFFF | 0x60000000) : palette.buttonBg());
                int brd = (selected || hovered) ? palette.buttonHoverBorder() : palette.buttonBorder();

                g.fill(x, y, x + width, y + height, bg);
                GuiRenderUtil.border(g, x, y, x + width, y + height, brd);
                if (selected)
                        g.fill(x + 1, y + 1, x + width - 1, y + 2, palette.subtleLine());

                int fg = selected ? palette.textGold() : palette.textPrimary();
                g.drawString(font(), label, x + (width - font().width(label)) / 2, y + (height - 8) / 2, fg, false);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (enabled && button == 0 && isMouseOver(mx, my)) {
                        this.selected = !this.selected;
                        if (onToggle != null)
                                onToggle.accept(this.selected);
                        return true;
                }
                return false;
        }
}
