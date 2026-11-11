package com.mingsiamz.xuanlingmod.gui;

import java.util.List;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.data.DataManager;
import com.mingsiamz.xuanlingmod.gui.component.canvas.CanvasNode;
import com.mingsiamz.xuanlingmod.gui.component.canvas.CanvasPanel;
import com.mingsiamz.xuanlingmod.gui.component.canvas.EditorState;
import com.mingsiamz.xuanlingmod.gui.component.canvas.ParamPopup;
import com.mingsiamz.xuanlingmod.gui.component.ComponentRegistry;
import com.mingsiamz.xuanlingmod.gui.component.palette.PalettePanel;
import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.Minecraft;
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

        private int draggingW = 110;
        private int draggingH = 46;

        private PalettePanel palette;
        private CanvasPanel canvas;
        private GuiButton createButton;

        private String toastMessage = null;
        private long toastTime = 0;

        private IComponent<?> draggingDef;

        private ParamPopup activePopup = null;

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
                palette.setOnDragStart((def, mx, my) -> draggingDef = def);

                createButton = new GuiButton(
                                LEFT_W, BUTTON_H,
                                XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation").getString());
                createButton.setPosition(MARGIN, MARGIN + paletteH + BUTTON_GAP);
                createButton.onClick(this::onCreate);

                canvas = new CanvasPanel(state, this.width - LEFT_W - GAP - MARGIN * 2, totalH);
                canvas.setPosition(MARGIN + LEFT_W + GAP, MARGIN);

                List<IComponent> defs = ComponentRegistry.getAvailable(
                                minecraft == null ? null : minecraft.player);

                palette.loadComponents(defs);
        }

        private void onCreate() {
                DataManager.saveData(state, null, minecraft.player.getUUID().toString(), "skills");
                if (minecraft != null && minecraft.player != null) {
                        minecraft.player.displayClientMessage(
                                        Component.literal("§a已导出：skills.json"), false);
                }
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                this.renderBackground(g, mouseX, mouseY, partialTick);

                palette.render(g, mouseX, mouseY, partialTick);
                canvas.render(g, mouseX, mouseY, partialTick);

                if (draggingDef != null) {
                        int gx = mouseX - draggingW / 2;
                        int gy = mouseY - draggingH / 2;
                        g.fill(gx, gy, gx + draggingW, gy + draggingH, 0x80000000 | (0xFF4DD0E1 & 0x00FFFFFF));
                        GuiRenderUtil.border(g, gx, gy, gx + draggingW, gy + draggingH, 0xFF4DD0E1);
                        g.drawString(font, draggingDef.trans(), gx + 6, gy + 4, CultivationTheme.TEXT_PRIMARY, false);
                }

                createButton.render(g, mouseX, mouseY, partialTick);

                if (activePopup != null) {
                        activePopup.render(g, mouseX, mouseY);
                }

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
                if (elapsed > 1500) {
                        alpha = 1.0f - (elapsed - 1500) / 500.0f;
                }
                int alphaInt = (int) (alpha * 255) << 24;

                int textW = font.width(toastMessage);
                int boxW = textW + 12;
                int boxH = 16;
                int boxX = mouseX - boxW / 2;
                int boxY = mouseY - 25;
                if (boxX < 5)
                        boxX = 5;
                if (boxX + boxW > this.width - 5)
                        boxX = this.width - boxW - 5;
                if (boxY < 5)
                        boxY = mouseY + 15;
                int bgColor = (0xE0101820 & 0x00FFFFFF) | alphaInt;
                int borderColor = (0xFFFF5555 & 0x00FFFFFF) | alphaInt;
                g.fill(boxX, boxY, boxX + boxW, boxY + boxH, bgColor);
                GuiRenderUtil.border(g, boxX, boxY, boxX + boxW, boxY + boxH, borderColor);

                int textColor = (0xFFFF5555 & 0x00FFFFFF) | alphaInt;
                g.drawString(font, toastMessage, boxX + 6, boxY + 4, textColor, false);
        }

        private void addNodeToCanvas(IComponent def, int cx, int cy) {
                CanvasNode node = new CanvasNode(def, cx, cy);
                node.setOnConfigClick(() -> {
                        if (def.getParams() == null || def.getParams().isEmpty()) {
                                this.toastMessage = "该组件无法衍化更多";
                                this.toastTime = System.currentTimeMillis();
                                return;
                        }

                        int popupX = canvas.toScreenX(node.getCanvasX() + node.getWidth() + 10);
                        int popupY = canvas.toScreenY(node.getCanvasY());

                        if (popupX + 160 > this.width) {
                                popupX = canvas.toScreenX(node.getCanvasX()) - 170;
                        }

                        activePopup = new ParamPopup(def, popupX, popupY);
                });
                state.nodes.add(node);
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
                if (draggingDef != null)
                        return true;
                if (palette.mouseDragged(mx, my, button, dx, dy))
                        return true;
                if (canvas.mouseDragged(mx, my, button, dx, dy))
                        return true;
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (palette.mouseReleased(mx, my, button))
                        return true;

                if (draggingDef != null) {
                        if (canvas.contains(mx, my)) {
                                int cx = canvas.toCanvasX((int) mx) - draggingW / 2;
                                int cy = canvas.toCanvasY((int) my) - draggingH / 2;
                                addNodeToCanvas(draggingDef, cx, cy);
                        } else if (palette.contains(mx, my)) {
                                int cx = canvas.getWidth() / 2 - draggingW / 2 - canvas.getPanX();
                                int cy = canvas.getHeight() / 2 - draggingH / 2 - canvas.getPanY();
                                addNodeToCanvas(draggingDef, cx, cy);
                        }
                        draggingDef = null;
                        return true;
                }

                if (canvas.mouseReleased(mx, my, button))
                        return true;
                return super.mouseReleased(mx, my, button);
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (activePopup != null && activePopup.isMouseOver(mx, my))
                        return true;
                if (palette.mouseScrolled(mx, my, sx, sy))
                        return true;
                return super.mouseScrolled(mx, my, sx, sy);
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }
}
