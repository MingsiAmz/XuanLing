package com.mingsiamz.xuanlingmod.util.gui.component;

import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class CheckBox extends GuiWidget {

        private static final int box = 10;

        private final Component label;
        private boolean checked = false;
        private Consumer<Boolean> onChange;

        public CheckBox(String label) {
                super(8 + box + 4 + 40, 12);
                this.label = Component.literal(label);
                this.width = box + 4 + font().width(this.label);
        }

        public CheckBox setChecked(boolean c) {
                this.checked = c;
                return this;
        }

        public boolean isChecked() {
                return checked;
        }

        public CheckBox onChange(Consumer<Boolean> c) {
                this.onChange = c;
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                if (!visible)
                        return;

                int by = y + (height - box) / 2;
                g.fill(x, by, x + box, by + box, palette.panelBgDark());
                GuiRenderUtil.border(g, x, by, x + box, by + box,
                                (checked || hovered) ? palette.borderGold() : palette.borderDim());

                if (checked) {
                        g.fill(x + 2, by + 5, x + 4, by + 7, palette.accent());
                        g.fill(x + 4, by + 5, x + 5, by + 3, palette.accent());
                        g.fill(x + 5, by + 3, x + 8, by + 6, palette.accent());
                        g.fill(x + 5, by + 7, x + 8, by + 4, palette.accent());
                }

                g.drawString(font(), label, x + box + 4, y + (height - 8) / 2,
                                enabled ? palette.textPrimary() : palette.textSecondary(), false);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (enabled && button == 0 && isMouseOver(mx, my)) {
                        this.checked = !this.checked;
                        if (onChange != null)
                                onChange.accept(this.checked);
                        return true;
                }
                return false;
        }
}
