package com.mingsiamz.xuanlingmod.type.spell.special;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.SpecialType;

public class Normal extends ImpComponent {

        protected Normal(String type, float proficiency) {
                super(type, proficiency);
        }

        public Normal() {
                this(SpecialType.NORMAL.name, 0);
        }

        @Override
        public void run() {
        }

}
