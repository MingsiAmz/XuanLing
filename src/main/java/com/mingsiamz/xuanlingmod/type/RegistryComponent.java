package com.mingsiamz.xuanlingmod.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mingsiamz.xuanlingmod.type.spell.ActionType;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;
import com.mingsiamz.xuanlingmod.type.spell.SpecialType;
import com.mingsiamz.xuanlingmod.type.spell.action.Damage;
import com.mingsiamz.xuanlingmod.type.spell.action.RecoverCE;
import com.mingsiamz.xuanlingmod.type.spell.action.RecoverHp;
import com.mingsiamz.xuanlingmod.type.spell.action.RecoverPE;
import com.mingsiamz.xuanlingmod.type.spell.basic.Aoe;
import com.mingsiamz.xuanlingmod.type.spell.basic.Projectle;
import com.mingsiamz.xuanlingmod.type.spell.basic.Self;
import com.mingsiamz.xuanlingmod.type.spell.special.Continue;
import com.mingsiamz.xuanlingmod.type.spell.special.Normal;

import net.minecraft.client.player.LocalPlayer;

public final class RegistryComponent {

        public static final Map<XuanLingTypeEnum, Map<String, ImpComponent>> maps = new ConcurrentHashMap<>();

        static {
                registerSpell(BasicType.PROJECTLE.name, new Projectle());
                registerSpell(BasicType.SELF.name, new Self());
                registerSpell(BasicType.AOE.name, new Aoe());
                registerSpell(ActionType.DAMAGE.name, new Damage());
                registerSpell(ActionType.RECOVER_P_ENERGY.name, new RecoverPE());
                registerSpell(ActionType.RECOVER_C_ENERGY.name, new RecoverCE());
                registerSpell(ActionType.RECOVER_HEALTH.name, new RecoverHp());
                registerSpell(SpecialType.NORMAL.name, new Normal());
                registerSpell(SpecialType.CONTINUOUS.name, new Continue());
        }

        public RegistryComponent() {
        }

        // public static List<IComponent<?>> getAvailable(XuanLingTypeEnum type,
        // LocalPlayer player) {
        public static List<ImpComponent> getAvailable(XuanLingTypeEnum type, LocalPlayer player) {

                List<ImpComponent> list = null;

                switch (type) {
                        case SPELL -> list = regSpell(player);
                        case ART -> list = regART(player);
                        case REALM -> list = regREALM(player);
                        default -> throw new IllegalArgumentException("unknown type + " + type);
                }

                // 示例：玩家拥有某物品/数据时才开放对应组件
                // if (player != null && PlayerData.hasComponent(player, "logic_gate")) {
                // list.add(new ComponentDef("logic_gate", "逻辑门", ComponentCategory.LOGIC));
                // }
                // if (player != null && PlayerData.hasComponent(player, "render_quad")) {
                // list.add(new ComponentDef("render_quad", "渲染方块", ComponentCategory.RENDER));
                // }

                return list;
        }

        private static List<ImpComponent> regSpell(LocalPlayer player) {
                List<ImpComponent> list = new ArrayList<>();

                for (BasicType type : BasicType.values())
                        list.add(getSpellComponent(type.name));

                for (ActionType type : ActionType.values())
                        list.add(getSpellComponent(type.name));

                for (SpecialType type : SpecialType.values())
                        list.add(getSpellComponent(type.name));

                return list;
        }

        private static List<ImpComponent> regART(LocalPlayer player) {
                List<ImpComponent> list = new ArrayList<>();
                return list;
        }

        private static List<ImpComponent> regREALM(LocalPlayer player) {
                List<ImpComponent> list = new ArrayList<>();
                return list;
        }

        public static ImpComponent getComponent(XuanLingTypeEnum type, String name) {
                if (maps == null || maps.isEmpty() || maps.get(type) == null)
                        return null;
                return maps.get(type).get(name);
        }

        public static ImpComponent getSpellComponent(String name) {
                return maps.get(XuanLingTypeEnum.SPELL).get(name);
        }

        public static ImpComponent getARTComponent(String name) {
                return maps.get(XuanLingTypeEnum.ART).get(name);
        }

        public static ImpComponent getRealmComponent(String name) {
                return maps.get(XuanLingTypeEnum.REALM).get(name);
        }

        private static void register(XuanLingTypeEnum type, String name, ImpComponent component) {
                maps.computeIfAbsent(type, k -> new ConcurrentHashMap<>()).put(name, component);
        }

        private static void registerSpell(String name, ImpComponent component) {
                maps.computeIfAbsent(XuanLingTypeEnum.SPELL, k -> new ConcurrentHashMap<>()).put(name, component);
        }

        private static void registerART(String name, ImpComponent component) {
                maps.computeIfAbsent(XuanLingTypeEnum.ART, k -> new ConcurrentHashMap<>()).put(name, component);
        }

        private static void registerRealm(String name, ImpComponent component) {
                maps.computeIfAbsent(XuanLingTypeEnum.REALM, k -> new ConcurrentHashMap<>()).put(name, component);
        }

}
