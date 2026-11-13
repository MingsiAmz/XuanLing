package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.gui.weight.GuiButton;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.util.gui.CultivationPanel;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiComponentGroup;
import com.mingsiamz.xuanlingmod.util.gui.SectionDivider;
import com.mingsiamz.xuanlingmod.util.gui.layout.Align;
import com.mingsiamz.xuanlingmod.util.gui.layout.Justify;
import com.mingsiamz.xuanlingmod.util.gui.layout.Layouts;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class CreateGui extends Screen {

        private int panelW;
        private int panelH;
        private int px;
        private int py;

        private CultivationPanel panel;
        private GuiComponentGroup root;

        public CreateGui() {
                super(Component.literal("create_gui"));
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }

        @Override
        protected void init() {
                this.panelW = Math.max(300, (int) (this.width * 0.32f));
                this.panelH = Math.max(220, (int) (this.height * 0.55f));

                this.px = (this.width - this.panelW) / 2;
                this.py = (this.height - this.panelH) / 2;

                panel = new CultivationPanel(this.panelW, this.panelH);
                panel.setPosition(this.px, this.py);

                int innerW = this.panelW - CultivationTheme.PADDING * 2;
                int btnW = (int) (innerW * 0.6f);
                int btnH = (int) Math.max(26, this.panelH * 0.12f);
                int spacing = (int) (this.panelH * 0.07f);

                Component spell = XuanLing.trans(XuanlingLangPro.GUI_STRING, "make_cultivation_spell");
                Component art = XuanLing.trans(XuanlingLangPro.GUI_STRING, "make_cultivation_art");
                Component realm = XuanLing.trans(XuanlingLangPro.GUI_STRING, "make_cultivation_realm");

                root = new GuiComponentGroup()
                                .setLayout(Layouts.column().setSpacing(spacing).setJustify(Justify.CENTER)
                                                .setAlign(Align.CENTER))
                                .setFixedWidth(innerW)
                                .setPadding(0)
                                .add(new GuiButton(btnW, btnH, spell.getString())
                                                .onClick(() -> minecraft.setScreen(
                                                                new ComponentEditorScreen(XuanLingTypeEnum.SPELL))))
                                .add(new GuiButton(btnW, btnH, art.getString())
                                                .onClick(() -> minecraft.setScreen(
                                                                new ComponentEditorScreen(XuanLingTypeEnum.SPELL))))
                                .add(new GuiButton(btnW, btnH, realm.getString())
                                                .onClick(() -> minecraft.setScreen(
                                                                new ComponentEditorScreen(XuanLingTypeEnum.SPELL))));

                int contentY = this.py + (int) (this.panelH * 0.28f);
                root.setPosition(this.px + CultivationTheme.PADDING, contentY);
                root.setFixedHeight(this.py + this.panelH - CultivationTheme.PADDING - contentY);
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                super.render(g, mouseX, mouseY, partialTick);
                this.renderBackground(g, mouseX, mouseY, partialTick);
                panel.render(g, mouseX, mouseY, partialTick);

                int cx = panel.getX() + this.panelW / 2;
                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, "create_cultivation");
                g.drawString(font, title, cx - font.width(title) / 2,
                                panel.getY() + (int) (this.panelH * 0.10f), CultivationTheme.BORDER_GOLD, false);

                SectionDivider divider = new SectionDivider(panel.getInnerWidth())
                                .setColors(0x40FFFFFF, CultivationTheme.BORDER_CYAN);
                divider.setPosition(panel.getInnerX(), panel.getY() + (int) (this.panelH * 0.18f));
                divider.render(g, mouseX, mouseY, partialTick);

                root.render(g, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (root.mouseClicked(mx, my, button))
                        return true;
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public void onClose() {
                minecraft.setScreen(ChangeGui.cycle('0'));
        }
}
