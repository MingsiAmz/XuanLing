package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.BasicRealm;
import com.mingsiamz.xuanlingmod.data.DefaultData;
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

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DetailDataGui extends Screen {

        private static final int PANEL_W = 340;
        private static final int PANEL_H = 280;

        private static final double BASE_AGE = 10.0;
        private static final double BASE_NORMAL_LIFE = 100.0;
        private static final double BASE_SOUL_LIFE = 100.0;

        private CultivationPanel panel;
        private GuiComponentGroup root;
        private GuiButton leftButton;
        private GuiButton rightButton;
        private StateBar progressBar;
        private Label realmLabel;
        private Component realmText;

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

                int innerW = PANEL_W - CultivationTheme.PADDING * 2;

                BasicData basicData = (minecraft != null && minecraft.player != null)
                                ? RegisterAttachments.getBasicData(minecraft.player)
                                : null;
                DefaultData defaultData = (minecraft != null && minecraft.player != null)
                                ? RegisterAttachments.getDefaultData(minecraft.player)
                                : null;

                if (basicData == null) basicData = new BasicData();
                if (defaultData == null) defaultData = DefaultData.init();

                double bodyAge = BASE_AGE + (BASE_NORMAL_LIFE - basicData.getNormalLife());
                double mentalAge = BASE_AGE + (BASE_SOUL_LIFE - basicData.getSoulLife());
                Component bodyAgeText = formatAge(bodyAge);
                Component mentalAgeText = formatAge(mentalAge);

                GuiComponentGroup statsGroup = new GuiComponentGroup()
                                .setLayout(Layouts.row().setJustify(Justify.CENTER).setSpacing(12))
                                .setFixedWidth(innerW);

                GuiComponentGroup leftCol = new GuiComponentGroup().setLayout(Layouts.column().setSpacing(5));
                GuiComponentGroup rightCol = new GuiComponentGroup().setLayout(Layouts.column().setSpacing(5));

                leftCol.add(new Label("normallife", (int) basicData.getNormalLife()).setWidth(140));
                rightCol.add(new Label("soullife", (int) basicData.getSoulLife()).setWidth(140));

                leftCol.add(new Label("body_age", bodyAgeText.getString()).setWidth(140));
                rightCol.add(new Label("mental_age", mentalAgeText.getString()).setWidth(140));

                leftCol.add(new Label("movespeedmof", String.format("%.2f", basicData.getMoveSpeedMof())).setWidth(140));
                rightCol.add(new Label("flyspeedmof", String.format("%.2f", basicData.getFlySpeedMof())).setWidth(140));

                leftCol.add(new Label("physical_defense", String.format("%.1f", defaultData.getPhysicalDefense())).setWidth(140));
                rightCol.add(new Label("magic_defense", String.format("%.1f", defaultData.getMagicDefense())).setWidth(140));

                leftCol.add(new Label("lucky", (int) basicData.getLucky()).setWidth(140));
                rightCol.add(new Label("charm", (int) basicData.getCharm()).setWidth(140));

                leftCol.add(new Label("tpao", (int) basicData.getTPAO()).setWidth(140));
                rightCol.add(new Label("mindsense", (int) basicData.getMindSense()).setWidth(140));

                statsGroup.add(leftCol);
                statsGroup.add(rightCol);

                Component realm = XuanLing.trans(XuanlingLangPro.GUI_STRING, "realm");
                String curRealm = BasicRealm.getRealm(minecraft.player.getUUID(), defaultData.getRealmType(),
                                defaultData.getRealmLevel()).getName();
                
                realmText = Component.literal(realm.getString() + Component.translatable(curRealm).getString());
                
                realmLabel = new Label(realmText)
                                .setWidth(innerW)
                                .setAlign(Align.CENTER)
                                .setColor(CultivationTheme.TEXT_GOLD);

                root = new GuiComponentGroup()
                                .setLayout(Layouts.column().setSpacing(8))
                                .setPadding(0)
                                .add(new SectionDivider(innerW).setColors(0x40FFFFFF, CultivationTheme.BORDER_GOLD))
                                .add(statsGroup)
                                .add(new SectionDivider(innerW).setColors(0x40FFFFFF, CultivationTheme.BORDER_CYAN)
                                                .setDiamondSize(3));

                int rootY = py + (int) (PANEL_H * 0.20f);
                root.setPosition(px + CultivationTheme.PADDING, rootY);

                int progressY = (int) (py + PANEL_H / 1.275);
                progressBar = new StateBar(innerW)
                                .setColor(CultivationTheme.BORDER_CYAN)
                                .setBarHeight(8)
                                .showText(false)
                                .showTicks(false)
                                .setValue(0, 100);
                progressBar.setPosition(px + CultivationTheme.PADDING, progressY);

                GuiButton[] bts = StateGui.initSideBtn(PANEL_W, PANEL_H, px, py);
                leftButton = bts[0];
                rightButton = bts[1];
                super.init();
        }

        private Component formatAge(double age) {
                if (age < 0) age = 0;
                int years = (int) Math.floor(age);
                int months = (int) Math.round((age - years) * 12.0);
                if (months >= 12) {
                        years += 1;
                        months -= 12;
                }
                if (months < 0) months = 0;
                return XuanLing.trans(XuanlingLangPro.GUI_STRING, "player_time_year", years);
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                this.renderBackground(g, mouseX, mouseY, partialTick);
                panel.render(g, mouseX, mouseY, partialTick);

                int cx = panel.getX() + PANEL_W / 2;
                int inX = panel.getX() + CultivationTheme.PADDING;
                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, "detail_data_gui_title");
                g.drawString(font, title, cx - font.width(title) / 2, panel.getY() + 14, CultivationTheme.BORDER_GOLD, false);

                if (minecraft != null && minecraft.player != null && minecraft.player.hasData(RegisterAttachments.BASIC_DATA)) {
                        BasicData basicData = RegisterAttachments.getBasicData(minecraft.player);
                        int idY = panel.getY() + 34;
                        Component nickName = XuanLing.trans(XuanlingLangPro.GUI_STRING, "nick_name", basicData.getNickName());
                        Component sect = XuanLing.trans(XuanlingLangPro.GUI_STRING, "sect", basicData.getSect());
                        g.drawString(font, nickName, inX + font.width(nickName), idY, CultivationTheme.TEXT_GOLD, false);
                        g.drawString(font, sect, inX + 150 + font.width(sect), idY, CultivationTheme.TEXT_GOLD, false);
                }

                root.render(g, mouseX, mouseY, partialTick);
                StateGui.renderSideBtn(g, mouseX, mouseY, partialTick);

                int realmY = progressBar.getY() - 26; 
                realmLabel.setPosition(inX, realmY);
                realmLabel.render(g, mouseX, mouseY, partialTick);

                progressBar.render(g, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (leftButton.mouseClicked(mouseX, mouseY, button) || rightButton.mouseClicked(mouseX, mouseY, button))
                        return true;
                return super.mouseClicked(mouseX, mouseY, button);
        }
}
