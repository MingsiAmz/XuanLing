package com.mingsiamz.xuanlingmod.gui;

import java.util.List;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.data.DataManager;
import com.mingsiamz.xuanlingmod.gui.component.ComponentRegistry;
import com.mingsiamz.xuanlingmod.gui.component.DragPayload;
import com.mingsiamz.xuanlingmod.gui.component.canvas.CanvasNode;
import com.mingsiamz.xuanlingmod.gui.component.canvas.CanvasPanel;
import com.mingsiamz.xuanlingmod.gui.component.canvas.EditorState;
import com.mingsiamz.xuanlingmod.gui.component.canvas.ParamPopup;
import com.mingsiamz.xuanlingmod.gui.component.palette.PalettePanel;
import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ComponentEditorScreen extends Screen {

        private static final int MARGIN = 10;
        private static final int LEFT_W = 180;
        private static final int GAP = 8;
        private static final int BUTTON_H = 20;
        private static final int BUTTON_GAP = 6;

        private final EditorState state = new EditorState();

        private PalettePanel palette;
        private CanvasPanel canvas;
        private GuiButton createButton;

        private Component toastMessage = null;
        private long toastTime = 0;
        private ParamPopup activePopup = null;

        private DragPayload dragPayload = null;

        public ComponentEditorScreen() {
                super(Component.literal("make cultivation method"));
        }

        @Override
        protected void init() {
                super.init();

                int totalH = this.height - MARGIN * 2;
                int paletteH = totalH - BUTTON_H - BUTTON_GAP;

                palette = new PalettePanel(LEFT_W, paletteH);
                palette.setPosition(MARGIN, MARGIN);
                palette.setOnDragStart((payload, pos) -> dragPayload = payload);

                createButton = new GuiButton(
                                LEFT_W, BUTTON_H,
                                XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation").getString());
                createButton.setPosition(MARGIN, MARGIN + paletteH + BUTTON_GAP);
                createButton.onClick(this::onCreate);

                canvas = new CanvasPanel(state, this.width - LEFT_W - GAP - MARGIN * 2, totalH);
                canvas.setPosition(MARGIN + LEFT_W + GAP, MARGIN);

                List<IComponent<?>> defs = ComponentRegistry.getAvailable(minecraft == null ? null : minecraft.player);
                palette.loadComponents(defs);
        }

        private void onCreate() {
                DataManager.saveData(state, null, minecraft.player.getUUID().toString(), "skills");
                if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(Component.literal("§a已导出：skills.json"), false);
                }
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                this.renderBackground(g, mouseX, mouseY, partialTick);

                palette.render(g, mouseX, mouseY, partialTick);
                canvas.render(g, mouseX, mouseY, partialTick);

                if (dragPayload != null) {
                        int w = 110;
                        int h = 30;
                        int gx = mouseX - w / 2;
                        int gy = mouseY - h / 2;
                        if (dragPayload.type == DragPayload.Type.CATEGORY) {
                                g.fill(gx, gy, gx + w, gy + h, 0x80000000 | (0xFFFFC94D & 0x00FFFFFF));
                                GuiRenderUtil.border(g, gx, gy, gx + w, gy + h, 0xFFFFC94D);
                                g.drawString(font, dragPayload.categoryName, gx + 6, gy + 11,
                                                CultivationTheme.TEXT_GOLD, false);
                        } else {
                                g.fill(gx, gy, gx + w, gy + h, 0x80000000 | (0xFF4DD0E1 & 0x00FFFFFF));
                                GuiRenderUtil.border(g, gx, gy, gx + w, gy + h, 0xFF4DD0E1);
                                g.drawString(font, dragPayload.component.trans(), gx + 6, gy + 11,
                                                CultivationTheme.TEXT_PRIMARY, false);
                        }
                }

                createButton.render(g, mouseX, mouseY, partialTick);

                if (activePopup != null)
                        activePopup.render(g, mouseX, mouseY);

                renderToast(g, mouseX, mouseY);
        }

        private void renderToast(GuiGraphics g, int mouseX, int mouseY) {
                if (toastMessage == null)
                        return;

                long elapsed = System.currentTimeMillis() - toastTime;
                if (elapsed > 2000) {
                        toastMessage = null;
                        return;
                }

                float alpha = 1.0f;
                if (elapsed > 1500)
                        alpha = 1.0f - (elapsed - 1500) / 500.0f;

                int alphaInt = (int) (alpha * 255) << 24;
                int textW = font.width(toastMessage);
                int boxW = textW + 12;
                int boxH = 16;
                int boxX = Math.max(5, Math.min(this.width - boxW - 5, mouseX - boxW / 2));
                int boxY = (mouseY - 25 < 5) ? mouseY + 15 : mouseY - 25;

                int bgColor = (0xE0101820 & 0x00FFFFFF) | alphaInt;
                int borderColor = (0xFFFF5555 & 0x00FFFFFF) | alphaInt;
                int textColor = (0xFFFF5555 & 0x00FFFFFF) | alphaInt;

                g.fill(boxX, boxY, boxX + boxW, boxY + boxH, bgColor);
                GuiRenderUtil.border(g, boxX, boxY, boxX + boxW, boxY + boxH, borderColor);
                g.drawString(font, toastMessage, boxX + 6, boxY + 4, textColor, false);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (activePopup != null) {
                        if (activePopup.isMouseOver(mx, my)) {
                                activePopup.mouseClicked(mx, my, button);
                                return true;
                        } else {
                                activePopup = null;
                        }
                }

                if (createButton.mouseClicked(mx, my, button))
                        return true;
                if (palette.mouseClicked(mx, my, button))
                        return true;
                if (canvas.mouseClicked(mx, my, button))
                        return true;
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (palette.mouseDragged(mx, my, button, dx, dy))
                        return true;
                if (canvas.mouseDragged(mx, my, button, dx, dy))
                        return true;
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                boolean paletteHandled = palette.mouseReleased(mx, my, button);
                boolean canvasHandled = canvas.mouseReleased(mx, my, button);

                if (dragPayload != null) {
                        if (canvas.contains(mx, my)) {
                                float cx = canvas.toCanvasX(mx);
                                float cy = canvas.toCanvasY(my);

                                if (dragPayload.type == DragPayload.Type.CATEGORY) {
                                        state.nodes.add(new CanvasNode(dragPayload.categoryName, (int) cx - 70,
                                                        (int) cy - 20));
                                } else {
                                        int dragW = 110;
                                        int dragH = 46;
                                        int dragX = (int) cx - dragW / 2;
                                        int dragY = (int) cy - dragH / 2;
                                        int magnet = 20;

                                        CanvasNode targetGroup = null;
                                        for (int i = state.nodes.size() - 1; i >= 0; i--) {
                                                CanvasNode node = state.nodes.get(i);
                                                if (node.isGroup()) {
                                                        int nx = node.getCanvasX();
                                                        int ny = node.getCanvasY();

                                                        int expandW = node.getWidth() + magnet * 2;
                                                        int expandH = node.getHeight() + magnet * 2;

                                                        if (node.hitTest(nx, ny, cx, cy) ||
                                                                        intersects(dragX, dragY, dragW, dragH,
                                                                                        nx - magnet, ny - magnet,
                                                                                        expandW, expandH)) {
                                                                targetGroup = node;
                                                                break;
                                                        }
                                                }
                                        }

                                        if (targetGroup != null) {
                                                String groupType = targetGroup.getGroupName();
                                                String compType = dragPayload.component.transType().getString();

                                                if (!groupType.equals(compType)) {
                                                        this.toastMessage = Component.literal("§c该组件无法放入此分类！");
                                                        this.toastTime = System.currentTimeMillis();

                                                        CanvasNode normalNode = new CanvasNode(dragPayload.component,
                                                                        (int) cx - 55, (int) cy - 23);
                                                        setupNodeClick(normalNode);
                                                        state.nodes.add(normalNode);
                                                } else {
                                                        CanvasNode childNode = new CanvasNode(dragPayload.component, 0,
                                                                        0);
                                                        setupNodeClick(childNode);
                                                        targetGroup.children.add(childNode);
                                                }
                                        } else {
                                                CanvasNode normalNode = new CanvasNode(dragPayload.component,
                                                                (int) cx - 55, (int) cy - 23);
                                                setupNodeClick(normalNode);
                                                state.nodes.add(normalNode);
                                        }
                                }
                        }
                        dragPayload = null;
                        return true;
                }

                return paletteHandled || canvasHandled || super.mouseReleased(mx, my, button);
        }

        private boolean intersects(int ax, int ay, int aw, int ah, int bx, int by, int bw, int bh) {
                return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (activePopup != null && activePopup.isMouseOver(mx, my))
                        return true;
                if (palette.mouseScrolled(mx, my, sx, sy))
                        return true;
                if (canvas.mouseScrolled(mx, my, sx, sy))
                        return true;
                return super.mouseScrolled(mx, my, sx, sy);
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }

        private void setupNodeClick(CanvasNode node) {
                node.setOnConfigClick(() -> {
                        if (node.getDef() == null)
                                return;
                        if (node.getDef().getParams() == null || node.getDef().getParams().isEmpty()) {
                                this.toastMessage = XuanLing.trans(XuanlingLangPro.GUI_STRING,
                                                "this_component_not_select_more");
                                this.toastTime = System.currentTimeMillis();
                                return;
                        }

                        int popupX = canvas.toScreenX(node.getCanvasX() + node.getWidth() + 10);
                        int popupY = canvas.toScreenY(node.getCanvasY());

                        if (popupX + 160 > this.width)
                                popupX = canvas.toScreenX(node.getCanvasX()) - 170;

                        activePopup = new ParamPopup(node.getDef(), popupX, popupY);
                });
        }
}
