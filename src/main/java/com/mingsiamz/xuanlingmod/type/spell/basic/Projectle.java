package com.mingsiamz.xuanlingmod.type.spell.basic;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;

public class Projectle extends ImpComponent {

        protected Projectle(String type, float proficiency) {
                super(type, proficiency);
        }

        public Projectle() {
                this(BasicType.PROJECTLE.name, 0);
        }

        @Override
        public void run() {
        }
}
