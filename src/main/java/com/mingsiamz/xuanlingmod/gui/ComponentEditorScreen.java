package com.mingsiamz.xuanlingmod.gui;

import java.util.List;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.ComponentGroup;
import com.mingsiamz.xuanlingmod.data.component.SpellComponent;
import com.mingsiamz.xuanlingmod.data.controller.DataManager;
import com.mingsiamz.xuanlingmod.gui.component.DragPayload;
import com.mingsiamz.xuanlingmod.gui.component.canvas.CanvasNode;
import com.mingsiamz.xuanlingmod.gui.component.canvas.CanvasPanel;
import com.mingsiamz.xuanlingmod.gui.component.canvas.EditorState;
import com.mingsiamz.xuanlingmod.gui.component.canvas.ParamPopup;
import com.mingsiamz.xuanlingmod.gui.component.palette.PalettePanel;
import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.RegistryComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.LocalPlayer;
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
        private XuanLingTypeEnum type;
        private String name = "";
        private ComponentGroup componentGroup = new ComponentGroup();

        private boolean showNamePopup = false;
        private EditBox nameInput;
        private GuiButton confirmButton;
        private GuiButton cancelButton;

        private int popupX;
        private int popupY;
        private final int popupW = 240;
        private final int popupH = 110;
        private int inputX;
        private int inputY;
        private int inputW;
        private static final int INPUT_H = 18;

        public ComponentEditorScreen(XuanLingTypeEnum type) {
                super(Component.literal("make cultivation method"));
                this.type = type;
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
                // createButton.onClick(this::onCreate);
                createButton.onClick(this::openNamePopup);

                canvas = new CanvasPanel(state, this.width - LEFT_W - GAP - MARGIN * 2, totalH);
                canvas.setPosition(MARGIN + LEFT_W + GAP, MARGIN);

                List<ImpComponent> defs = RegistryComponent.getAvailable(this.type,
                                minecraft == null ? null : minecraft.player);
                palette.loadComponents(defs);

                computePopupLayout();
                nameInput = new EditBox(this.font, inputX, inputY, inputW, INPUT_H, Component.literal("name"));
                nameInput.setMaxLength(64);
                nameInput.setBordered(false);
                nameInput.setVisible(false);
                nameInput.setValue("");

                confirmButton = new GuiButton(80, 20,
                                XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation").getString());
                cancelButton = new GuiButton(80, 20,
                                XuanLing.trans(XuanlingLangPro.GUI_STRING, "give_up").getString());
                confirmButton.onClick(this::confirmName);
                cancelButton.onClick(this::closeNamePopup);

                int btnY = popupY + popupH - 32;
                int spacing = 10;
                int startX = popupX + (popupW - (80 * 2 + spacing)) / 2;
                confirmButton.setPosition(startX, btnY);
                cancelButton.setPosition(startX + 80 + spacing, btnY);
        }

        private void onCreate() {
                if (this.componentGroup == null || this.componentGroup.isEmpty())
                        return;
                LocalPlayer player = minecraft.player;
                if (minecraft != null && player != null) {
                        switch (this.type) {
                                case SPELL:
                                        SpellComponent spell = new SpellComponent(this.name, this.componentGroup);
                                        spell.toJson(player);
                                        break;

                                default:
                                        break;
                        }
                        player.displayClientMessage(Component
                                        .literal(XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation") + ":"
                                                        + this.name),
                                        false);
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

                if (showNamePopup)
                        renderNamePopup(g, mouseX, mouseY, partialTick);

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
                if (showNamePopup) {
                        if (confirmButton.mouseClicked(mx, my, button))
                                return true;
                        if (cancelButton.mouseClicked(mx, my, button))
                                return true;
                        if (mx >= inputX - 2 && mx <= inputX + inputW + 2
                                        && my >= inputY - 2 && my <= inputY + INPUT_H + 2) {
                                nameInput.setFocused(true);
                        } else {
                                nameInput.setFocused(false);
                        }
                        return true;
                }

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
                                                String compType = dragPayload.component.type();

                                                if (!CanvasNode.canJoinGroup(targetGroup, dragPayload.component)) {
                                                        this.toastMessage = XuanLing.trans(XuanlingLangPro.GUI_STRING,
                                                                        "the_component_not_join_group");
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
                                                componentGroup.addComponent(this.type, compType, dragPayload.component);
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

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (keyCode == 257 || keyCode == 335) {
                        confirmName();
                        return true;
                }
                if (keyCode == 1) {
                        closeNamePopup();
                        return true;
                }
                if (nameInput.keyPressed(keyCode, scanCode, modifiers))
                        return true;
                return super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
                if (showNamePopup && nameInput.charTyped(codePoint, modifiers))
                        return true;
                return super.charTyped(codePoint, modifiers);
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

        @Override
        public void onClose() {
                minecraft.setScreen(new CreateGui());
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

        private void computePopupLayout() {
                this.popupX = (this.width - popupW) / 2;
                this.popupY = (this.height - popupH) / 2;
                this.inputW = popupW - 40;
                this.inputX = popupX + 20;
                this.inputY = popupY + 40;
        }

        private void openNamePopup() {
                this.showNamePopup = true;
                this.nameInput.setValue(this.name == null ? "" : this.name);
                this.nameInput.setVisible(true);
                this.nameInput.setFocused(true);
        }

        private void closeNamePopup() {
                this.showNamePopup = false;
                this.nameInput.setVisible(false);
                this.nameInput.setFocused(false);
        }

        private void confirmName() {
                this.name = this.nameInput.getValue().trim();
                closeNamePopup();
                onCreate();
        }

        private void renderNamePopup(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                g.fill(0, 0, this.width, this.height, CultivationTheme.BG_MASK);

                g.fill(popupX, popupY, popupX + popupW, popupY + popupH, CultivationTheme.PANEL_BG);
                GuiRenderUtil.border(g, popupX, popupY, popupX + popupW, popupY + popupH, CultivationTheme.BORDER_GOLD);

                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, "input_cultivation_name");
                g.drawString(font, title, popupX + (popupW - font.width(title)) / 2, popupY + 14,
                                CultivationTheme.TEXT_GOLD, true);

                g.fill(inputX - 2, inputY - 2, inputX + inputW + 2, inputY + INPUT_H + 2,
                                CultivationTheme.PANEL_BG_DARK);
                GuiRenderUtil.border(g, inputX - 2, inputY - 2, inputX + inputW + 2, inputY + INPUT_H + 2,
                                CultivationTheme.BORDER_CYAN);

                String text = nameInput.getValue();
                int textW = font.width(text);
                int textX = inputX + (inputW - textW) / 2;
                int textY = inputY + (INPUT_H - 8) / 2;
                g.drawString(font, text, textX, textY, CultivationTheme.TEXT_PRIMARY, false);

                if (nameInput.isFocused() && (System.currentTimeMillis() / 500) % 2 == 0) {
                        g.fill(textX + textW + 1, textY - 1, textX + textW + 2, textY + 9,
                                        CultivationTheme.TEXT_PRIMARY);
                }

                confirmButton.render(g, mouseX, mouseY, partialTick);
                cancelButton.render(g, mouseX, mouseY, partialTick);
        }
}
