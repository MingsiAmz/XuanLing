package com.mingsiamz.xuanlingmod;

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

        static enum BasicComponent {
                PROJECTLE("projectle", 1.0f),
                AOE("aoe", 2.0f),
                SELF("self", 0.5f);

                public String type;
                public float consumeModify;
                public float proficiency = 0.0f;

                private BasicComponent(String type, float consumeModify) {
                        this.type = type;
                        this.consumeModify = consumeModify;
                }
        }

        static enum SpecialComponent {
                NORMAL,
                CONTINUOUS,
                PROFICIENCY;

                public float proficiency;
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
