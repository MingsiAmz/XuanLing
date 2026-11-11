package com.mingsiamz.xuanlingmod.type;

public enum XuanLingTypeEnum {
        ART("gong_fa"),
        SPELL("shen_tong"),
        REALM("jing_jie"),
        ENERGY("neng_liang");

        public String type;

        private XuanLingTypeEnum(String type) {
                this.type = type;
        }

        static enum RealmComponent {
                
        }

        static enum ArtEffectComponent {
                DAMAGE,
                HP,
                ENERGY_REPLY,
                NORMAL_LIFE,
                SOUL_LIFE
        }

        static enum SpellEffectComponent {

        }
}
