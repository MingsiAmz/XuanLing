package com.mingsiamz.xuanlingmod.gui.component.palette;

import com.mingsiamz.xuanlingmod.gui.component.data.ComponentCategory;
import com.mingsiamz.xuanlingmod.gui.component.data.ComponentDef;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PalettePanel {
        private int x, y, width, height;
        private final List<CollapsibleCategory> categories = new ArrayList<>();
        private PaletteItem.DragStart dragStart;

        public PalettePanel(int width, int height) {
                this.width = width;
                this.height = height;
        }

        public void setOnDragStart(PaletteItem.DragStart l) {
                this.dragStart = l;
        }

        public void loadComponents(List<ComponentDef> defs) {
                categories.clear();
                if (defs == null || defs.isEmpty())
                        return;

                Map<ComponentCategory, List<ComponentDef>> byCat = new LinkedHashMap<>();
                for (ComponentDef def : defs) {
                        byCat.computeIfAbsent(def.category(), k -> new ArrayList<>()).add(def);
                }
                for (var entry : byCat.entrySet()) {
                        CollapsibleCategory cat = new CollapsibleCategory(entry.getKey(), width - 12);
                        for (ComponentDef def : entry.getValue()) {
                                cat.addItem(new PaletteItem(def, width - 12, 18).setOnDragStart(dragStart));
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

        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                g.fill(x, y, x + width, y + height, 0xF0101820);
                GuiRenderUtil.border(g, x, y, x + width, y + height, CultivationTheme.BORDER_GOLD);

                var font = Minecraft.getInstance().font;
                g.drawString(font, Component.literal("◆ 组件库"),
                                x + 8, y + 6, CultivationTheme.BORDER_GOLD, false);

                if (categories.isEmpty()) {
                        g.drawString(font, Component.literal("(暂无可用组件)"),
                                        x + 12, y + 30, CultivationTheme.TEXT_SECONDARY, false);
                        return;
                }

                int cy = y + 22;
                for (CollapsibleCategory cat : categories) {
                        cat.setPosition(x + 6, cy);
                        cat.render(g, mouseX, mouseY, partialTick);
                        cy += cat.getHeight() + 4;
                }
        }

        public boolean mouseClicked(double mx, double my, int button) {
                if (!contains(mx, my))
                        return false;
                for (CollapsibleCategory cat : categories) {
                        if (cat.mouseClicked(mx, my, button))
                                return true;
                }
                return true;
        }
}
