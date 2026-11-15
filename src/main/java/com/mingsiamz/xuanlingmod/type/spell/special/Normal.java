package com.mingsiamz.xuanlingmod.type.spell.special;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.SpecialType;

public class Normal extends ImpComponent {

        protected Normal(String type, String name, float proficiency) {
                super(type, name, proficiency);
        }

        public Normal() {
                this(SpecialType.GROUP, SpecialType.NORMAL.name, 0);
        }

        @Override
        public void run() {
        }

}
