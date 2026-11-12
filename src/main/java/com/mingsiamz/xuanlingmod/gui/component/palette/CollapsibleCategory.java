package com.mingsiamz.xuanlingmod.gui.component.palette;

import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.gui.component.DragPayload;
import com.mingsiamz.xuanlingmod.gui.component.DragPayload.Type;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class CollapsibleCategory {
        private Component name;
        private int x, y;
        private final int width;
        private boolean expanded = false;

        private static final int HEADER_H = 20;
        private static final int ITEM_H = 18;
        private static final int ITEM_GAP = 2;

        private final List<PaletteItem> items = new ArrayList<>();

        private BiConsumer<DragPayload, int[]> onCategoryDrag;

        public CollapsibleCategory(Component name, int width) {
                this.name = name;
                this.width = width;
        }

        public void addItem(PaletteItem item) {
                items.add(item);
        }

        public List<PaletteItem> getItems() {
                return items;
        }

        public void setOnCategoryDrag(BiConsumer<DragPayload, int[]> listener) {
                this.onCategoryDrag = listener;
        }

        public void setPosition(int x, int y) {
                this.x = x;
                this.y = y;
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
                if (!expanded)
                        return HEADER_H;
                return HEADER_H + 4 + items.size() * (ITEM_H + ITEM_GAP);
        }

        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                boolean hoveredHeader = mouseX >= x && mouseX < x + width
                                && mouseY >= y && mouseY < y + HEADER_H;

                int headerBg = hoveredHeader ? 0x6000E5FF : 0x30101820;
                g.fill(x, y, x + width, y + HEADER_H, headerBg);

                var font = Minecraft.getInstance().font;
                String arrow = expanded ? "▼" : "▶";
                g.drawString(font, Component.literal(arrow),
                                x + 4, y + (HEADER_H - 8) / 2, CultivationTheme.BORDER_CYAN, false);
                g.drawString(font, this.name, x + 18, y + (HEADER_H - 8) / 2,
                                hoveredHeader ? CultivationTheme.TEXT_GOLD : CultivationTheme.TEXT_PRIMARY, false);

                GuiRenderUtil.border(g, x, y, x + width, y + HEADER_H, CultivationTheme.BORDER_DIM);

                if (expanded) {
                        int cy = y + HEADER_H + 4;
                        for (PaletteItem item : items) {
                                item.setPosition(x + 4, cy);
                                item.render(g, mouseX, mouseY, partialTick);
                                cy += ITEM_H + ITEM_GAP;
                        }
                }
        }

        public boolean mouseClicked(double mx, double my, int button) {
                if (button == 0 && mx >= x && mx < x + width && my >= y && my < y + HEADER_H) {
                        expanded = !expanded;
                        if (onCategoryDrag != null) {
                                onCategoryDrag.accept(new DragPayload(Type.CATEGORY, name.getString(), null), new int[]{(int)mx,(int)my});
                        }
                        return true;
                }
                if (expanded) {
                        for (PaletteItem item : items) {
                                if (item.mouseClicked(mx, my, button))
                                        return true;
                        }
                }
                return false;
        }
}
