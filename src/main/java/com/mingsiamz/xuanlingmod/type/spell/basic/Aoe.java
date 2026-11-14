package com.mingsiamz.xuanlingmod.type.spell.basic;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;

public class Aoe extends ImpComponent {

        protected Aoe(String type, float proficiency) {
                super(type, proficiency);
        }

        public Aoe() {
                this(BasicType.AOE.name, 0);
        }

        @Override
        public void run() {
        }

}
