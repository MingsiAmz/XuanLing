package com.mingsiamz.xuanlingmod.type.spell;

import java.util.LinkedHashMap;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import net.minecraft.network.chat.Component;

public enum ActionType implements IComponent<ActionType> {
        DAMAGE("damage", "ACTION"),
        RECOVER_HEALTH("recover_health", "ACTION"),
        RECOVER_P_ENERGY("recover_p_energy", "ACTION"),
        RECOVER_C_ENERGY("recover_c_energy", "ACTION");

        private String type;
        private String name;
        private double value = 0;
        private float proficiency = 0;
        private LinkedHashMap<String, Object> params;

        public Component TYPE = XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "action");

        private ActionType(String name, String type) {
                this.type = type;
                this.name = name;
        }

        @Override
        public String type() {
                return this.type;
        }

        @Override
        public void run() {
                switch (this) {
                        case DAMAGE -> damage();
                        case RECOVER_HEALTH -> recoverHealth();
                        case RECOVER_P_ENERGY -> recoverPEnergy();
                        case RECOVER_C_ENERGY -> recoverCEnergy();
                }
        }

        private void damage() {

        }

        private void recoverHealth() {

        }

        private void recoverPEnergy() {

        }

        private void recoverCEnergy() {

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

        @Override
        public ActionType withParams(LinkedHashMap<String, Object> params) {
                return this;
        }

        @Override
        public LinkedHashMap<String, Object> getParams() {
                return this.params;
        }

}
