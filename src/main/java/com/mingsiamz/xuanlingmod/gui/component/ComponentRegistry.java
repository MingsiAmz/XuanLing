package com.mingsiamz.xuanlingmod.gui.component;

import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;
import com.mingsiamz.xuanlingmod.type.spell.SpecialType;

public final class ComponentRegistry {
        private ComponentRegistry() {
        }

        public static List<IComponent<?>> getAvailable(LocalPlayer player) {
                List<IComponent<?>> list = new ArrayList<>();

                for (BasicType type : BasicType.values()) 
                        list.add(type);

                for (ActionType type : ActionType.values())
                        list.add(type);

                for (SpecialType type : SpecialType.values())
                        list.add(type);

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
