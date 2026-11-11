package com.mingsiamz.xuanlingmod.type.spell;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import net.minecraft.network.chat.Component;

public enum SpecialType implements IComponent {
        NORMAL("normal", "SPECIAL", 0),
        CONTINUOUS("continuos", "SPECIAL", 0);

        private float proficiency;
        private String type;
        private String name;

        public Component TYPE = XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "special");
        
        private SpecialType(String name, String type, float proficiency) {
                this.type = type;
                this.proficiency = proficiency;
                this.name = name;
        }

        @Override
        public String type() {
                return null;
        }

        @Override
        public void run() {
                switch (this) {
                        case NORMAL -> normal();
                        case CONTINUOUS -> continuous();
                }
        }

        private void normal() {

        }

        private void continuous() {

        }

        @Override
        public float getProficiency() {
                return 0;
        }

        @Override
        public void putComponentToPlayerByWeight() {
        }

        @Override
        public Component trans() {
                return XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, name());
        }

        @Override
        public Component transType() {
                return TYPE;
        }

}
