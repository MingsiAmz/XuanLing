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

public enum BasicType implements IComponent<BasicType> {
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

        @Override 
        public BasicType withParams(LinkedHashMap<String, Object> params) {
                this.params = params;
                return this;
        }

        @Override
        public LinkedHashMap<String, Object> getParams() {
                return this.params;
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
                // 如果 weight == -1，说明是初始就有的组件
                if (this.weight == -1) {
                        // TODO: 替换为实际赋予玩家的逻辑，例如放入玩家的 Attachment 中
                        System.out.println("[XuanLing] 初始获得组件: " + this.name());
                        return;
                }

                // 生成 0 ~ 10000 的随机整数（包含两端）
                int rand = java.util.concurrent.ThreadLocalRandom.current().nextInt(10001);
                
                // 如果随机数小于等于权重，则成功获取
                if (rand <= this.weight) {
                        // TODO: 替换为实际赋予玩家的逻辑
                        System.out.println("[XuanLing] 通过权重获得组件: " + this.name() 
                                + " (随机数: " + rand + ", 权重: " + this.weight + ")");
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
