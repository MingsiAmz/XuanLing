package com.mingsiamz.xuanlingmod.type.spell.action;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;

public class RecoverHp extends ImpComponent {

        protected RecoverHp(String type, float proficiency) {
                super(type, proficiency);
        }

        public RecoverHp() {
                this(ActionType.RECOVER_HEALTH.name, 0);
        }

        @Override
        public void run() {
        }

}
