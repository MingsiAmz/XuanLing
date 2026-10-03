package com.mingsiamz.xuanlingmod.util.gui.layout;

import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;

import java.util.List;

public class ColumnLayout extends Layout {

        @Override
        public void apply(int cx, int cy, int cw, int ch, List<IGuiComponent> children) {
                if (children.isEmpty())
                        return;

                int count = children.size();
                int sumH = 0;
                for (IGuiComponent c : children)
                        sumH += c.getHeight();
                int contentH = sumH + spacing * (count - 1);

                int startY = cy + startOffset(justify, ch, contentH, count);
                int gap = gapSize(justify, ch, contentH, spacing, count);

                int cursor = startY;
                for (IGuiComponent c : children) {
                        int x = cx + crossOffset(align, cw, c.getWidth());
                        c.setPosition(x, cursor);
                        cursor += c.getHeight() + gap;
                }
        }

        @Override
        public int measureWidth(List<IGuiComponent> children) {
                int max = 0;
                for (IGuiComponent c : children)
                        max = Math.max(max, c.getWidth());
                return max;
        }

        @Override
        public int measureHeight(List<IGuiComponent> children) {
                if (children.isEmpty())
                        return 0;
                int sum = 0;
                for (IGuiComponent c : children)
                        sum += c.getHeight();
                return sum + spacing * (children.size() - 1);
        }
}
