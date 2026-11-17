package com.mingsiamz.xuanlingmod.util.gui.component;

import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class Stepper extends GuiWidget {

        private Component label;
        private double value;
        private final double step;
        private final double min;
        private final double max;
        private boolean integerMode;
        private Consumer<Double> onChange;

        private int minusX;
        private int plusX;
        private int rowH = 16;

        public Stepper(int width, Component label, double value, double step, double min, double max) {
                super(width, 18);
                this.label = label;
                this.step = step;
                this.min = min;
                this.max = max;
                this.integerMode = (step == Math.floor(step) && !(value != Math.floor(value)));
                this.value = clamp(value);
        }

        public Stepper onChange(Consumer<Double> c) {
                this.onChange = c;
                return this;
        }

        public double getValue() {
                return value;
        }

        private double clamp(double v) {
                return Math.max(min, Math.min(max, v));
        }

        private String display() {
                if (integerMode)
                        return String.valueOf((long) value);
                return String.format("%.2f", value);
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                if (!visible)
                        return;

                int ry = y + (height - rowH) / 2;
                minusX = x + width - 65;
                plusX = x + width - 25;

                g.drawString(font(), label, x, ry + 4, palette.textPrimary(), false);

                boolean hvM = mouseX >= minusX && mouseX < minusX + 12 && mouseY >= ry && mouseY < ry + rowH;
                g.fill(minusX, ry, minusX + 12, ry + rowH, hvM ? palette.hoverRed() : palette.softOverlay());
                GuiRenderUtil.border(g, minusX, ry, minusX + 12, ry + rowH, palette.borderDim());
                g.drawString(font(), "-", minusX + 4, ry + 4, palette.textPrimary(), false);

                g.drawString(font(), display(), minusX + 16, ry + 4, palette.textGold(), false);

                boolean hvP = mouseX >= plusX && mouseX < plusX + 12 && mouseY >= ry && mouseY < ry + rowH;
                g.fill(plusX, ry, plusX + 12, ry + rowH, hvP ? palette.hoverCyan() : palette.softOverlay());
                GuiRenderUtil.border(g, plusX, ry, plusX + 12, ry + rowH, palette.borderDim());
                g.drawString(font(), "+", plusX + 4, ry + 4, palette.textPrimary(), false);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (!enabled || button != 0 || !isMouseOver(mx, my))
                        return false;
                int ry = y + (height - rowH) / 2;
                if (mx >= minusX && mx < minusX + 12 && my >= ry && my < ry + rowH) {
                        setValue(value - step);
                        return true;
                }
                if (mx >= plusX && mx < plusX + 12 && my >= ry && my < ry + rowH) {
                        setValue(value + step);
                        return true;
                }
                return true;
        }

        public void setValue(double v) {
                double nv = clamp(v);
                if (integerMode)
                        nv = Math.round(nv);
                this.value = nv;
                if (onChange != null)
                        onChange.accept(this.value);
        }
}
