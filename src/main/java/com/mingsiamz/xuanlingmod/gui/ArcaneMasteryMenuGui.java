package com.mingsiamz.xuanlingmod.gui;

import java.util.ArrayList;
import java.util.List;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.util.gui.CultivationPanel;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import com.mingsiamz.xuanlingmod.util.gui.IGuiComponent;
import com.mingsiamz.xuanlingmod.util.gui.SectionDivider;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ArcaneMasteryMenuGui extends Screen {

        private static final int GRID_GAP = 8;
        private static final int SCROLLBAR_WIDTH = 4;
        private static final int SCROLLBAR_MARGIN = 3;
        private static final int SLOT_BOTTOM_PAD = 30;

        private final XuanLingTypeEnum type;

        private int panelW;
        private int panelH;
        private int px;
        private int py;

        private int gridX, gridY, gridW, gridH;
        private int cols;
        private int slotW, slotH;
        private int contentH;

        private float scrollY = 0f;
        private boolean draggingScrollbar = false;
        private float dragStartMouseY;
        private float dragStartScrollY;

        private CultivationPanel panel;
        private GuiButton backButton;
        private SectionDivider divider;

        private List<ArtSlot> slots;

        public ArcaneMasteryMenuGui(XuanLingTypeEnum type) {
                super(Component.literal("arcane_mastery_menu_gui"));
                this.type = type;
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }

        @Override
        protected void init() {
                this.panelW = clamp((int) (this.width * 0.55f), 360, 720);
                this.panelH = clamp((int) (this.height * 0.65f), 240, 540);

                this.px = (this.width - this.panelW) / 2;
                this.py = (this.height - this.panelH) / 2;

                panel = new CultivationPanel(this.panelW, this.panelH);
                panel.setPosition(this.px, this.py);

                int innerW = this.panelW - CultivationTheme.PADDING * 2;

                divider = new SectionDivider(innerW).setColors(0x40FFFFFF, CultivationTheme.BORDER_GOLD);
                divider.setPosition(this.px + CultivationTheme.PADDING, this.py + (int) (this.panelH * 0.14f));

                slots = buildHardcodedSlots();

                Component backComp = XuanLing.trans(XuanlingLangPro.GUI_STRING, "arcane_mastery_back");
                backButton = new GuiButton(60, 20, backComp.getString())
                                .onClick(() -> minecraft.setScreen(new ArcaneMasteryGui()));

                recomputeLayout();
        }

        /** 根据当前面板尺寸重新计算网格列数 / 卡片尺寸 / 内容高度 / 按钮位置 */
        private void recomputeLayout() {
                int innerW = this.panelW - CultivationTheme.PADDING * 2;
                int innerH = this.panelH - CultivationTheme.PADDING * 2;

                int usableH = (int) (innerH * 0.74f);
                int topOffset = (int) (this.panelH * 0.18f);
                this.gridX = this.px + CultivationTheme.PADDING;
                this.gridY = this.py + topOffset;
                this.gridW = innerW - SCROLLBAR_WIDTH - SCROLLBAR_MARGIN * 2;
                this.gridH = usableH;

                int targetSlot = clamp(innerW / 6, 64, 96);
                this.cols = Math.max(2, (this.gridW + GRID_GAP) / (targetSlot + GRID_GAP));
                this.slotW = (this.gridW - GRID_GAP * (cols - 1)) / cols;
                this.slotH = this.slotW;

                int rows = (int) Math.ceil((double) slots.size() / cols);
                this.contentH = rows * this.slotH + Math.max(0, rows - 1) * GRID_GAP;

                int btnY = this.py + this.panelH - SLOT_BOTTOM_PAD;
                backButton.setPosition(this.px + (this.panelW - 60) / 2, btnY);

                clampScroll();
        }

        private void clampScroll() {
                float max = getMaxScroll();
                this.scrollY = Mth.clamp(this.scrollY, 0, max);
        }

        private float getMaxScroll() {
                return Math.max(0f, contentH - gridH);
        }

        private List<ArtSlot> buildHardcodedSlots() {
                List<ArtSlot> list = new ArrayList<>();
                if (this.type == XuanLingTypeEnum.ART) {
                        list.add(new ArtSlot("太虚剑典", 5, true));
                        list.add(new ArtSlot("九阳神功", 4, true));
                        list.add(new ArtSlot("北冥心法", 3, false));
                        list.add(new ArtSlot("混元功", 2, false));
                        list.add(new ArtSlot("紫霄雷诀", 5, false));
                        list.add(new ArtSlot("玄冰真解", 3, false));
                        list.add(new ArtSlot("焚天秘典", 4, false));
                        list.add(new ArtSlot("万剑归宗", 5, false));
                        for (int i = list.size(); i < 24; i++) {
                                list.add(new ArtSlot(null, 0, false));
                        }
                } else {
                        list.add(new ArtSlot("天雷引", 4, true));
                        list.add(new ArtSlot("御剑术", 3, true));
                        list.add(new ArtSlot("缩地成寸", 5, false));
                        list.add(new ArtSlot("火球术", 1, true));
                        list.add(new ArtSlot("冰封千里", 4, false));
                        list.add(new ArtSlot("金刚不坏", 3, false));
                        list.add(new ArtSlot("大品天眼", 5, false));
                        list.add(new ArtSlot("他心通", 2, false));
                        for (int i = list.size(); i < 24; i++) {
                                list.add(new ArtSlot(null, 0, false));
                        }
                }
                return list;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                this.renderBackground(g, mouseX, mouseY, partialTick);
                panel.render(g, mouseX, mouseY, partialTick);

                int cx = panel.getX() + this.panelW / 2;
                String titleKey = (this.type == XuanLingTypeEnum.ART) ? "arcane_mastery_art_title" : "arcane_mastery_spell_title";
                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, titleKey);
                g.drawString(font, title, cx - font.width(title) / 2,
                                panel.getY() + (int) (this.panelH * 0.06f), CultivationTheme.BORDER_GOLD, false);

                divider.render(g, mouseX, mouseY, partialTick);

                g.enableScissor(gridX, gridY, gridX + gridW + SCROLLBAR_WIDTH + SCROLLBAR_MARGIN * 2, gridY + gridH);

                for (int i = 0; i < slots.size(); i++) {
                        int r = i / cols;
                        int c = i % cols;
                        int sx = gridX + c * (slotW + GRID_GAP);
                        int sy = gridY + r * (slotH + GRID_GAP) - (int) scrollY;

                        ArtSlot slot = slots.get(i);
                        slot.setSize(slotW, slotH);
                        slot.setPosition(sx, sy);

                        if (sy + slotH >= gridY && sy <= gridY + gridH) {
                                slot.render(g, mouseX, mouseY, partialTick);
                        }
                }

                g.disableScissor();

                renderScrollbar(g, mouseX, mouseY);
                backButton.render(g, mouseX, mouseY, partialTick);
        }

        private void renderScrollbar(GuiGraphics g, int mouseX, int mouseY) {
                float max = getMaxScroll();
                if (max <= 0f)
                        return;

                int barX = gridX + gridW + SCROLLBAR_MARGIN;
                g.fill(barX, gridY, barX + SCROLLBAR_WIDTH, gridY + gridH, 0x30000000);

                int thumbH = Math.max(20, (int) ((float) gridH / contentH * gridH));
                int thumbY = gridY + (int) ((gridH - thumbH) * (scrollY / max));

                boolean hover = mouseX >= barX && mouseX <= barX + SCROLLBAR_WIDTH
                                && mouseY >= thumbY && mouseY <= thumbY + thumbH;
                int color = (hover || draggingScrollbar) ? CultivationTheme.BORDER_GOLD : CultivationTheme.BORDER_CYAN;
                g.fill(barX, thumbY, barX + SCROLLBAR_WIDTH, thumbY + thumbH, color);
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (isInGrid(mx, my)) {
                        float max = getMaxScroll();
                        if (max > 0f) {
                                scrollY = Mth.clamp((float) (scrollY - sy * 18f), 0, max);
                                return true;
                        }
                }
                return super.mouseScrolled(mx, my, sx, sy);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (backButton.mouseClicked(mx, my, button))
                        return true;

                int barX = gridX + gridW + SCROLLBAR_MARGIN;
                float max = getMaxScroll();
                if (max > 0f && button == 0 && mx >= barX && mx <= barX + SCROLLBAR_WIDTH + 2 && my >= gridY
                                && my <= gridY + gridH) {
                        int thumbH = Math.max(20, (int) ((float) gridH / contentH * gridH));
                        int thumbY = gridY + (int) ((gridH - thumbH) * (scrollY / max));
                        if (my >= thumbY && my <= thumbY + thumbH) {
                                draggingScrollbar = true;
                                dragStartMouseY = (float) my;
                                dragStartScrollY = scrollY;
                                return true;
                        } else {
                                float ratio = (float) (my - gridY) / gridH;
                                scrollY = Mth.clamp(ratio * max, 0, max);
                                return true;
                        }
                }

                if (isInGrid(mx, my)) {
                        int barLimit = gridX + gridW;
                        for (int i = 0; i < slots.size(); i++) {
                                int r = i / cols;
                                int c = i % cols;
                                int sx = gridX + c * (slotW + GRID_GAP);
                                int sy = gridY + r * (slotH + GRID_GAP) - (int) scrollY;
                                ArtSlot slot = slots.get(i);
                                slot.setPosition(sx, sy);
                                slot.setSize(slotW, slotH);
                                if (mx < barLimit && slot.isMouseOver(mx, my)) {
                                        slot.onClick();
                                        return true;
                                }
                        }
                        return true;
                }
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (draggingScrollbar) {
                        float max = getMaxScroll();
                        int thumbH = Math.max(20, (int) ((float) gridH / contentH * gridH));
                        int track = gridH - thumbH;
                        if (track > 0) {
                                float delta = (float) ((my - dragStartMouseY) / track * max);
                                scrollY = Mth.clamp(dragStartScrollY + delta, 0, max);
                        }
                        return true;
                }
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (draggingScrollbar) {
                        draggingScrollbar = false;
                        return true;
                }
                return super.mouseReleased(mx, my, button);
        }

        @Override
        public void resize(net.minecraft.client.Minecraft minecraft, int width, int height) {
                super.resize(minecraft, width, height);
                this.width = width;
                this.height = height;
                this.init(minecraft, width, height);
        }

        private boolean isInGrid(double mx, double my) {
                return mx >= gridX && mx <= gridX + gridW + SCROLLBAR_WIDTH + SCROLLBAR_MARGIN * 2
                                && my >= gridY && my <= gridY + gridH;
        }

        private static int clamp(int v, int min, int max) {
                return Math.max(min, Math.min(max, v));
        }

        @Override
        public void onClose() {
                minecraft.setScreen(new ArcaneMasteryGui());
        }

        public static class ArtSlot implements IGuiComponent {
                private final String name;
                private final int rarity;
                private final boolean unlocked;
                private int x, y;
                private int width = 80;
                private int height = 80;
                private boolean hovered;
                private Runnable onClickAction;

                public ArtSlot(String name, int rarity, boolean unlocked) {
                        this.name = name;
                        this.rarity = rarity;
                        this.unlocked = unlocked;
                }

                public ArtSlot setOnClick(Runnable r) {
                        this.onClickAction = r;
                        return this;
                }

                public ArtSlot setSize(int w, int h) {
                        this.width = w;
                        this.height = h;
                        return this;
                }

                public void onClick() {
                        if (unlocked && onClickAction != null) {
                                onClickAction.run();
                        }
                }

                @Override
                public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                        this.hovered = isMouseOver(mouseX, mouseY);
                        int bgColor = unlocked ? 0x4000E5FF : 0x20FFFFFF;
                        int borderColor = unlocked ? getRarityColor() : CultivationTheme.BORDER_DIM;

                        g.fill(x, y, x + width, y + height, bgColor);
                        GuiRenderUtil.border(g, x, y, x + width, y + height,
                                        hovered ? CultivationTheme.BORDER_GOLD : borderColor);

                        var font = net.minecraft.client.Minecraft.getInstance().font;
                        if (unlocked && name != null) {
                                Component nameComp = Component.literal(name);
                                int textX = x + (width - font.width(nameComp)) / 2;
                                int textY = y + height - 18;
                                if (height >= 60) {
                                        g.drawString(font, nameComp, textX, textY, CultivationTheme.TEXT_PRIMARY,
                                                        false);

                                        String stars = "★".repeat(rarity);
                                        Component starComp = Component.literal(stars);
                                        int starX = x + (width - font.width(starComp)) / 2;
                                        g.drawString(font, starComp, starX, y + 6, getRarityColor(), false);
                                } else {
                                        g.drawString(font, nameComp, textX, y + (height - 8) / 2,
                                                        CultivationTheme.TEXT_PRIMARY, false);
                                }
                        } else {
                                Component empty = XuanLing.trans(XuanlingLangPro.GUI_STRING, "arcane_mastery_empty_slot");
                                int textX = x + (width - font.width(empty)) / 2;
                                g.drawString(font, empty, textX, y + height / 2 - 4, CultivationTheme.TEXT_SECONDARY,
                                                false);
                        }
                }

                private int getRarityColor() {
                        return switch (rarity) {
                                case 5 -> 0xFFFF4D4D;
                                case 4 -> 0xFFFFC94D;
                                case 3 -> 0xFFB44DFF;
                                case 2 -> 0xFF4DFF7B;
                                default -> CultivationTheme.BORDER_CYAN;
                        };
                }

                @Override
                public void setPosition(int x, int y) {
                        this.x = x;
                        this.y = y;
                }

                @Override
                public int getX() {
                        return x;
                }

                @Override
                public int getY() {
                        return y;
                }

                @Override
                public int getWidth() {
                        return width;
                }

                @Override
                public int getHeight() {
                        return height;
                }
        }
}
