package com.mingsiamz.xuanlingmod.util.gui.component;

import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ModalDialog extends GuiWidget {

        private Component title = Component.empty();
        private String confirmLabel = "确定";
        private String cancelLabel = "取消";

        private boolean hasInput = false;
        private String placeholder = "";
        private Runnable onConfirm;
        private Runnable onCancel;
        private boolean closeOnConfirm = true;

        private boolean open = false;
        private int screenWidth = 1920;
        private int screenHeight = 1080;

        private TextInput input;
        private GuiButton confirmBtn;
        private GuiButton cancelBtn;
        private boolean built = false;

        public ModalDialog(int width, int height) {
                super(width, height);
        }

        public ModalDialog title(Component t) {
                this.title = t;
                return this;
        }

        public ModalDialog confirmLabel(String s) {
                this.confirmLabel = s;
                return this;
        }

        public ModalDialog cancelLabel(String s) {
                this.cancelLabel = s;
                return this;
        }

        public ModalDialog withInput(String placeholder) {
                this.hasInput = true;
                this.placeholder = placeholder;
                return this;
        }

        public ModalDialog onConfirm(Runnable r) {
                this.onConfirm = r;
                return this;
        }

        public ModalDialog onCancel(Runnable r) {
                this.onCancel = r;
                return this;
        }

        public ModalDialog setCloseOnConfirm(boolean b) {
                this.closeOnConfirm = b;
                return this;
        }

        public ModalDialog setScreenSize(int w, int h) {
                this.screenWidth = w;
                this.screenHeight = h;
                return this;
        }

        public boolean isOpen() {
                return open;
        }

        public void open() {
                this.open = true;
                buildIfNeeded();
                if (input != null)
                        input.setFocused(true);
        }

        public void close() {
                this.open = false;
                if (input != null)
                        input.setFocused(false);
        }

        public String getInputValue() {
                return input == null ? "" : input.getValue();
        }

        public ModalDialog setInputValue(String v) {
                buildIfNeeded();
                if (input != null)
                        input.setValue(v);
                return this;
        }

        private void buildIfNeeded() {
                if (built)
                        return;
                if (hasInput)
                        input = new TextInput(width - 40, 18, placeholder).setMaxLength(64);
                confirmBtn = new GuiButton(80, 20, confirmLabel).onClick(this::doConfirm);
                cancelBtn = new GuiButton(80, 20, cancelLabel).onClick(this::doCancel);
                built = true;
        }

        private void doConfirm() {
                if (onConfirm != null)
                        onConfirm.run();
                if (closeOnConfirm)
                        close();
        }

        private void doCancel() {
                if (onCancel != null)
                        onCancel.run();
                close();
        }

        private int panelX() {
                return (screenWidth - width) / 2;
        }

        private int panelY() {
                return (screenHeight - height) / 2;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                if (!open)
                        return;
                buildIfNeeded();

                this.hovered = isMouseOver(mouseX, mouseY);

                g.fill(0, 0, screenWidth, screenHeight, palette.mask());

                int px = panelX();
                int py = panelY();
                this.x = px;
                this.y = py;

                g.fill(px, py, px + width, py + height, palette.panelBg());
                GuiRenderUtil.glow(g, px, py, px + width, py + height, palette.accent() & 0xFFFFFF, 3, 8);
                GuiRenderUtil.border(g, px, py, px + width, py + height, palette.borderGold());
                GuiRenderUtil.border(g, px + 2, py + 2, px + width - 2, py + height - 2, palette.borderCyan());
                GuiRenderUtil.cornerAccents(g, px, py, px + width, py + height, 12, palette.borderGold());

                g.drawString(font(), title, px + (width - font().width(title)) / 2, py + 14, palette.textGold(),
                                true);
                g.fill(px + 8, py + 26, px + width - 8, py + 27, palette.subtleLine());

                int btnY = py + height - 32;
                int spacing = 10;
                int totalBtnW = confirmBtn.getWidth() + cancelBtn.getWidth() + spacing;
                int startX = px + (width - totalBtnW) / 2;
                confirmBtn.setPosition(startX, btnY);
                cancelBtn.setPosition(startX + confirmBtn.getWidth() + spacing, btnY);

                if (input != null) {
                        int iy = py + (btnY - (py + 26)) / 2 + 26 - input.getHeight() / 2;
                        input.setPosition(px + 20, iy);
                        input.render(g, mouseX, mouseY, partialTick);
                }

                confirmBtn.render(g, mouseX, mouseY, partialTick);
                cancelBtn.render(g, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (!open)
                        return false;

                if (confirmBtn.mouseClicked(mx, my, button))
                        return true;
                if (cancelBtn.mouseClicked(mx, my, button))
                        return true;
                if (input != null && input.isMouseOver(mx, my)) {
                        input.mouseClicked(mx, my, button);
                        return true;
                }
                if (!isMouseOver(mx, my) && input != null) {
                        input.mouseClicked(mx, my, button);
                }
                return true;
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                return open && (input != null && input.mouseReleased(mx, my, button));
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (!open)
                        return false;
                if (keyCode == 257 || keyCode == 335) {
                        doConfirm();
                        return true;
                }
                if (keyCode == 1) {
                        doCancel();
                        return true;
                }
                return input != null && input.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
                return open && input != null && input.charTyped(codePoint, modifiers);
        }
}
