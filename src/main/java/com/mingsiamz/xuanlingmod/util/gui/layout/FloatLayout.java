package com.mingsiamz.xuanlingmod.util.gui.layout;

import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class FloatLayout extends Layout {

        public enum Side {
                LEFT, RIGHT
        }

        private final Map<IGuiComponent, Side> sides = new IdentityHashMap<>();
        private int lineSpacing = -1;

        public FloatLayout floatLeft(IGuiComponent... cs) {
                for (var c : cs)
                        sides.put(c, Side.LEFT);
                return this;
        }

        public FloatLayout floatRight(IGuiComponent... cs) {
                for (var c : cs)
                        sides.put(c, Side.RIGHT);
                return this;
        }

        public FloatLayout setSide(IGuiComponent c, Side s) {
                sides.put(c, s);
                return this;
        }

        public FloatLayout setLineSpacing(int s) {
                this.lineSpacing = s;
                return this;
        }

        private int lineGap() {
                return lineSpacing >= 0 ? lineSpacing : spacing;
        }

        @Override
        public void apply(int cx, int cy, int cw, int ch, List<IGuiComponent> children) {
                int leftX = cx;
                int rightX = cx + cw;
                int topY = cy;
                int lineH = 0;
                int lg = lineGap();

                for (IGuiComponent c : children) {
                        Side side = sides.getOrDefault(c, Side.LEFT);
                        int w = c.getWidth();

                        if (side == Side.LEFT) {
                                if (leftX + w > rightX) {
                                        topY += lineH + lg;
                                        leftX = cx;
                                        rightX = cx + cw;
                                        lineH = 0;
                                }
                                c.setPosition(leftX, topY);
                                leftX += w + spacing;
                                lineH = Math.max(lineH, c.getHeight());
                        } else {
                                if (rightX - w < leftX) {
                                        topY += lineH + lg;
                                        leftX = cx;
                                        rightX = cx + cw;
                                        lineH = 0;
                                }
                                rightX -= w;
                                c.setPosition(rightX, topY);
                                rightX -= spacing;
                                lineH = Math.max(lineH, c.getHeight());
                        }
                }
        }

        @Override
        public int measureWidth(List<IGuiComponent> children) {
                int sum = 0;
                for (IGuiComponent c : children)
                        sum += c.getWidth();
                return sum + spacing * Math.max(0, children.size() - 1);
        }

        @Override
        public int measureHeight(List<IGuiComponent> children) {
                int max = 0;
                for (IGuiComponent c : children)
                        max = Math.max(max, c.getHeight());
                return max;
        }
}
