package com.mingsiamz.xuanlingmod.type.spell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import net.minecraft.network.chat.Component;

public enum BasicType implements IComponent {
        PROJECTLE("projectle", "BASIC", 1.0f, -1),
        AOE("aoe", "BASIC", 2.0f, -1),
        SELF("self", "BASIC", 0.5f, -1);

        public float consumeModify;
        private float weight;
        private String type;
        private String name;
        private float proficiency;
        private Component TYPE = XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, "basic");
        private LinkedHashMap<String, Object> params;
        private static HashMap<Integer, List<BasicType>> typesMap = new LinkedHashMap<>();

        private BasicType(String name, String type, float consumeModify, float weight) {
                this.type = type;
                this.consumeModify = consumeModify;
                this.weight = weight;
        }

        public BasicType withParams(LinkedHashMap<String, Object> params) {
                this.params = params;
                return this;
        }

        @Override
        public String type() {
                return this.type;
        }

        @Override
        public void run() {
                switch (this) {

                        case PROJECTLE:
                                System.out.println("Running projectle component...");
                                break;
                        case AOE:
                                System.out.println("Running aoe component...");
                                break;
                        case SELF:
                                System.out.println("Running self component...");
                                break;
                }

        }

        private void projectle() {

        }

        private void aoe() {

        }

        private void self() {

        }

        @Override
        public float getProficiency() {
                return this.proficiency;
        }

        @Override
        public void putComponentToPlayerByWeight() {
                // 根据weight来生成随机获取权重，越低的权重越难获取，从10000到0
                int weight = (int) Math.floor(Math.random() * 100);
                BasicType[] values = values();
                for (BasicType type : values) {

                }

        }

        @Override
        public String toString() {
                return "BasicType{" +
                                "type='" + type + '\'' +
                                ", consumeModify=" + consumeModify +
                                ", proficiency=" + proficiency +
                                ", weight=" + weight +
                                '}';
        }

        @Override
        public Component trans() {
                return XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, name());
        }

        @Override
        public Component transType() {
                return TYPE;
        }
}
