package com.mingsiamz.xuanlingmod.gui.component.data;

import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

public final class ComponentRegistry {
        private ComponentRegistry() {
        }

        public static List<ComponentDef> getAvailable(LocalPlayer player) {
                List<ComponentDef> list = new ArrayList<>();

                // 测试阶段：固定提供一个 Example
                list.add(new ComponentDef("example", "示例组件",
                                ComponentCategory.BASIC, 110, 46, 0xFF4DD0E1));

                // 示例：玩家拥有某物品/数据时才开放对应组件
                // if (player != null && PlayerData.hasComponent(player, "logic_gate")) {
                // list.add(new ComponentDef("logic_gate", "逻辑门", ComponentCategory.LOGIC));
                // }
                // if (player != null && PlayerData.hasComponent(player, "render_quad")) {
                // list.add(new ComponentDef("render_quad", "渲染方块", ComponentCategory.RENDER));
                // }

                return list;
        }
}
