package com.mingsiamz.xuanlingmod.type.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.type.ComponentType;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.network.chat.Component;

@ComponentType (type = XuanLingTypeEnum.SPELL)
public enum BasicType {
        PROJECTLE("projectle", "BASIC", -1),
        AOE("aoe", "BASIC", -1),
        SELF("self", "BASIC", -1);

        public static final Component TYPE = XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "basic_type");

        public float weight;
        public String type;
        public String name;

        private BasicType(String name, String type, float weight) {
                this.name = name;
                this.type = type;
                this.weight = weight;
        }

}
