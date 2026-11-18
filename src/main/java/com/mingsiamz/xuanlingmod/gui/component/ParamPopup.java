package com.mingsiamz.xuanlingmod.gui.component;

import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.ParamType;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamPopup {
        private final ImpComponent target;
        private final int x, y;
        private final int width = 160;
        private final int height;

        private final List<RowData> rows = new ArrayList<>();
        private ParamType editingKey = null;
        private String editBuffer = "";

        public ParamPopup(ImpComponent target, int x, int y) {
                this.target = target;
                this.x = x;
                this.y = y;

                Map<ParamType, Object> params = target != null ? target.getParams() : null;
                int rowCount = params != null ? params.size() : 0;

                this.height = 30 + rowCount * 22 + 10;

                if (params != null) {
                        int cy = this.y + 25;
                        for (Map.Entry<ParamType, Object> entry : params.entrySet()) {
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
                g.fill(x, y, x + width, y + height, CultivationTheme.PANEL_BG);
                GuiRenderUtil.border(g, x, y, x + width, y + height, CultivationTheme.BORDER_GOLD);

                var font = Minecraft.getInstance().font;
                g.drawString(font, target.trans().getString() + " 参数", x + 8, y + 6,
                                CultivationTheme.BORDER_GOLD, false);
                g.fill(x + 6, y + 18, x + width - 6, y + 19, CultivationTheme.SUBTLE_LINE);

                Map<ParamType, Object> params = target.getParams();
                if (params == null)
                        return;

                for (RowData row : rows) {
                        g.drawString(font, row.key.name, x + 8, row.y + 4, CultivationTheme.TEXT_PRIMARY,
                                false);

                        int minusX = x + width - 65;
                        boolean hoverMinus = mouseX >= minusX && mouseX < minusX + 12 && mouseY >= row.y
                                        && mouseY < row.y + 16;
                        g.fill(minusX, row.y, minusX + 12, row.y + 16,
                                        hoverMinus ? CultivationTheme.HOVER_RED : CultivationTheme.SOFT_OVERLAY);
                        GuiRenderUtil.border(g, minusX, row.y, minusX + 12, row.y + 16,
                                CultivationTheme.BORDER_DIM);
                        g.drawString(font, "-", minusX + 4, row.y + 4, CultivationTheme.TEXT_PRIMARY, false);

                        Object valObj = params.get(row.key);
                        String valStr = formatValue(valObj);
                        boolean editing = editingKey == row.key;
                        int valX = minusX + 16;
                        if (editing) {
                                String show = editBuffer + ((System.currentTimeMillis() / 500) % 2 == 0 ? "_" : "");
                                g.drawString(font, show, valX, row.y + 4, CultivationTheme.TEXT_GOLD, false);
                        } else {
                                g.drawString(font, valStr, valX, row.y + 4, CultivationTheme.TEXT_GOLD, false);
                        }

                        int plusX = x + width - 25;
                        boolean hoverPlus = mouseX >= plusX && mouseX < plusX + 12 && mouseY >= row.y
                                        && mouseY < row.y + 16;
                        g.fill(plusX, row.y, plusX + 12, row.y + 16,
                                        hoverPlus ? CultivationTheme.HOVER_CYAN : CultivationTheme.SOFT_OVERLAY);
                        GuiRenderUtil.border(g, plusX, row.y, plusX + 12, row.y + 16, CultivationTheme.BORDER_DIM);
                        g.drawString(font, "+", plusX + 4, row.y + 4, CultivationTheme.TEXT_PRIMARY, false);
                }
        }

        public boolean mouseClicked(double mx, double my, int button) {
                if (!isMouseOver(mx, my))
                        return false;

                Map<ParamType, Object> params = target.getParams();
                if (params == null)
                        return true;

                boolean shift = Screen.hasShiftDown();

                for (RowData row : rows) {
                        int minusX = x + width - 65;
                        int plusX = x + width - 25;
                        int valX = minusX + 16;
                        int valW = plusX - valX;

                        Object valObj = params.get(row.key);
                        double currentVal = valObj instanceof Number ? ((Number) valObj).doubleValue() : 0.0;

                        boolean isInt = valObj instanceof Integer || valObj instanceof Long;
                        double step = (isInt ? 1.0 : 0.1) * (shift ? 10.0 : 1.0);

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
                        if (mx >= valX && mx < valX + valW && my >= row.y && my < row.y + 16) {
                                editingKey = row.key;
                                editBuffer = String.valueOf(currentVal);
                                return true;
                        }
                }
                return true;
        }

        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (editingKey == null)
                        return false;
                if (keyCode == 257 || keyCode == 335) {
                        commitEdit();
                        return true;
                }
                if (keyCode == 256) {
                        editingKey = null;
                        editBuffer = "";
                        return true;
                }
                return false;
        }

        public boolean charTyped(char codePoint, int modifiers) {
                if (editingKey == null)
                        return false;
                if (codePoint >= '0' && codePoint <= '9' || codePoint == '.' || codePoint == '-') {
                        editBuffer += codePoint;
                        return true;
                }
                if (codePoint == '\b') {
                        if (!editBuffer.isEmpty())
                                editBuffer = editBuffer.substring(0, editBuffer.length() - 1);
                        return true;
                }
                return false;
        }

        private void commitEdit() {
                if (editingKey == null)
                        return;
                Map<ParamType, Object> params = target.getParams();
                if (params == null) {
                        editingKey = null;
                        editBuffer = "";
                        return;
                }
                Object cur = params.get(editingKey);
                boolean isInt = cur instanceof Integer || cur instanceof Long;
                try {
                        double v = Double.parseDouble(editBuffer);
                        if (isInt)
                                v = Math.round(v);
                        params.put(editingKey, isInt ? (int) v : v);
                } catch (NumberFormatException ignored) {
                }
                editingKey = null;
                editBuffer = "";
        }

        private static String formatValue(Object valObj) {
                if (valObj instanceof Number number) {
                        return String.format("%.2f", number.doubleValue());
                }
                return String.valueOf(valObj);
        }

        private static class RowData {
                ParamType key;
                int y;

                public RowData(ParamType key, int y) {
                        this.key = key;
                        this.y = y;
                }
        }
}
