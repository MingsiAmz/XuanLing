package com.mingsiamz.xuanlingmod.type.spell.action;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;

public class Damage extends ImpComponent {

        protected Damage(String type, float proficiency) {
                super(type, proficiency);
        }

        public Damage() {
                this(ActionType.DAMAGE.name, 0);
        }

        @Override
        public void run() {
        }

}
