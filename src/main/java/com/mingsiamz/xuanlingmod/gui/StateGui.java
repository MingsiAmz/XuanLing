package com.mingsiamz.xuanlingmod.gui;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.BasicRealm;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.component.ComponentRealm;
import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.util.gui.CultivationPanel;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiComponentGroup;
import com.mingsiamz.xuanlingmod.util.gui.Label;
import com.mingsiamz.xuanlingmod.util.gui.Label.Align;
import com.mingsiamz.xuanlingmod.util.gui.SectionDivider;
import com.mingsiamz.xuanlingmod.util.gui.StateBar;
import com.mingsiamz.xuanlingmod.util.gui.layout.Justify;
import com.mingsiamz.xuanlingmod.util.gui.layout.Layouts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class StateGui extends Screen {

        private int panelW;
        private int panelH;
        private int px;
        private int py;

        private CultivationPanel panel;
        private GuiComponentGroup root;
        private StateBar progressBar;
        private static GuiButton leftButton;
        private static GuiButton rightButton;

        public StateGui() {
                super(Component.literal("state_gui"));
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

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (root.keyPressed(keyCode, scanCode, modifiers))
                        return true;
                return super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (root.mouseClicked(mx, my, button) || leftButton.mouseClicked(mx, my, button)
                                || rightButton.mouseClicked(mx, my, button))
                        return true;
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (root.mouseDragged(mx, my, button, dx, dy))
                        return true;
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (root.mouseReleased(mx, my, button))
                        return true;
                return super.mouseReleased(mx, my, button);
        }

        @Override
        public boolean mouseScrolled(double mx, double my, double sx, double sy) {
                if (root.mouseScrolled(mx, my, sx, sy))
                        return true;
                return super.mouseScrolled(mx, my, sx, sy);
        }

        @Override
        protected void init() {
                this.panelW = Math.max(340, Math.min(480, (int) (this.width * 0.45f)));
                this.panelH = Math.max(280, Math.min(400, (int) (this.height * 0.60f)));

                this.px = (this.width - this.panelW) / 2;
                this.py = (this.height - this.panelH) / 2;

                panel = new CultivationPanel(this.panelW, this.panelH);
                panel.setPosition(this.px, this.py);

                int innerW = this.panelW - CultivationTheme.PADDING * 2;
                
                GuiComponentGroup statGroup = new GuiComponentGroup()
                                .setLayout(Layouts.row().setJustify(Justify.CENTER).setSpacing(10))
                                .setFixedWidth(innerW);

                GuiComponentGroup statLeft = new GuiComponentGroup().setLayout(Layouts.column().setSpacing(6));
                GuiComponentGroup statRight = new GuiComponentGroup().setLayout(Layouts.column().setSpacing(6));

                String[] str = RegisterAttachments.getBasicData(minecraft.player).toString().split("\n");
                for (int i = 0; i < str.length / 2; i++) {
                        String[] tmp = str[i].split(":");
                        statLeft.add(new Label(tmp[0].toLowerCase(), tmp[1]).setWidth(100));
                }
                for (int i = str.length / 2; i < str.length; i++) {
                        String[] tmp = str[i].split(":");
                        statRight.add(new Label(tmp[0].toLowerCase(), tmp[1]).setWidth(100).setAlign(Align.RIGHT));
                }
                statGroup.add(statLeft);
                statGroup.add(statRight);

                Component make = XuanLing.trans(XuanlingLangPro.GUI_STRING, "make_cultivation_component");
                Component cultivateTitle = XuanLing.trans(XuanlingLangPro.GUI_STRING, "cultivate_title_btn");
                GuiComponentGroup buttonGroup = new GuiComponentGroup()
                                .setLayout(Layouts.flow(innerW).setSpacing(8).setJustify(Justify.CENTER))
                                .setFixedWidth(innerW)
                                .add(new GuiButton(80, 20, make.getString()).onClick(() -> minecraft.setScreen(new ComponentEditorScreen())))
                                .add(new GuiButton(80, 20, cultivateTitle.getString()).onClick(() -> minecraft.setScreen(new LevelProgressGui())))
                                .add(new GuiButton(80, 20, "功法").onClick(() -> {
                                                        // TODO
                                }));

                root = new GuiComponentGroup()
                                .setLayout(Layouts.column().setSpacing(14))
                                .setPadding(0)
                                .add(new SectionDivider(innerW).setColors(0x40FFFFFF, CultivationTheme.BORDER_GOLD))
                                .add(statGroup)
                                .add(new SectionDivider(innerW).setColors(0x40FFFFFF, CultivationTheme.BORDER_CYAN).setDiamondSize(3))
                                .add(buttonGroup);
                
                int rootY = this.py + (int) (this.panelH * 0.375f);
                root.setPosition(this.px + CultivationTheme.PADDING, rootY);

                int progressY = this.py + (int) (this.panelH * 0.31f);
                progressBar = new StateBar(innerW)
                                .setColor(CultivationTheme.BORDER_CYAN)
                                .setBarHeight(8)
                                .showText(false)
                                .showTicks(false)
                                .setValue(0, 100);
                progressBar.setPosition(this.px + CultivationTheme.PADDING, progressY);

                initSideBtn(this.panelW, this.panelH, this.px, this.py);
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                this.renderBackground(g, mouseX, mouseY, partialTick);
                panel.render(g, mouseX, mouseY, partialTick);

                int cx = this.px + this.panelW / 2;
                int innerX = this.px + CultivationTheme.PADDING;
                int rightX = this.px + this.panelW - CultivationTheme.PADDING;

                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, "state_title");
                g.drawString(font, title, cx - font.width(title) / 2,
                                this.py + (int) (this.panelH * 0.05f), CultivationTheme.BORDER_GOLD, false);

                int idY = this.py + (int) (this.panelH * 0.20f);
                if (minecraft.player.hasData(RegisterAttachments.BASIC_DATA)) {
                        BasicData basicData = RegisterAttachments.getBasicData(minecraft.player);
                        g.drawString(font, XuanLing.trans(XuanlingLangPro.GUI_STRING, "nick_name", basicData.getNickName()), 
                                     innerX, idY, CultivationTheme.TEXT_PRIMARY, false);
                        g.drawString(font, XuanLing.trans(XuanlingLangPro.GUI_STRING, "sect", basicData.getSect()), 
                                     innerX, idY + 16, CultivationTheme.TEXT_PRIMARY, false);
                }

                renderRealm(g, rightX, idY);
                renderPlayer(g, mouseX, mouseY);

                root.render(g, mouseX, mouseY, partialTick);
                progressBar.render(g, mouseX, mouseY, partialTick);
                renderSideBtn(g, mouseX, mouseY, partialTick);
        }

        public void renderRealm(GuiGraphics g, int rightX, int idY) {
                DefaultData data = RegisterAttachments.getDefaultData(minecraft.player);
                Component realmComp = XuanLing.trans(XuanlingLangPro.GUI_STRING, "realm");
                if (data != null) {
                        ComponentRealm comRealm = BasicRealm.getRealm(minecraft.player.getUUID(), data.getRealmType(), data.getRealmLevel());
                        if (comRealm != null) {
                                g.drawString(font, realmComp, rightX - font.width(realmComp) - 20, idY, CultivationTheme.TEXT_GOLD, true);
                                if (!comRealm.canTrans()) {
                                        g.drawString(font, Component.literal(comRealm.getName()), rightX - font.width(realmComp), idY, CultivationTheme.TEXT_GOLD, true);
                                } else {
                                        g.drawString(font, Component.translatable(comRealm.getName()), rightX - font.width(realmComp), idY, CultivationTheme.TEXT_GOLD);
                                }
                        }
                }
        }

        public void renderPlayer(GuiGraphics g, int mouseX, int mouseY) {
                int figureX = this.px + this.panelW / 2;
                int figureY = this.py + (int) (this.panelH * 0.714f);
                int figureScale = 35;

                int haloRx = 34;
                int haloRy = 58;
                int haloCenterX = figureX;
                int haloCenterY = figureY - 35;

                drawEllipse(g, haloCenterX, haloCenterY, haloRx + 6, haloRy + 6, 0x0A00FFFF);
                drawEllipse(g, haloCenterX, haloCenterY, haloRx + 3, haloRy + 3, 0x0A00FFFF);
                drawEllipse(g, haloCenterX, haloCenterY, haloRx, haloRy, 0x0A00FFFF);
                drawEllipse(g, haloCenterX, haloCenterY, haloRx - 4, haloRy - 4, 0x0A00FFFF);

                if (minecraft.player != null) {
                        float mouseDX = (float) mouseX - figureX;
                        float mouseDY = (float) mouseY - figureY;

                        float yRot = Mth.clamp(-mouseDX * 0.6f, -35f, 35f);
                        float xRot = Mth.clamp(-mouseDY * 0.3f, -15f, 15f);

                        float oldYRot = minecraft.player.getYRot();
                        float oldXRot = minecraft.player.getXRot();
                        float oldYBodyRot = minecraft.player.yBodyRot;
                        float oldYHeadRot = minecraft.player.yHeadRot;
                        float oldYHeadRotO = minecraft.player.yHeadRotO;

                        minecraft.player.yBodyRot = 180.0F + yRot;
                        minecraft.player.setYRot(180.0F + yRot);
                        minecraft.player.setXRot(0.0F);
                        minecraft.player.yHeadRot = minecraft.player.getYRot();
                        minecraft.player.yHeadRotO = minecraft.player.getYRot();

                        Quaternionf rotation = new Quaternionf().rotateZ((float) Math.PI);
                        Quaternionf xRotation = new Quaternionf().rotateX((float) Math.toRadians(xRot));
                        rotation.mul(xRotation);

                        InventoryScreen.renderEntityInInventory(
                                        g, figureX, figureY, figureScale,
                                        new Vector3f(0, 0, 0),
                                        rotation,
                                        null,
                                        minecraft.player);

                        minecraft.player.setYRot(oldYRot);
                        minecraft.player.setXRot(oldXRot);
                        minecraft.player.yBodyRot = oldYBodyRot;
                        minecraft.player.yHeadRot = oldYHeadRot;
                        minecraft.player.yHeadRotO = oldYHeadRotO;
                }
        }

        private void drawEllipse(GuiGraphics g, int cx, int cy, int rx, int ry, int color) {
                for (int y = -ry; y <= ry; y++) {
                        int x = (int) (rx * Math.sqrt(1 - (double) (y * y) / (ry * ry)));
                        g.fill(cx - x, cy + y, cx + x, cy + y + 1, color);
                }
        }

        public static void renderSideBtn(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                leftButton.render(g, mouseX, mouseY, partialTick);
                rightButton.render(g, mouseX, mouseY, partialTick);
        }

        public static GuiButton[] initSideBtn(int panelW, int panelH, int px, int py) {
                int sideW = 22;
                int sideH = 60;
                int sideGap = 8;

                leftButton = new GuiButton(sideW, sideH, "◀").onClick(() -> {
                        Minecraft.getInstance().setScreen(ChangeGui.cycle('p'));
                });
                rightButton = new GuiButton(sideW, sideH, "▶").onClick(() -> {
                        Minecraft.getInstance().setScreen(ChangeGui.cycle('n'));
                });

                int btnY = py + (panelH - sideH) / 2;
                leftButton.setPosition(px - sideGap - sideW, btnY);
                rightButton.setPosition(px + panelW + sideGap, btnY);
                return new GuiButton[] { leftButton, rightButton };
        }
}
