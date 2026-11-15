package com.mingsiamz.xuanlingmod.type.spell.action;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;

public class Damage extends ImpComponent {

        protected Damage(String type, String name, float proficiency) {
                super(type, name, proficiency);
        }

        public Damage() {
                this(ActionType.GROUP, ActionType.DAMAGE.name, 0);
        }

        @Override
        public void run() {
        }

}
