package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.gui.GuiGraphics;

public interface IGuiComponent {

        void render(GuiGraphics g, int mouseX, int mouseY, float partialTick);

        void setPosition(int x, int y);

        int getX();

        int getY();

        int getWidth();

        int getHeight();

        default boolean isMouseOver(double mouseX, double mouseY) {
                return mouseX >= getX() && mouseX < getX() + getWidth()
                        && mouseY >= getY() && mouseY < getY() + getHeight();
        }

        default boolean mouseClicked(double mouseX, double mouseY, int button) {
                return false;
        }

        default boolean mouseReleased(double mouseX, double mouseY, int button) {
                return false;
        }

        default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
                return false;
        }

        default boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
                return false;
        }

        default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                return false;
        }

        default boolean charTyped(char codePoint, int modifiers) {
                return false;
        }
}
