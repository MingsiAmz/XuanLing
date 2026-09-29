package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.util.gui.*;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StateGui extends Screen {

        private static final int PANEL_W = 340;
        private static final int PANEL_H = 280;

        private CultivationPanel panel;
        private GuiComponentGroup root;

        private final String daoName = "太虚散人";
        private final String sect = "太虚剑宗";
        private final String realm = "筑基后期";

        public StateGui() {
                super(Component.literal("道枢"));
        }

        @Override
        protected void init() {
                int px = (this.width - PANEL_W) / 2;
                int py = (this.height - PANEL_H) / 2;

                panel = new CultivationPanel(PANEL_W, PANEL_H);
                panel.setPosition(px, py);

                int innerW = PANEL_W - CultivationTheme.PADDING * 2;
                GuiComponentGroup statGroup = new GuiComponentGroup()
                        .setLayout(GuiComponentGroup.Layout.VERTICAL)
                        .setSpacing(10)
                        .setPadding(0)
                        .add(new Label(RegisterAttachments.getBasicData(minecraft.player).toString()));

                

                GuiComponentGroup buttonGroup = new GuiComponentGroup()
                        .setLayout(GuiComponentGroup.Layout.HORIZONTAL)
                        .setSpacing(8)
                        .add(new GuiButton(80, 20, "突破")
                             .onClick(() -> System.out.println("尝试突破")))
                        .add(new GuiButton(80, 20, "炼丹")
                             .onClick(() -> System.out.println("打开炼丹炉")))
                        .add(new GuiButton(80, 20, "功法")
                             .onClick(() -> System.out.println("查看功法")));

                root = new GuiComponentGroup()
                        .setLayout(GuiComponentGroup.Layout.VERTICAL)
                        .setSpacing(14)
                        .setPadding(0)
                        .add(new SectionDivider(innerW)
                             .setColors(0x40FFFFFF, CultivationTheme.BORDER_GOLD))
                        .add(statGroup)
                        .add(new SectionDivider(innerW)
                             .setColors(0x40FFFFFF, CultivationTheme.BORDER_CYAN)
                             .setDiamondSize(3))
                        .add(buttonGroup);

                root.setPosition(px + CultivationTheme.PADDING, py + 105);
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                this.renderBackground(g, mouseX, mouseY, partialTick);
                panel.render(g, mouseX, mouseY, partialTick);

                int cx = panel.getX() + PANEL_W / 2;
                int innerX = panel.getX() + CultivationTheme.PADDING;
                int rightX = panel.getX() + PANEL_W - CultivationTheme.PADDING;

                // 标题
                Component title = Component.literal("道 枢");
                g.drawString(font, title, cx - font.width(title) / 2,
                             panel.getY() + 14, CultivationTheme.BORDER_GOLD, false);
                Component subtitle = Component.literal("· 修 行 者 状 态 面 板 ·");
                g.drawString(font, subtitle, cx - font.width(subtitle) / 2,
                             panel.getY() + 28, CultivationTheme.TEXT_SECONDARY, false);

                int idY = panel.getY() + 56;
                g.drawString(font, Component.literal("道号"), innerX, idY, CultivationTheme.TEXT_SECONDARY, false);
                g.drawString(font, Component.literal(daoName), innerX + 34, idY, CultivationTheme.TEXT_PRIMARY, false);

                g.drawString(font, Component.literal("宗门"), innerX, idY + 16, CultivationTheme.TEXT_SECONDARY, false);
                g.drawString(font, Component.literal(sect), innerX + 34, idY + 16, CultivationTheme.TEXT_PRIMARY,
                             false);

                Component realmComp = Component.literal(realm);
                g.drawString(font, realmComp, rightX - font.width(realmComp), idY, CultivationTheme.TEXT_GOLD, true);
                Component realmLabel = Component.literal("境界");
                g.drawString(font, realmLabel,
                             rightX - font.width(realmComp) - font.width(realmLabel) - 8, idY,
                             CultivationTheme.TEXT_SECONDARY, false);

                root.render(g, mouseX, mouseY, partialTick);

                Component hint = Component.literal("[ ESC ] 关闭");
                g.drawString(font, hint, cx - font.width(hint) / 2,
                             panel.getY() + PANEL_H - 20, CultivationTheme.TEXT_SECONDARY, false);

        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (root.mouseClicked(mx, my, button))
                        return true;
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (root.mouseReleased(mx, my, button))
                        return true;
                return super.mouseReleased(mx, my, button);
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (root.mouseDragged(mx, my, button, dx, dy))
                        return true;
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (root.mouseScrolled(mx, my, sx, sy))
                        return true;
                return super.mouseScrolled(mx, my, sx, sy);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (root.keyPressed(keyCode, scanCode, modifiers))
                        return true;
                return super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean charTyped(char codePoint, int modifiers) {
                if (root.charTyped(codePoint, modifiers))
                        return true;
                return super.charTyped(codePoint, modifiers);
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }
}
