package com.mingsiamz.xuanlingmod.util.gui.layout;

import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;

import java.util.List;

public abstract class Layout {

        protected Justify justify = Justify.START;
        protected Align align = Align.START;
        protected int spacing = 0;

        public Layout setJustify(Justify justify) {
                this.justify = justify;
                return this;
        }

        public Layout setAlign(Align align) {
                this.align = align;
                return this;
        }

        public Layout setSpacing(int spacing) {
                this.spacing = spacing;
                return this;
        }

        public Justify getJustify() {
                return justify;
        }

        public Align getAlign() {
                return align;
        }

        public int getSpacing() {
                return spacing;
        }

        public abstract void apply(int cx, int cy, int cw, int ch, List<IGuiComponent> children);

        public abstract int measureWidth(List<IGuiComponent> children);

        public abstract int measureHeight(List<IGuiComponent> children);

        protected static int startOffset(Justify j, int container, int content, int count) {
                return switch (j) {
                        case START, SPACE_BETWEEN -> 0;
                        case CENTER -> (container - content) / 2;
                        case END -> container - content;
                        case SPACE_AROUND -> Math.max(0, (container - content) / Math.max(1, count) / 2);
                        case SPACE_EVENLY -> Math.max(0, (container - content) / Math.max(1, count + 1));
                };
        }

        protected static int gapSize(Justify j, int container, int content, int base, int count) {
                if (count <= 1)
                        return base;
                int remaining = Math.max(0, container - content);
                return switch (j) {
                        case SPACE_BETWEEN -> Math.max(base, remaining / (count - 1));
                        case SPACE_AROUND -> Math.max(base, remaining / count);
                        case SPACE_EVENLY -> Math.max(base, remaining / (count + 1));
                        default -> base;
                };
        }

        protected static int crossOffset(Align a, int container, int itemSize) {
                return switch (a) {
                        case CENTER -> (container - itemSize) / 2;
                        case END -> container - itemSize;
                        default -> 0;
                };
        }
}
