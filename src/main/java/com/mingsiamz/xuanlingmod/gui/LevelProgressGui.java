package com.mingsiamz.xuanlingmod.gui;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.gui.ChangeGui;
import com.mingsiamz.xuanlingmod.network.ComprehensionPacket;
import com.mingsiamz.xuanlingmod.network.Networking;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.util.gui.CultivationPanel;
import com.mingsiamz.xuanlingmod.util.gui.CultivationSlider;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.component.GuiButton;
import com.mingsiamz.xuanlingmod.util.gui.component.ProgressBar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class LevelProgressGui extends Screen {
        private int panelW;
        private int panelH;
        private int px;
        private int py;

        private CultivationPanel panel;
        private CultivationSlider intensitySlider;
        private ProgressBar progressBar;
        private GuiButton actionButton;

        private boolean wasComprehending = false;

        public LevelProgressGui() {
                super(Component.literal("level_progress_gui"));
        }

        @Override
        protected void init() {
                super.init();

                this.panelW = Math.max(340, Math.min(520, (int) (this.width * 0.40f)));
                this.panelH = Math.max(260, Math.min(360, (int) (this.height * 0.50f)));
                this.px = (this.width - panelW) / 2;
                this.py = (this.height - panelH) / 2;

                panel = new CultivationPanel(panelW, panelH);
                panel.setPosition(px, py);

                int innerW = panelW - CultivationTheme.PADDING * 2;
                int cx = px + panelW / 2;

                // 参悟强度滑动条
                int sliderY = py + (int) (panelH * 0.34f);
                intensitySlider = new CultivationSlider(innerW, 10);
                intensitySlider.setPosition(px + CultivationTheme.PADDING, sliderY);
                intensitySlider.setValue(0.5f);

                // 读条进度条
                int barY = sliderY + 44;
                progressBar = new ProgressBar(innerW, 14).setLabel("").showText(true);
                progressBar.setPosition(px + CultivationTheme.PADDING, barY);

                // 开始/停止参悟按钮
                Component btnComp = XuanLing.trans(XuanlingLangPro.GUI_STRING, "comprehend_start");
                actionButton = new GuiButton(120, 24, btnComp.getString())
                                .onClick(this::onActionClick);
                actionButton.setPosition(cx - 60, barY + 42);
        }

        private void onActionClick() {
                Player player = this.minecraft.player;
                if (player == null)
                        return;
                BasicData data = RegisterAttachments.getBasicData(player);
                if (data == null)
                        return;
                if (data.isComprehending()) {
                        Networking.sendToServer(new ComprehensionPacket(false, "", 0f));
                } else {
                        Networking.sendToServer(new ComprehensionPacket(true, "comprehension", intensitySlider.getValue()));
                }
        }

        @Override
        public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
                this.renderBackground(g, mouseX, mouseY, partialTick);
                panel.render(g, mouseX, mouseY, partialTick);

                int cx = px + panelW / 2;
                int innerW = panelW - CultivationTheme.PADDING * 2;

                Component title = XuanLing.trans(XuanlingLangPro.GUI_STRING, "level_progress_gui_title");
                g.drawString(font, title, cx - font.width(title) / 2, py + 16, CultivationTheme.BORDER_GOLD, false);

                Player player = this.minecraft.player;
                BasicData data = player == null ? null : RegisterAttachments.getBasicData(player);

                if (data != null) {
                        Component lifeComp = XuanLing.trans(XuanlingLangPro.GUI_STRING, "lifespan",
                                        data.getLifespanMonth());
                        g.drawString(font, lifeComp, px + CultivationTheme.PADDING, py + 42,
                                        CultivationTheme.TEXT_PRIMARY, false);
                }

                // 参悟强度滑动条
                int sliderY = py + (int) (panelH * 0.28f);
                Component intensityLabel = XuanLing.trans(XuanlingLangPro.GUI_STRING, "comprehend_intensity");
                int pct = (int) (intensitySlider.getValue() * 100);
                Component pctComp = Component.literal(pct + "%");
                g.drawString(font, intensityLabel, px + CultivationTheme.PADDING, sliderY - 14,
                                CultivationTheme.TEXT_SECONDARY, false);
                g.drawString(font, pctComp, px + panelW - CultivationTheme.PADDING - font.width(pctComp), sliderY - 14,
                                CultivationTheme.TEXT_PRIMARY, false);

                boolean comprehending = data != null && data.isComprehending();

                // 读条结束后滑动条复位
                if (wasComprehending && !comprehending) {
                        intensitySlider.setValue(0f);
                }
                wasComprehending = comprehending;

                int barY = sliderY + 44;
                if (comprehending) {
                        int tick = data.getComprehensionTick();
                        int maxTick = data.getComprehensionMaxTick();
                        float progress = maxTick > 0 ? 1f - (float) tick / maxTick : 0f;
                        progressBar.setInstant(progress);
                        progressBar.setLabel(XuanLing.trans(XuanlingLangPro.GUI_STRING, "comprehend_progress")
                                        .getString());
                        actionButton.setText(XuanLing.trans(XuanlingLangPro.GUI_STRING, "comprehend_stop").getString());
                        Component meditating = XuanLing.trans(XuanlingLangPro.GUI_STRING, "comprehend_meditating");
                        g.drawString(font, meditating, cx - font.width(meditating) / 2, barY - 16,
                                        CultivationTheme.TEXT_INFO, false);
                } else {
                        progressBar.setInstant(0f);
                        progressBar.setLabel(XuanLing.trans(XuanlingLangPro.GUI_STRING, "comprehend_progress")
                                        .getString());
                        actionButton.setText(XuanLing.trans(XuanlingLangPro.GUI_STRING, "comprehend_start").getString());
                }

                intensitySlider.render(g, mouseX, mouseY, partialTick);

                progressBar.setPosition(px + CultivationTheme.PADDING, barY);
                progressBar.render(g, mouseX, mouseY, partialTick);

                actionButton.render(g, mouseX, mouseY, partialTick);
        }

        @Override
        public boolean mouseClicked(double mx, double my, int button) {
                if (intensitySlider.mouseClicked(mx, my, button))
                        return true;
                if (actionButton.mouseClicked(mx, my, button))
                        return true;
                return super.mouseClicked(mx, my, button);
        }

        @Override
        public boolean mouseDragged(double mx, double my, int button, double dx, double dy) {
                if (intensitySlider.mouseDragged(mx, my, button, dx, dy))
                        return true;
                return super.mouseDragged(mx, my, button, dx, dy);
        }

        @Override
        public boolean mouseReleased(double mx, double my, int button) {
                if (intensitySlider.mouseReleased(mx, my, button))
                        return true;
                return super.mouseReleased(mx, my, button);
        }

        @Override
        public boolean isPauseScreen() {
                return false;
        }

        @Override
        public void onClose() {
                this.minecraft.setScreen(ChangeGui.cycle('0'));
        }
}
