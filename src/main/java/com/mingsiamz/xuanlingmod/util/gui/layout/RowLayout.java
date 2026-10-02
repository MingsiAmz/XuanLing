package com.mingsiamz.xuanlingmod.util.gui.layout;

import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;

import java.util.List;

public class RowLayout extends Layout {

        @Override
        public void apply(int cx, int cy, int cw, int ch, List<IGuiComponent> children) {
                if (children.isEmpty())
                        return;

                int count = children.size();
                int sumW = 0;
                for (IGuiComponent c : children)
                        sumW += c.getWidth();
                int contentW = sumW + spacing * (count - 1);

                int startX = cx + startOffset(justify, cw, contentW, count);
                int gap = gapSize(justify, cw, contentW, spacing, count);

                int cursor = startX;
                for (IGuiComponent c : children) {
                        int y = cy + crossOffset(align, ch, c.getHeight());
                        c.setPosition(cursor, y);
                        cursor += c.getWidth() + gap;
                }
        }

        @Override
        public int measureWidth(List<IGuiComponent> children) {
                if (children.isEmpty())
                        return 0;
                int sum = 0;
                for (IGuiComponent c : children)
                        sum += c.getWidth();
                return sum + spacing * (children.size() - 1);
        }

        @Override
        public int measureHeight(List<IGuiComponent> children) {
                int max = 0;
                for (IGuiComponent c : children)
                        max = Math.max(max, c.getHeight());
                return max;
        }
}
