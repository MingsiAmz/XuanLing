package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.DataManager;
import com.mingsiamz.xuanlingmod.gui.component.canvas.CanvasPanel;
import com.mingsiamz.xuanlingmod.gui.component.canvas.EditorState;
import com.mingsiamz.xuanlingmod.gui.component.data.ComponentDef;
import com.mingsiamz.xuanlingmod.gui.component.data.ComponentRegistry;
import com.mingsiamz.xuanlingmod.gui.component.palette.PalettePanel;
import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

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

        private ComponentDef draggingDef;

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

                canvas = new CanvasPanel(state,
                                this.width - LEFT_W - GAP - MARGIN * 2, totalH);
                canvas.setPosition(MARGIN + LEFT_W + GAP, MARGIN);

                List<ComponentDef> defs = ComponentRegistry.getAvailable(
                                minecraft == null ? null : minecraft.player);
                palette.loadComponents(defs);
        }

        private void onCreate() {
                DataManager.saveData(state, "skills");
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
                        int w = draggingDef.width();
                        int h = draggingDef.height();
                        int gx = mouseX - w / 2;
                        int gy = mouseY - h / 2;
                        g.fill(gx, gy, gx + w, gy + h,
                                        0x80000000 | (draggingDef.color() & 0x00FFFFFF));
                        GuiRenderUtil.border(g, gx, gy, gx + w, gy + h, draggingDef.color());
                        g.drawString(font, Component.literal(draggingDef.displayName()),
                                        gx + 6, gy + 4, CultivationTheme.TEXT_PRIMARY, false);
                }

                createButton.render(g, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
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
                if (canvas.mouseDragged(mx, my, button, dx, dy))
                        return true;
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (draggingDef != null) {
                        if (canvas.contains(mx, my)) {
                                int cx = canvas.toCanvasX((int) mx) - draggingDef.width() / 2;
                                int cy = canvas.toCanvasY((int) my) - draggingDef.height() / 2;
                                canvas.addNode(draggingDef, cx, cy);
                        } else if (palette.contains(mx, my)) {
                                int cx = canvas.getWidth() / 2 - draggingDef.width() / 2 - canvas.getPanX();
                                int cy = canvas.getHeight() / 2 - draggingDef.height() / 2 - canvas.getPanY();
                                canvas.addNode(draggingDef, cx, cy);
                        }
                        draggingDef = null;
                        return true;
                }
                if (canvas.mouseReleased(mx, my, button))
                        return true;
                return super.mouseReleased(mx, my, button);
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }
}
