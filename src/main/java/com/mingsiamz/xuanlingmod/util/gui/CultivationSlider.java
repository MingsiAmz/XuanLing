package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class CultivationSlider implements IGuiComponent {
        private int x, y, width, height;
        private float value = 0f; // 0.0 ~ 1.0
        private boolean dragging = false;
        private Consumer<Float> onValueChanged;

        public CultivationSlider(int width, int height) {
                this.width = width;
                this.height = height;
        }

        public void setValue(float value) {
                this.value = Mth.clamp(value, 0f, 1f);
                if (onValueChanged != null)
                        onValueChanged.accept(this.value);
        }

        public float getValue() {
                return value;
        }

        public CultivationSlider setOnValueChanged(Consumer<Float> listener) {
                this.onValueChanged = listener;
                return this;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                g.fill(x, y + height / 2 - 2, x + width, y + height / 2 + 2, CultivationTheme.PANEL_BG_DARK);
                GuiRenderUtil.border(g, x, y + height / 2 - 2, x + width, y + height / 2 + 2,
                                CultivationTheme.BORDER_DIM);

                int fillW = (int) (width * value);
                g.fill(x, y + height / 2 - 2, x + fillW, y + height / 2 + 2, CultivationTheme.BORDER_CYAN);

                int handleX = x + fillW - 4;
                g.fill(handleX, y, handleX + 8, y + height, CultivationTheme.BORDER_GOLD);
                GuiRenderUtil.border(g, handleX, y, handleX + 8, y + height, 0xFFFFFFFF);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (button == 0 && isMouseOver(mx, my)) {
                        this.dragging = true;
                        updateValueFromMouse(mx);
                        return true;
                }
                return false;
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (dragging && button == 0) {
                        updateValueFromMouse(mx);
                        return true;
                }
                return false;
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (dragging && button == 0) {
                        dragging = false;
                        return true;
                }
                return false;
        }

        private void updateValueFromMouse(double mx) {
                float raw = (float) (mx - x) / width;
                setValue(raw);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
                return mouseX >= x - 4 && mouseX < x + width + 4
                                && mouseY >= y - 4 && mouseY < y + height + 4;
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

        @Override
        public int getWidth() {
                return width;
        }

        @Override
        public int getHeight() {
                return height;
        }
}
