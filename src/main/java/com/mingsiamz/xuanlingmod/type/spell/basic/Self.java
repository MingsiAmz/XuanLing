package com.mingsiamz.xuanlingmod.type.spell.basic;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;

public class Self extends ImpComponent {

        protected Self(String type, String name, float proficiency) {
                super(type, name, proficiency);
        }

        public Self() {
                this(BasicType.GROUP, BasicType.SELF.name, 0);
        }

        @Override
        public void run() {
        }

}
