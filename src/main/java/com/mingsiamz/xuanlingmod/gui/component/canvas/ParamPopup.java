package com.mingsiamz.xuanlingmod.gui.component.canvas;

import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamPopup {
        private final IComponent<?> target;
        private final int x, y;
        private final int width = 160;
        private final int height;

        private final List<RowData> rows = new ArrayList<>();

        public ParamPopup(IComponent<?> target, int x, int y) {
                this.target = target;
                this.x = x;
                this.y = y;

                Map<String, Object> params = target != null ? target.getParams() : null;
                int rowCount = params != null ? params.size() : 0;

                this.height = 30 + rowCount * 22 + 10;

                if (params != null) {
                        int cy = this.y + 25;
                        for (Map.Entry<String, Object> entry : params.entrySet()) {
                                rows.add(new RowData(entry.getKey(), cy));
                                cy += 22;
                        }
                }
        }

        public IComponent<?> getTarget() {
                return target;
        }

        public int getX() {
                return x;
        }

        public int getY() {
                return y;
        }

        public int getWidth() {
                return width;
        }

        public int getHeight() {
                return height;
        }

        public boolean isMouseOver(double mx, double my) {
                return mx >= x && mx < x + width && my >= y && my < y + height;
        }

        public void render(GuiGraphics g, int mouseX, int mouseY) {
                g.fill(x, y, x + width, y + height, 0xF0101820);
                GuiRenderUtil.border(g, x, y, x + width, y + height, CultivationTheme.BORDER_GOLD);

                var font = Minecraft.getInstance().font;
                g.drawString(font, target.trans().getString() + " 参数", x + 8, y + 6, CultivationTheme.BORDER_GOLD,
                                false);
                g.fill(x + 6, y + 18, x + width - 6, y + 19, 0x40FFFFFF);

                Map<String, Object> params = target.getParams();
                if (params == null)
                        return;

                for (RowData row : rows) {
                        g.drawString(font, row.key, x + 8, row.y + 4, CultivationTheme.TEXT_PRIMARY, false);

                        int minusX = x + width - 65;
                        boolean hoverMinus = mouseX >= minusX && mouseX < minusX + 12 && mouseY >= row.y
                                        && mouseY < row.y + 16;
                        g.fill(minusX, row.y, minusX + 12, row.y + 16, hoverMinus ? 0x60FF5555 : 0x30000000);
                        GuiRenderUtil.border(g, minusX, row.y, minusX + 12, row.y + 16, CultivationTheme.BORDER_DIM);
                        g.drawString(font, "-", minusX + 4, row.y + 4, CultivationTheme.TEXT_PRIMARY, false);

                        Object valObj = params.get(row.key);
                        String valStr = String.valueOf(valObj);
                        g.drawString(font, valStr, minusX + 16, row.y + 4, CultivationTheme.TEXT_GOLD, false);

                        int plusX = x + width - 25;
                        boolean hoverPlus = mouseX >= plusX && mouseX < plusX + 12 && mouseY >= row.y
                                        && mouseY < row.y + 16;
                        g.fill(plusX, row.y, plusX + 12, row.y + 16, hoverPlus ? 0x6000E5FF : 0x30000000);
                        GuiRenderUtil.border(g, plusX, row.y, plusX + 12, row.y + 16, CultivationTheme.BORDER_DIM);
                        g.drawString(font, "+", plusX + 4, row.y + 4, CultivationTheme.TEXT_PRIMARY, false);
                }
        }

        public boolean mouseClicked(double mx, double my, int button) {
                if (!isMouseOver(mx, my))
                        return false;

                Map<String, Object> params = target.getParams();
                if (params == null)
                        return true;

                for (RowData row : rows) {
                        int minusX = x + width - 65;
                        int plusX = x + width - 25;

                        Object valObj = params.get(row.key);
                        double currentVal = valObj instanceof Number ? ((Number) valObj).doubleValue() : 0.0;

                        boolean isInt = valObj instanceof Integer || valObj instanceof Long;
                        double step = isInt ? 1.0 : 0.1;

                        if (mx >= minusX && mx < minusX + 12 && my >= row.y && my < row.y + 16) {
                                double newVal = currentVal - step;
                                if (isInt)
                                        newVal = Math.round(newVal);
                                params.put(row.key, isInt ? (int) newVal : newVal);
                                return true;
                        }
                        if (mx >= plusX && mx < plusX + 12 && my >= row.y && my < row.y + 16) {
                                double newVal = currentVal + step;
                                if (isInt)
                                        newVal = Math.round(newVal);
                                params.put(row.key, isInt ? (int) newVal : newVal);
                                return true;
                        }
                }
                return true;
        }

        private static class RowData {
                String key;
                int y;

                public RowData(String key, int y) {
                        this.key = key;
                        this.y = y;
                }
        }
}
