package com.mingsiamz.xuanlingmod.util.gui.layout;

import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;

import java.util.List;

public class GridLayout extends Layout {

        private final int cols;
        private int rows = 0;
        private int rowSpacing = -1;
        private int colSpacing = -1;

        public GridLayout(int cols) {
                this.cols = Math.max(1, cols);
        }

        public GridLayout(int cols, int rows) {
                this.cols = Math.max(1, cols);
                this.rows = rows;
        }

        public GridLayout setRowSpacing(int s) {
                this.rowSpacing = s;
                return this;
        }

        public GridLayout setColSpacing(int s) {
                this.colSpacing = s;
                return this;
        }

        private int rowGap() {
                return rowSpacing >= 0 ? rowSpacing : spacing;
        }

        private int colGap() {
                return colSpacing >= 0 ? colSpacing : spacing;
        }

        @Override
        public void apply(int cx, int cy, int cw, int ch, List<IGuiComponent> children) {
                if (children.isEmpty())
                        return;

                int count = children.size();
                int actualRows = rows > 0 ? rows : (int) Math.ceil((double) count / cols);
                int rg = rowGap();
                int cg = colGap();

                int cellW = (cw - cg * (cols - 1)) / cols;
                int cellH = actualRows > 0 ? (ch - rg * (actualRows - 1)) / actualRows : ch;

                for (int i = 0; i < count; i++) {
                        int r = i / cols;
                        int c = i % cols;
                        int x = cx + c * (cellW + cg);
                        int y = cy + r * (cellH + rg);

                        IGuiComponent child = children.get(i);
                        int ox = crossOffset(align, cellW, child.getWidth());
                        int oy = crossOffset(align, cellH, child.getHeight());
                        child.setPosition(x + Math.max(0, ox), y + Math.max(0, oy));
                }
        }

        @Override
        public int measureWidth(List<IGuiComponent> children) {
                int max = 0;
                for (IGuiComponent c : children)
                        max = Math.max(max, c.getWidth());
                return max * cols + colGap() * (cols - 1);
        }

        @Override
        public int measureHeight(List<IGuiComponent> children) {
                if (children.isEmpty())
                        return 0;
                int actualRows = rows > 0 ? rows : (int) Math.ceil((double) children.size() / cols);
                int max = 0;
                for (IGuiComponent c : children)
                        max = Math.max(max, c.getHeight());
                return max * actualRows + rowGap() * (actualRows - 1);
        }
}
