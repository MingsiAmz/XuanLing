package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.*;
import com.mingsiamz.xuanlingmod.util.gui.layout.Justify;
import com.mingsiamz.xuanlingmod.util.gui.layout.Layouts;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class LevelProgressGui extends Screen {

        private int panelW;
        private int panelH;
        private int px;
        private int py;

        private CultivationPanel panel;
        private CultivationSlider timeSlider;
        private EditBox timeInput;

        private static int MAX_MONTHS = 1000;
        private int currentSelectedMonths = 0;

        private boolean isSyncing = false;

        public LevelProgressGui() {
                super(Component.literal("level_progress_gui"));
        }

        @Override
        protected void init() {
                super.init();

                this.panelW = Math.max(300, Math.min(400, (int) (this.width * 0.35f)));
                this.panelH = Math.max(200, Math.min(280, (int) (this.height * 0.45f)));
                this.px = (this.width - panelW) / 2;
                this.py = (this.height - panelH) / 2;

                panel = new CultivationPanel(panelW, panelH);
                panel.setPosition(px, py);

                int innerW = panelW - CultivationTheme.PADDING * 2;

                int sliderY = py + (int) (panelH * 0.40f);
                timeSlider = new CultivationSlider(innerW, 10);
                timeSlider.setPosition(px + CultivationTheme.PADDING, sliderY);

                timeSlider.setOnValueChanged(val -> {
                        if (isSyncing)
                                return;
                        isSyncing = true;
                        currentSelectedMonths = (int) (val * MAX_MONTHS);
                        updateInputBox(currentSelectedMonths);
                        isSyncing = false;
                });

                int inputW = 50;
                int cx = px + panelW / 2;
                int inputX = cx - inputW / 2;
                int inputY = sliderY + 32;

                timeInput = new EditBox(this.font, inputX, inputY, inputW, 10, Component.literal("Months"));
                timeInput.setMaxLength(4);
                timeInput.setBordered(false);
                timeInput.setVisible(true);
                timeInput.setValue("0");

                timeInput.setResponder(text -> {
                        if (isSyncing)
                                return;
                        if (!text.matches("\\d*")) {
                                timeInput.setValue(text.replaceAll("[^\\d]", ""));
                                return;
                        }
                        if (!text.isEmpty()) {
                                int months = Mth.clamp(Integer.parseInt(text), 0, MAX_MONTHS);
                                currentSelectedMonths = months;
                                isSyncing = true;
                                timeSlider.setValue((float) months / MAX_MONTHS);
                                isSyncing = false;
                        }
                });

                this.addRenderableWidget(timeInput);
        }

        private void updateInputBox(int months) {
                timeInput.setValue(String.valueOf(months));
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                this.renderBackground(g, mouseX, mouseY, partialTick);

                panel.render(g, mouseX, mouseY, partialTick);

                int cx = px + panelW / 2;
                int innerW = panelW - CultivationTheme.PADDING * 2;

                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, "level_progress_gui_title");
                g.drawString(font, title, cx - font.width(title) / 2, py + 15, CultivationTheme.BORDER_GOLD, true);

                Component yearTimeComponent = XuanLing.trans(XuanlingLangPro.GUI_STRING, "cultivate_time_year", currentSelectedMonths / 12, currentSelectedMonths % 12);
                Component monthTimeComponent = XuanLing.trans(XuanlingLangPro.GUI_STRING, "cultivate_time_month", currentSelectedMonths);
                int timeTextY = py + (int) (panelH * 0.25f);
                if (currentSelectedMonths > 12) {
                        g.drawString(font, yearTimeComponent, cx - font.width(yearTimeComponent) / 2, timeTextY, CultivationTheme.TEXT_PRIMARY,
                                     false);
                } else {
                        g.drawString(font, monthTimeComponent, cx - font.width(monthTimeComponent) / 2, timeTextY, CultivationTheme.TEXT_PRIMARY,
                                     false);

                }
                timeSlider.render(g, mouseX, mouseY, partialTick);

                int inputW = 50;
                int inputH = 10;
                int inputX = cx - inputW / 2;
                int inputY = timeSlider.getY() + 32;

                Component hintText = XuanLing.trans(XuanlingLangPro.GUI_STRING, "input_time_month");
                g.drawString(font, hintText, cx - font.width(hintText) / 2, inputY - 14,
                                CultivationTheme.TEXT_SECONDARY, false);

                int boxPadding = 2;
                g.fill(inputX - boxPadding, inputY - boxPadding, inputX + inputW + boxPadding,
                                inputY + inputH + boxPadding, CultivationTheme.PANEL_BG_DARK);
                GuiRenderUtil.border(g, inputX - boxPadding, inputY - boxPadding, inputX + inputW + boxPadding,
                                inputY + inputH + boxPadding, CultivationTheme.BORDER_CYAN);

                String text = timeInput.getValue();
                int textW = font.width(text);
                int textRenderX = inputX + (inputW - textW) / 2;
                int textRenderY = inputY + (inputH - 8) / 2;

                g.drawString(font, text, textRenderX, textRenderY, CultivationTheme.TEXT_PRIMARY, false);

                if (timeInput.isFocused() && (System.currentTimeMillis() / 500) % 2 == 0) {
                        g.fill(textRenderX + textW + 1, textRenderY - 1, textRenderX + textW + 2, textRenderY + 9,
                                        CultivationTheme.TEXT_PRIMARY);
                }

                Component startTrain = XuanLing.trans(XuanlingLangPro.GUI_STRING, "start_train");
                Component refining = XuanLing.trans(XuanlingLangPro.GUI_STRING, "refining");
                GuiComponentGroup btnGroup = new GuiComponentGroup()
                                .setLayout(Layouts.row().setJustify(Justify.CENTER).setSpacing(10))
                                .setFixedWidth(innerW)
                                .add(new GuiButton(80, 20, startTrain.getString()).onClick(() -> {
                                                        // TODO
                                }))
                                .add(new GuiButton(80, 20, refining.getString()).onClick(() -> {
                                                        // TODO
                                }));

                btnGroup.setPosition(px + CultivationTheme.PADDING, py + panelH - 45);
                btnGroup.render(g, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                int inputW = 50;
                int cx = px + panelW / 2;
                int inputX = cx - inputW / 2;
                int inputY = timeSlider.getY() + 32;

                if (mx >= inputX - 2 && mx <= inputX + inputW + 2 && my >= inputY - 2 && my <= inputY + 10 + 2) {
                        timeInput.setFocused(true);
                        return true;
                }

                if (timeInput.isFocused()) {
                        timeInput.setFocused(false);
                        timeInput.setValue(timeInput.getValue());
                }

                if (timeSlider.mouseClicked(mx, my, button))
                        return true;
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (timeInput.isFocused()) {
                        if (keyCode == 257 || keyCode == 335) {
                                timeInput.setFocused(false);
                                timeInput.setValue(timeInput.getValue());
                                return true;
                        }
                        if (timeInput.keyPressed(keyCode, scanCode, modifiers))
                                return true;
                }
                return super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
                if (timeInput.isFocused() && timeInput.charTyped(codePoint, modifiers))
                        return true;
                return super.charTyped(codePoint, modifiers);
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (timeSlider.mouseDragged(mx, my, button, dx, dy))
                        return true;
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (timeSlider.mouseReleased(mx, my, button))
                        return true;
                if (timeInput.mouseReleased(mx, my, button))
                        return true;
                return super.mouseReleased(mx, my, button);
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }
}
