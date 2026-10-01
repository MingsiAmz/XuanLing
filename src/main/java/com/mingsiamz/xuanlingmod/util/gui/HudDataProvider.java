package com.mingsiamz.xuanlingmod.util.gui;

import net.minecraft.client.player.LocalPlayer;

public final class HudDataProvider {
        private HudDataProvider() {
        }

        // TODO: 替换为从玩家 Capability/Attachment 读取
        public static int getQi(LocalPlayer player) {
                return 3200;
        }

        public static int getMaxQi(LocalPlayer player) {
                return 5000;
        }

        public static int getNeili(LocalPlayer player) {
                return 1500;
        }

        public static int getMaxNeili(LocalPlayer player) {
                return 3000;
        }
}
