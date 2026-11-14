package com.mingsiamz.xuanlingmod.type.spell.special;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.SpecialType;

public class Continue extends ImpComponent {

        protected Continue(String type, float proficiency) {
                super(type, proficiency);
        }

        public Continue() {
                this(SpecialType.CONTINUOUS.name, 0);
        }

        @Override
        public void run() {
        }

}
