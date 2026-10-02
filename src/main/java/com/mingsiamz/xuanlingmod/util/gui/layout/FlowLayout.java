package com.mingsiamz.xuanlingmod.util.gui.layout;

import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends Layout {

        private int maxWidth = -1;
        private int lineSpacing = -1;

        public FlowLayout setMaxWidth(int w) {
                this.maxWidth = w;
                return this;
        }

        public FlowLayout setLineSpacing(int s) {
                this.lineSpacing = s;
                return this;
        }

        private int lineGap() {
                return lineSpacing >= 0 ? lineSpacing : spacing;
        }

        @Override
        public void apply(int cx, int cy, int cw, int ch, List<IGuiComponent> children) {
                if (children.isEmpty())
                        return;

                int wrapW = maxWidth > 0 ? maxWidth : cw;
                int lg = lineGap();

                List<List<IGuiComponent>> lines = new ArrayList<>();
                List<IGuiComponent> cur = new ArrayList<>();
                int curW = 0;
                for (IGuiComponent c : children) {
                        int w = c.getWidth();
                        int needed = cur.isEmpty() ? w : curW + spacing + w;
                        if (needed > wrapW && !cur.isEmpty()) {
                                lines.add(cur);
                                cur = new ArrayList<>();
                                curW = w;
                        } else {
                                curW = needed;
                        }
                        cur.add(c);
                }
                if (!cur.isEmpty())
                        lines.add(cur);

                int[] lineHs = new int[lines.size()];
                int totalH = 0;
                for (int i = 0; i < lines.size(); i++) {
                        int h = 0;
                        for (IGuiComponent c : lines.get(i))
                                h = Math.max(h, c.getHeight());
                        lineHs[i] = h;
                        totalH += h;
                }
                totalH += lg * (lines.size() - 1);

                int startY = cy + switch (align) {
                        case CENTER -> Math.max(0, (ch - totalH) / 2);
                        case END -> Math.max(0, ch - totalH);
                        default -> 0;
                };

                int y = startY;
                for (int li = 0; li < lines.size(); li++) {
                        List<IGuiComponent> line = lines.get(li);
                        int lineH = lineHs[li];

                        int sumW = 0;
                        for (IGuiComponent c : line)
                                sumW += c.getWidth();
                        int contentW = sumW + spacing * (line.size() - 1);

                        int sx = cx + startOffset(justify, cw, contentW, line.size());
                        int gap = gapSize(justify, cw, contentW, spacing, line.size());

                        int cursor = sx;
                        for (IGuiComponent c : line) {
                                c.setPosition(cursor, y);
                                cursor += c.getWidth() + gap;
                        }
                        y += lineH + lg;
                }
        }

        @Override
        public int measureWidth(List<IGuiComponent> children) {
                if (maxWidth > 0)
                        return maxWidth;
                if (children.isEmpty())
                        return 0;
                int sum = 0;
                for (IGuiComponent c : children)
                        sum += c.getWidth();
                return sum + spacing * (children.size() - 1);
        }

        @Override
        public int measureHeight(List<IGuiComponent> children) {
                if (children.isEmpty())
                        return 0;
                int wrapW = maxWidth > 0 ? maxWidth : Integer.MAX_VALUE;
                int lg = lineGap();

                int lines = 1, curW = 0, totalH = 0, lineH = 0;
                for (IGuiComponent c : children) {
                        int w = c.getWidth();
                        int needed = curW == 0 ? w : curW + spacing + w;
                        if (needed > wrapW && curW > 0) {
                                totalH += lineH + lg;
                                lines++;
                                curW = w;
                                lineH = c.getHeight();
                        } else {
                                curW = needed;
                                lineH = Math.max(lineH, c.getHeight());
                        }
                }
                totalH += lineH;
                return totalH;
        }
}
