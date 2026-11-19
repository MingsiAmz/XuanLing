package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.client.ArtSpellCache;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.network.StoreDataPacket;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterItems;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.util.gui.CultivationPanel;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import com.mingsiamz.xuanlingmod.util.gui.component.GuiButton;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JadeSlipStorageGui extends Screen {

        private static final int GRID_GAP = 8;
        private static final int SCROLLBAR_WIDTH = 4;
        private static final int SCROLLBAR_MARGIN = 3;
        private static final int SLOT_BOTTOM_PAD = 30;
        private static final int SLOT_LABEL_HEIGHT = 12;

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

        private List<String> names;

        public JadeSlipStorageGui(XuanLingTypeEnum type) {
                super(Component.literal("jade_slip_storage_gui"));
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

                names = getNames();

                Component backComp = XuanLing.trans(XuanlingLangPro.GUI_STRING, "arcane_mastery_back");
                backButton = new GuiButton(60, 20, backComp.getString())
                                .onClick(() -> minecraft.setScreen(null));

                recomputeLayout();
        }

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
                this.slotH = this.slotW + SLOT_LABEL_HEIGHT;

                int rows = (int) Math.ceil((double) Math.max(1, names.size()) / cols);
                this.contentH = rows * this.slotH + Math.max(0, rows - 1) * GRID_GAP;

                int btnY = this.py + this.panelH - SLOT_BOTTOM_PAD;
                backButton.setPosition(this.px + (this.panelW - 60) / 2, btnY);

                clampScroll();
        }

        private void clampScroll() {
                float max = getMaxScroll();
                this.scrollY = clampFloat(this.scrollY, 0, max);
        }

        private float getMaxScroll() {
                return Math.max(0f, contentH - gridH);
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                this.renderBackground(g, mouseX, mouseY, partialTick);
                panel.render(g, mouseX, mouseY, partialTick);

                int cx = panel.getX() + this.panelW / 2;
                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, "jade_slip_storage_title");
                g.drawString(font, title, cx - font.width(title) / 2,
                                panel.getY() + (int) (this.panelH * 0.06f), CultivationTheme.BORDER_GOLD, false);

                g.enableScissor(gridX, gridY, gridX + gridW + SCROLLBAR_WIDTH + SCROLLBAR_MARGIN * 2, gridY + gridH);

                int rows = (int) Math.ceil((double) Math.max(1, names.size()) / cols);
                for (int i = 0; i < rows; i++) {
                        for (int c = 0; c < cols; c++) {
                                int idx = i * cols + c;
                                if (names.isEmpty()) {
                                        break;
                                }
                                if (idx >= names.size()) {
                                        break;
                                }
                                String name = names.get(idx);
                                int sx = gridX + c * (slotW + GRID_GAP);
                                int sy = gridY + i * (slotH + GRID_GAP) - (int) scrollY;
                                renderSlot(g, sx, sy, slotW, slotH, name, mouseX, mouseY);
                        }
                }

                g.disableScissor();

                renderScrollbar(g, mouseX, mouseY);
                backButton.render(g, mouseX, mouseY, partialTick);
        }

        private void renderSlot(GuiGraphics g, int x, int y, int w, int h, String name, int mouseX, int mouseY) {
                int boxHeight = Math.max(20, h - SLOT_LABEL_HEIGHT);
                boolean hovered = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + boxHeight;

                g.fill(x, y, x + w, y + boxHeight, 0x3000E5FF);
                GuiRenderUtil.border(g, x, y, x + w, y + boxHeight,
                                hovered ? CultivationTheme.BORDER_GOLD : CultivationTheme.BORDER_CYAN);

                var font = net.minecraft.client.Minecraft.getInstance().font;
                Component nameComp = Component.literal(name);
                int textX = x + (w - font.width(nameComp)) / 2;
                int textY = y + (boxHeight - 8) / 2;
                g.drawString(font, nameComp, textX, textY, CultivationTheme.TEXT_PRIMARY, false);
        }

        private void renderScrollbar(GuiGraphics g, int mouseX, int mouseY) {
                float max = getMaxScroll();
                if (max <= 0f)
                        return;

                int barX = gridX + gridW + SCROLLBAR_MARGIN;
                g.fill(barX, gridY, barX + SCROLLBAR_WIDTH, gridY + gridH, 0x30000000);

                int thumbH = Math.max(20, (int) ((float) gridH / Math.max(1f, contentH) * gridH));
                int thumbY = gridY + (int) ((gridH - thumbH) * (scrollY / max));

                boolean hover = mouseX >= barX && mouseX <= barX + SCROLLBAR_WIDTH
                                && mouseY >= thumbY && mouseY <= thumbY + thumbH;
                int color = (hover || draggingScrollbar) ? CultivationTheme.BORDER_GOLD : CultivationTheme.BORDER_CYAN;
                g.fill(barX, thumbY, barX + SCROLLBAR_WIDTH, thumbY + thumbH, color);
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (mx >= gridX && mx <= gridX + gridW + SCROLLBAR_WIDTH + SCROLLBAR_MARGIN * 2
                                && my >= gridY && my <= gridY + gridH) {
                        float max = getMaxScroll();
                        if (max > 0f) {
                                scrollY = clampFloat((float) (scrollY - sy * 18f), 0, max);
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
                        int thumbH = Math.max(20, (int) ((float) gridH / Math.max(1f, contentH) * gridH));
                        int thumbY = gridY + (int) ((gridH - thumbH) * (scrollY / max));
                        if (my >= thumbY && my <= thumbY + thumbH) {
                                draggingScrollbar = true;
                                dragStartMouseY = (float) my;
                                dragStartScrollY = scrollY;
                                return true;
                        } else {
                                float ratio = (float) (my - gridY) / gridH;
                                scrollY = clampFloat(ratio * max, 0, max);
                                return true;
                        }
                }

                if (mx >= gridX && mx <= gridX + gridW + SCROLLBAR_WIDTH + SCROLLBAR_MARGIN * 2
                                && my >= gridY && my <= gridY + gridH) {
                        int barLimit = gridX + gridW;
                        int rows = (int) Math.ceil((double) Math.max(1, names.size()) / cols);
                        for (int i = 0; i < rows; i++) {
                                for (int c = 0; c < cols; c++) {
                                        int idx = i * cols + c;
                                        if (names.isEmpty() || idx >= names.size()) {
                                                break;
                                        }
                                        String name = names.get(idx);
                                        int sx = gridX + c * (slotW + GRID_GAP);
                                        int sy = gridY + i * (slotH + GRID_GAP) - (int) scrollY;
                                        if (mx < barLimit && mx >= sx && mx < sx + slotW && my >= sy
                                                        && my < sy + slotH) {
                                                if (button == 0) {
                                                        storeSelected(name);
                                                }
                                                return true;
                                        }
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
                        int thumbH = Math.max(20, (int) ((float) gridH / Math.max(1f, contentH) * gridH));
                        int track = gridH - thumbH;
                        if (track > 0) {
                                float delta = (float) ((my - dragStartMouseY) / track * max);
                                scrollY = clampFloat(dragStartScrollY + delta, 0, max);
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

        private void storeSelected(String name) {
                if (minecraft.player == null)
                        return;
                ItemStack held = minecraft.player.getItemInHand(InteractionHand.MAIN_HAND);
                if (held.getItem() != RegisterItems.JADE_SLIP.get()) {
                        XuanLing.LOGGER.info("玉简存储：手持物品不是玉简，操作取消");
                        return;
                }
                Networking.sendToServer(new StoreDataPacket(this.type, name));
                minecraft.setScreen(null);
        }

        private List<String> getNames() {
                if (ArtSpellCache.isInitialized()) {
                        List<String> cached = ArtSpellCache.getByType(this.type);
                        if (cached != null && !cached.isEmpty()) {
                                return new ArrayList<>(cached);
                        }
                        return new ArrayList<>();
                }

                CompoundTag nbt = minecraft.player.getPersistentData()
                                .getCompound(XuanLing.MODID + this.type.type);
                if (nbt == null || nbt.isEmpty())
                        return new ArrayList<>();

                Iterator<String> it = nbt.getAllKeys().iterator();
                ArrayList<String> list = new ArrayList<>();
                while (it.hasNext()) {
                        String name = nbt.getString(it.next());
                        list.add(name);
                }
                return list;
        }

        private static int clamp(int v, int min, int max) {
                return Math.max(min, Math.min(max, v));
        }

        private static float clampFloat(float v, float min, float max) {
                return Math.max(min, Math.min(max, v));
        }

        @Override
        public void resize(net.minecraft.client.Minecraft minecraft, int width, int height) {
                super.resize(minecraft, width, height);
                this.width = width;
                this.height = height;
                this.init(minecraft, width, height);
        }

        @Override
        public void onClose() {
                minecraft.setScreen(null);
        }
}
