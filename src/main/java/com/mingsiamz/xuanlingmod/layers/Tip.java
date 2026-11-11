package com.mingsiamz.xuanlingmod.layers;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw.Layer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

public class Tip implements Layer {

        Component text;

        public Tip(String text) {
                this.text = XuanLing.trans(XuanlingLangPro.GUI_STRING, text);
        }

        @Override
        public void render(GuiGraphics g, DeltaTracker deltaTracker) {
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer player = minecraft.player;
                if (RegisterAttachments.getDefaultEnergy(player).getMaxPValue() <= 0 || RegisterAttachments.getDefaultEnergy(player).getMaxCValue() <= 0)
                        return;
                Font font = minecraft.font;
                int width = g.guiWidth() / 2 - (font.width(this.text.getString()) / 2);
                int height = g.guiHeight() - 35;
                g.drawString(Minecraft.getInstance().font, this.text, width, height, CultivationTheme.TEXT_PRIMARY);
        }

}
