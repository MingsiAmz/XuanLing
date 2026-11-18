package com.mingsiamz.xuanlingmod.gui;

import java.util.List;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.network.CastSpellPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class SpellSelectWheelGui extends Screen {

        private static final int RADIUS = 84;
        private static final int OPTION_W = 110;
        private static final int OPTION_H = 24;

        private final List<String> names;
        private final String combo;

        private int cx;
        private int cy;

        public SpellSelectWheelGui(List<String> names, String combo) {
                super(Component.literal("spell_select_wheel_gui"));
                this.names = names;
                this.combo = combo;
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }

        @Override
        protected void init() {
                this.cx = this.width / 2;
                this.cy = this.height / 2;
        }

        private int optionCount() {
                return Math.min(this.names.size(), 9);
        }

        private double angleOf(int i) {
                return Math.toRadians(-90 + i * 360.0 / optionCount());
        }

        private int optionX(int i) {
                return this.cx + (int) Math.round(Math.cos(angleOf(i)) * RADIUS) - OPTION_W / 2;
        }

        private int optionY(int i) {
                return this.cy + (int) Math.round(Math.sin(angleOf(i)) * RADIUS) - OPTION_H / 2;
        }

        private int hoveredOption(double mx, double my) {
                for (int i = 0; i < optionCount(); i++) {
                        int x = optionX(i);
                        int y = optionY(i);
                        if (mx >= x && mx <= x + OPTION_W && my >= y && my <= y + OPTION_H)
                                return i;
                }
                return -1;
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                this.renderBackground(g, mouseX, mouseY, partialTick);

                Component comboLabel = Component.literal(this.combo);
                g.fill(this.cx - 26, this.cy - 12, this.cx + 26, this.cy + 12, CultivationTheme.PANEL_BG_DARK);
                GuiRenderUtil.border(g, this.cx - 26, this.cy - 12, this.cx + 26, this.cy + 12,
                                CultivationTheme.BORDER_GOLD);
                g.drawString(font, comboLabel, this.cx - font.width(comboLabel) / 2, this.cy - 4,
                                CultivationTheme.TEXT_GOLD, false);

                int hovered = hoveredOption(mouseX, mouseY);
                for (int i = 0; i < optionCount(); i++) {
                        int x = optionX(i);
                        int y = optionY(i);
                        boolean hot = i == hovered;

                        int midX = x + OPTION_W / 2;
                        int midY = y + OPTION_H / 2;
                        g.fill(Math.min(this.cx, midX) - 1, Math.min(this.cy, midY),
                                        Math.max(this.cx, midX) + 1, Math.max(this.cy, midY) + 1,
                                        hot ? 0x5000E5FF : 0x2800E5FF);

                        g.fill(x, y, x + OPTION_W, y + OPTION_H,
                                        hot ? CultivationTheme.BUTTON_HOVER_BG : CultivationTheme.BUTTON_BG);
                        GuiRenderUtil.border(g, x, y, x + OPTION_W, y + OPTION_H,
                                        hot ? CultivationTheme.BORDER_GOLD : CultivationTheme.BORDER_CYAN);

                        Component label = Component.literal((i + 1) + ". " + this.names.get(i));
                        int labelX = x + (OPTION_W - font.width(label)) / 2;
                        g.drawString(font, label, labelX, y + (OPTION_H - 8) / 2,
                                        hot ? CultivationTheme.TEXT_GOLD : CultivationTheme.TEXT_PRIMARY, false);
                }

                Component hint = XuanLing.trans(XuanlingLangPro.GUI_STRING, "wheel_hint");
                g.drawString(font, hint, this.cx - font.width(hint) / 2, this.cy + RADIUS + OPTION_H,
                                CultivationTheme.TEXT_SECONDARY, false);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (button == 0) {
                        int i = hoveredOption(mx, my);
                        if (i >= 0) {
                                select(i);
                                return true;
                        }
                }
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (keyCode == 256) {
                        onClose();
                        return true;
                }
                int idx = keyCode - '1';
                if (idx >= 0 && idx < optionCount()) {
                        select(idx);
                        return true;
                }
                return super.keyPressed(keyCode, scanCode, modifiers);
        }

        private void select(int i) {
                Networking.sendToServer(new CastSpellPacket(this.names.get(i)));
                onClose();
        }

        @Override
        public void onClose() {
                minecraft.setScreen(null);
        }
}
