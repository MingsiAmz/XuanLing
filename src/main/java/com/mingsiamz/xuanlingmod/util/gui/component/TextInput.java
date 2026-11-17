package com.mingsiamz.xuanlingmod.util.gui.component;

import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

public class TextInput extends GuiWidget {

        private final EditBox edit;

        public TextInput(int width, int height, String placeholder) {
                super(width, height);
                this.edit = new EditBox(font(), x, y, width, height, Component.literal(placeholder));
                this.edit.setBordered(false);
                this.edit.setValue("");
        }

        public TextInput setMaxLength(int len) {
                this.edit.setMaxLength(len);
                return this;
        }

        public TextInput setValue(String v) {
                this.edit.setValue(v == null ? "" : v);
                return this;
        }

        public String getValue() {
                return this.edit.getValue();
        }

        public boolean isFocused() {
                return this.edit.isFocused();
        }

        public TextInput setFocused(boolean f) {
                this.edit.setFocused(f);
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                if (!visible)
                        return;

                g.fill(x, y, x + width, y + height, palette.inputBg());
                GuiRenderUtil.border(g, x, y, x + width, y + height,
                                isFocused() ? palette.borderGold() : palette.inputBorder());

                this.edit.setX(x + 3);
                this.edit.setY(y + (height - 10) / 2);
                this.edit.setWidth(width - 6);

                String text = this.edit.getValue();
                if (text.isEmpty()) {
                        g.drawString(font(), this.edit.getMessage(), x + 4, y + (height - 8) / 2,
                                        palette.textSecondary(), false);
                } else {
                        this.edit.render(g, mouseX, mouseY, partialTick);
                        if (isFocused() && (System.currentTimeMillis() / 500) % 2 == 0) {
                                int tw = font().width(text);
                                int tx = x + 3 + tw;
                                g.fill(tx + 1, y + 2, tx + 2, y + height - 2, palette.cursor());
                        }
                }
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (enabled && isMouseOver(mx, my)) {
                        this.edit.setFocused(true);
                        return true;
                } else if (isFocused()) {
                        this.edit.setFocused(false);
                }
                return false;
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                return isFocused() && this.edit.mouseReleased(mx, my, button);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                return isFocused() && this.edit.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
                return isFocused() && this.edit.charTyped(codePoint, modifiers);
        }
}
