package com.mingsiamz.xuanlingmod.gui.component.palette;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.gui.component.DragPayload;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class PalettePanel {
        private int x, y, width, height;
        private final List<CollapsibleCategory> categories = new ArrayList<>();
        // private PaletteItem.DragStart dragStart;
        private BiConsumer<DragPayload, int[]> dragStart;

        private float scrollY = 0;
        private static final int TITLE_HEIGHT = 22;
        private static final int SCROLLBAR_WIDTH = 4;
        private static final int SCROLLBAR_MARGIN = 2;

        private boolean isDraggingScrollbar = false;
        private float dragStartY = 0;
        private float dragStartScrollY = 0;

        public PalettePanel(int width, int height) {
                this.width = width;
                this.height = height;
        }

        public void setOnDragStart(BiConsumer<DragPayload, int[]> l) {
                this.dragStart = l;
        }

        public void loadComponents(List<IComponent<?>> cmps) {
                categories.clear();
                scrollY = 0;
                if (cmps == null || cmps.isEmpty())
                        return;

                Map<Component, List<IComponent<?>>> byCat = new LinkedHashMap<>();
                for (IComponent<?> def : cmps) {
                        byCat.computeIfAbsent(def.transType(), k -> new ArrayList<>()).add(def);
                }

                int itemWidth = width - 12 - (SCROLLBAR_WIDTH + SCROLLBAR_MARGIN * 2);

                for (Map.Entry<Component, List<IComponent<?>>> entry : byCat.entrySet()) {
                        CollapsibleCategory cat = new CollapsibleCategory(entry.getKey(), itemWidth);
                        cat.setOnCategoryDrag(dragStart);
                        for (IComponent<?> def : entry.getValue()) {
                                cat.addItem(new PaletteItem(def, itemWidth, 18)
                                                .setOnDragStart((d, mx, my) -> dragStart.accept(
                                                                new DragPayload(DragPayload.Type.COMPONENT,
                                                                                def.trans().getString(), d),
                                                                new int[] { mx, my })));
                        }
                        categories.add(cat);
                }
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
                return height;
        }

        public void resize(int w, int h) {
                this.width = w;
                this.height = h;
        }

        public boolean contains(double mx, double my) {
                return mx >= x && mx < x + width && my >= y && my < y + height;
        }

        private int getContentHeight() {
                int total = 0;
                for (CollapsibleCategory cat : categories) {
                        total += cat.getHeight() + 4;
                }
                return total;
        }

        private int getViewHeight() {
                return height - TITLE_HEIGHT;
        }

        private float getMaxScroll() {
                return Math.max(0, getContentHeight() - getViewHeight());
        }

        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                g.fill(x, y, x + width, y + height, 0xF0101820);
                GuiRenderUtil.border(g, x, y, x + width, y + height, CultivationTheme.BORDER_GOLD);

                var font = Minecraft.getInstance().font;
                Component componentLib = XuanLing.trans(XuanlingLangPro.GUI_STRING, "component_lib_title");
                g.drawString(font, componentLib, x + 8, y + 6, CultivationTheme.BORDER_GOLD, false);

                Component notComponent = XuanLing.trans(XuanlingLangPro.GUI_STRING, "not_component");
                if (categories.isEmpty()) {
                        g.drawString(font, notComponent, x + 12, y + 30, CultivationTheme.TEXT_SECONDARY, false);
                        return;
                }

                g.enableScissor(x, y + TITLE_HEIGHT, x + width, y + height);

                int cy = y + TITLE_HEIGHT - (int) scrollY;

                for (CollapsibleCategory cat : categories) {
                        cat.setPosition(x + 6, cy);

                        if (cy + cat.getHeight() > y + TITLE_HEIGHT && cy < y + height) {
                                cat.render(g, mouseX, mouseY, partialTick);
                        }
                        cy += cat.getHeight() + 4;
                }

                g.disableScissor();
                float maxScroll = getMaxScroll();
                if (maxScroll > 0) {
                        int scrollbarX = x + width - SCROLLBAR_WIDTH - SCROLLBAR_MARGIN;
                        int viewHeight = getViewHeight();

                        g.fill(scrollbarX, y + TITLE_HEIGHT, scrollbarX + SCROLLBAR_WIDTH, y + height, 0x30000000);

                        int barHeight = Math.max(20, (int) ((float) viewHeight / getContentHeight() * viewHeight));
                        int barY = y + TITLE_HEIGHT + (int) ((viewHeight - barHeight) * (scrollY / maxScroll));

                        boolean hovered = mouseX >= scrollbarX && mouseX <= scrollbarX + SCROLLBAR_WIDTH
                                        && mouseY >= barY && mouseY <= barY + barHeight;
                        int barColor = (hovered || isDraggingScrollbar) ? CultivationTheme.BORDER_GOLD
                                        : CultivationTheme.BORDER_CYAN;

                        g.fill(scrollbarX, barY, scrollbarX + SCROLLBAR_WIDTH, barY + barHeight, barColor);
                }
        }

        public boolean mouseClicked(double mx, double my, int button) {
                if (!contains(mx, my))
                        return false;

                float maxScroll = getMaxScroll();
                int viewHeight = getViewHeight();
                int scrollbarX = x + width - SCROLLBAR_WIDTH - SCROLLBAR_MARGIN;

                if (maxScroll > 0 && mx >= scrollbarX - 2) {
                        int barHeight = Math.max(20, (int) ((float) viewHeight / getContentHeight() * viewHeight));
                        int barY = y + TITLE_HEIGHT + (int) ((viewHeight - barHeight) * (scrollY / maxScroll));

                        if (my >= barY && my <= barY + barHeight) {
                                isDraggingScrollbar = true;
                                dragStartY = (float) my;
                                dragStartScrollY = scrollY;
                                return true;
                        }
                }

                if (my < y + TITLE_HEIGHT)
                        return true;

                for (CollapsibleCategory cat : categories) {
                        if (cat.mouseClicked(mx, my, button))
                                return true;
                }
                return true;
        }

        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (isDraggingScrollbar) {
                        float maxScroll = getMaxScroll();
                        int viewHeight = getViewHeight();
                        int barHeight = Math.max(20, (int) ((float) viewHeight / getContentHeight() * viewHeight));

                        if (viewHeight - barHeight > 0) {
                                float scrollDelta = (float) ((my - dragStartY) / (viewHeight - barHeight) * maxScroll);
                                scrollY = Mth.clamp(dragStartScrollY + scrollDelta, 0, maxScroll);
                        }
                        return true;
                }
                return false;
        }

        public boolean mouseReleased(double mx, double my, int button) {
                if (isDraggingScrollbar) {
                        isDraggingScrollbar = false;
                        return true;
                }
                return false;
        }

        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (!contains(mx, my))
                        return false;

                float maxScroll = getMaxScroll();
                if (maxScroll > 0) {
                        scrollY = Mth.clamp(scrollY - (float) sy * 15f, 0, maxScroll);
                        return true;
                }
                return false;
        }

}
