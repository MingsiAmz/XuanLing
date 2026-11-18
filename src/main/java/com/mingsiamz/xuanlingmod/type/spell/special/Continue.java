package com.mingsiamz.xuanlingmod.type.spell.special;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.SpecialType;

public class Continue extends ImpComponent {

        protected Continue(String type, String name, float proficiency) {
                super(type, name, proficiency);
        }

        public Continue() {
                this(SpecialType.GROUP, SpecialType.CONTINUOUS.name, 0);
        }

        @Override
        public void run() {
        }

}
