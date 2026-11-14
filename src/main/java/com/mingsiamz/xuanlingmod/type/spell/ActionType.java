package com.mingsiamz.xuanlingmod.type.spell;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.type.ComponentType;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.network.chat.Component;

@ComponentType (type = XuanLingTypeEnum.SPELL)
public enum ActionType {
        DAMAGE("damage", "ACTION"),
        RECOVER_HEALTH("recover_health", "ACTION"),
        RECOVER_P_ENERGY("recover_p_energy", "ACTION"),
        RECOVER_C_ENERGY("recover_c_energy", "ACTION");

        public static final Component TYPE = XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "action");

        public String type;
        public String name;

        private ActionType(String name, String type) {
                this.name = name;
                this.type = type;
        }

}
