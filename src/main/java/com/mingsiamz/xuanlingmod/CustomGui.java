package com.mingsiamz.xuanlingmod;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CustomGui extends Screen{
    private static final ResourceLocation BACKGROUND_TEXTURE = XuanLing.prefix("textures/gui/data_gui.png");
    private static final int GUI_WIDTH = 300;
    private static final int GUI_HEIGHT = 200;
    private static final int TARGET_WIDTH = 512;
    private static final int TARGET_HEIGHT = 326;
    private static final String TITLE = "Test GUI";

    private int leftPos = 0;
    private int topPos = 0;

    protected CustomGui() {
	super(Component.literal(TITLE));
    }

    @Override
    protected void init() {
	super.init();
	this.leftPos = (this.width - GUI_WIDTH) / 2;
	this.topPos = (this.height - GUI_HEIGHT) / 2;

	this.addRenderableWidget(Button.builder(
						Component.literal("test_button"), b -> {
						    
						})
				 .bounds(leftPos + 30, topPos + 40, 100, 20)
				 .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
	super.render(guiGraphics, mouseX, mouseY, partialTick);
	guiGraphics.drawString(this.font, this.title, leftPos + 8, topPos + 6, 0xFFFFFF, false);
	// TODO Fix screen size
	guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, GUI_WIDTH, GUI_HEIGHT, TARGET_WIDTH, TARGET_HEIGHT);
    }

    @Override
    public boolean isPauseScreen() {
	return false;
    }
}
