package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.util.gui.CultivationPanel;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiComponentGroup;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DetailDataGui extends Screen {

        private static final int PANEL_W = 340;
        private static final int PANEL_H = 280;

        private CultivationPanel panel;
        private GuiComponentGroup root;
        private GuiButton leftButton;
        private GuiButton rightButton;

        public DetailDataGui() {
                super(Component.literal("detail_data_gui"));
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }

        @Override
        protected void init() {
                int px = (this.width - PANEL_W) / 2;
                int py = (this.height - PANEL_H) / 2;

                panel = new CultivationPanel(PANEL_W, PANEL_H);
                panel.setPosition(px, py);

                int inW = PANEL_W - CultivationTheme.PADDING * 2;

                GuiButton[] bts = StateGui.initSideBtn(PANEL_W, PANEL_H, px, py);
                leftButton = bts[0];
                rightButton = bts[1];
                super.init();
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                this.renderBackground(g, mouseX, mouseY, partialTick);
                panel.render(g, mouseX, mouseY, partialTick);

                int cx = panel.getX() + PANEL_W / 2;
                int inX = panel.getX() + CultivationTheme.PADDING;
                int rightX = panel.getX() + PANEL_W - CultivationTheme.PADDING;
                g.drawString(font, "test", cx - font.width("test"), panel.getY() + 14, CultivationTheme.BORDER_GOLD, false);

                // root.render(g, mouseX, mouseY, partialTick);
                StateGui.renderSideBtn(g, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (leftButton.mouseClicked(mouseX, mouseY, button) || rightButton.mouseClicked(mouseX, mouseY, button))
                        return true;
                return super.mouseClicked(mouseX, mouseY, button);
        }

}
