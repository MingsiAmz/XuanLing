package com.mingsiamz.xuanlingmod.type.spell.action;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;

public class RecoverCE extends ImpComponent {

        protected RecoverCE(String type, float proficiency) {
                super(type, proficiency);
        }

        public RecoverCE() {
                this(ActionType.RECOVER_C_ENERGY.name, 0);
        }

        @Override
        public void run() {
        }

}
