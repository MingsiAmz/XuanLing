package com.mingsiamz.xuanlingmod.layers;

import com.mingsiamz.xuanlingmod.util.gui.CultivationTheme;
import com.mingsiamz.xuanlingmod.util.gui.GuiRenderUtil;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw.Layer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class Tip implements Layer {

        private final Supplier<Component> textSupplier;
        private Predicate<LocalPlayer> condition = player -> true;

        private int themeColor = CultivationTheme.BORDER_CYAN;
        private float pulseSpeed = 0.003f;
        private float minAlpha = 0.5f;
        private float maxAlpha = 1.0f;
        private int yOffset = -45;

        private long lifeTimeMs = -1;
        private boolean fadeOut = true;
        private long fadeOutDurationMs = 500;

        private long triggerTime = 0;
        private boolean lastCondition = false;

        public Tip(Supplier<Component> textSupplier) {
                this.textSupplier = textSupplier;
        }

        public Tip(Component text) {
                this(() -> text);
        }

        public Tip setCondition(Predicate<LocalPlayer> condition) {
                this.condition = condition;
                return this;
        }

        public Tip setThemeColor(int themeColor) {
                this.themeColor = themeColor;
                return this;
        }

        public Tip setAnimation(float speed, float minAlpha, float maxAlpha) {
                this.pulseSpeed = speed;
                this.minAlpha = minAlpha;
                this.maxAlpha = maxAlpha;
                return this;
        }

        public Tip setYOffset(int yOffset) {
                this.yOffset = yOffset;
                return this;
        }

        public Tip setLifeTime(long timeMs, boolean fadeOut) {
                this.lifeTimeMs = timeMs;
                this.fadeOut = fadeOut;
                return this;
        }

        public Tip setFadeOutDuration(long durationMs) {
                this.fadeOutDurationMs = durationMs;
                return this;
        }

        @Override
        public void render(GuiGraphics g, DeltaTracker deltaTracker) {
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer player = minecraft.player;
                if (player == null)
                        return;

                boolean conditionMet = condition.test(player);

                if (conditionMet && !lastCondition) {
                        triggerTime = System.currentTimeMillis();
                }
                lastCondition = conditionMet;
                if (triggerTime == 0)
                        return;

                float lifeAlphaMultiplier = 1.0f;
                long elapsed = System.currentTimeMillis() - triggerTime;

                if (lifeTimeMs > 0) {
                        if (elapsed > lifeTimeMs) {
                                if (!fadeOut) {
                                        triggerTime = 0;
                                        return;
                                }

                                long fadeElapsed = elapsed - lifeTimeMs;
                                if (fadeElapsed >= fadeOutDurationMs) {
                                        triggerTime = 0;
                                        return;
                                }

                                lifeAlphaMultiplier = 1.0f - ((float) fadeElapsed / fadeOutDurationMs);
                        }
                } else {
                        if (!conditionMet) {
                                triggerTime = 0;
                                return;
                        }
                }

                Component text = textSupplier.get();
                if (text == null || text.getString().isEmpty())
                        return;

                Font font = minecraft.font;
                String textStr = text.getString();
                int textW = font.width(textStr);

                int cx = g.guiWidth() / 2;
                int cy = g.guiHeight() + yOffset;

                double time = System.currentTimeMillis() * pulseSpeed;
                float normalizedPulse = (float) (Math.sin(time) * 0.5 + 0.5);
                float currentAlpha = (minAlpha + (maxAlpha - minAlpha) * normalizedPulse) * lifeAlphaMultiplier;

                if (currentAlpha <= 0.01f)
                        return;

                int alphaInt = (int) (currentAlpha * 255) << 24;
                int textColor = (0xFFFFFFFF & 0x00FFFFFF) | alphaInt;
                int borderColor = (themeColor & 0x00FFFFFF) | alphaInt;
                int bgColor = (0x101820 & 0x00FFFFFF) | ((int) (currentAlpha * 0xB0) << 24);

                int boxW = textW + 24;
                int boxH = 14;
                int x1 = cx - boxW / 2;
                int y1 = cy - boxH / 2;

                g.fill(x1, y1, x1 + boxW, y1 + boxH, bgColor);
                GuiRenderUtil.border(g, x1, y1, x1 + boxW, y1 + boxH, borderColor);

                int diamondY = cy;
                GuiRenderUtil.diamond(g, x1 + 6, diamondY, 2, borderColor);
                GuiRenderUtil.diamond(g, x1 + boxW - 6, diamondY, 2, borderColor);

                int textX = cx - textW / 2;
                int textY = y1 + (boxH - 8) / 2;
                g.drawString(font, textStr, textX, textY, textColor, false);
        }
}
