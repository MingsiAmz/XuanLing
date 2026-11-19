package com.mingsiamz.xuanlingmod.util.gui;

import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy.Type;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class HudDataProvider {
        public static Minecraft minecraft = Minecraft.getInstance();
        public static DefaultEnergy energy = RegisterAttachments.getDefaultEnergy(minecraft.player);

        private HudDataProvider() {
        }

        public static double getQi() {
                return energy.getNowValue(ComponentEnergy.Type.PARRENT);
        }

        public static double getMaxQi() {
                return energy.getMaxPValue();
        }

        public static double getNeili() {
                return energy.getNowValue(ComponentEnergy.Type.CHILD);
        }

        public static double getMaxNeili() {
                return energy.getMaxCValue();
        }

        public static float getEnergyMultiplier() {
                if (minecraft.player == null)
                        return 0f;
                BasicData data = RegisterAttachments.getBasicData(minecraft.player);
                return data != null ? data.getEnergyMultiplier() : 0f;
        }
}
