package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.gui.GuiGraphics;

public class SectionDivider implements IGuiComponent {
    private int x, y;
    private int width;
    private int lineColor = CultivationTheme.BORDER_DIM;
    private int diamondColor = CultivationTheme.BORDER_CYAN;
    private int diamondSize = 4;
    private int lineThickness = 1;

    public SectionDivider(int width) {
        this.width = width;
    }

    public SectionDivider setColors(int lineColor, int diamondColor) {
        this.lineColor = lineColor; this.diamondColor = diamondColor; return this;
    }
    public SectionDivider setDiamondSize(int size) { this.diamondSize = size; return this; }
    public SectionDivider setLineThickness(int thickness) { this.lineThickness = thickness; return this; }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        int cx = x + width / 2;
        int half = width / 2;
        int gap = diamondSize * 2 + 2;

        g.fill(cx - half, y, cx - gap, y + lineThickness, lineColor);
        g.fill(cx + gap, y, cx + half, y + lineThickness, lineColor);
        
        if (diamondSize > 0) {
            GuiRenderUtil.diamond(g, cx, y, diamondSize, diamondColor);
        }
    }

    @Override public void setPosition(int x, int y) { this.x = x; this.y = y; }
    @Override public int getX() { return x; }
    @Override public int getY() { return y; }
    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return Math.max(lineThickness, diamondSize * 2); }
}
