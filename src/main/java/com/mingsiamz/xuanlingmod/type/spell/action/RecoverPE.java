package com.mingsiamz.xuanlingmod.type.spell.action;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;

public class RecoverPE extends ImpComponent {

        protected RecoverPE(String type, float proficiency) {
                super(type, proficiency);
        }

        public RecoverPE() {
                this(ActionType.RECOVER_P_ENERGY.name, 0);
        }

        @Override
        public void run() {
        }

}
