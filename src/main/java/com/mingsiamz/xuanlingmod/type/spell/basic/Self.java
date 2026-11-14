package com.mingsiamz.xuanlingmod.type.spell.basic;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;

public class Self extends ImpComponent {

        protected Self(String type, float proficiency) {
                super(type, proficiency);
        }

        public Self() {
                this(BasicType.SELF.name, 0);
        }

        @Override
        public void run() {
        }

}
