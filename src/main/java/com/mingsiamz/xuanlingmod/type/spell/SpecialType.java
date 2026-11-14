package com.mingsiamz.xuanlingmod.type.spell;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.type.ComponentType;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.network.chat.Component;

@ComponentType (type = XuanLingTypeEnum.SPELL)
public enum SpecialType {
        NORMAL("normal", "SPECIAL", 0),
        CONTINUOUS("continuos", "SPECIAL", 0);

        public static final Component TYPE = XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "special");
        
        public String type;
        public String name;
        public float proficiency;

        private SpecialType(String name, String type, float proficiency) {
                this.name = name;
                this.type = type;
                this.proficiency = proficiency;
        }
}
