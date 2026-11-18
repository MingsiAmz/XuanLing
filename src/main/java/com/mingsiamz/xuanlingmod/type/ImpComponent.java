package com.mingsiamz.xuanlingmod.type;

import java.util.concurrent.ConcurrentHashMap;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IComponent;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;
import com.mingsiamz.xuanlingmod.type.spell.ParamType;

import net.minecraft.network.chat.Component;

public abstract class ImpComponent implements IComponent<ImpComponent> {
        protected String name;
        protected String type;
        protected float proficiency;
        protected ConcurrentHashMap<ParamType, Object> params = new ConcurrentHashMap<>();

        protected ImpComponent(String type, String name, float proficiency) {
                this.type = type;
                this.name = name;
                this.proficiency = proficiency;
                switch (name) {
                        case "projectle" -> withParams(ParamType.INIT_SPEED, 0.0)
                                        .withParams(ParamType.SPEED, 0.0)
                                        .withParams(ParamType.LIFETIME, 0)
                                        .withParams(ParamType.MAX_TARGETS, 0);
                        case "aoe" -> withParams(ParamType.RADIUS, 0.0)
                                        .withParams(ParamType.DURATION, 0)
                                        .withParams(ParamType.TICK_INTERVAL, 0)
                                        .withParams(ParamType.MAX_TARGETS, 0);
                        case "self" -> withParams(ParamType.COOLDOWN, 0);
                        case "damage" -> withParams(ParamType.DAMAGE, 0.0)
                                        .withParams(ParamType.KNOCKBACK, 0.0);
                        case "recover_health", "recover_p_energy", "recover_c_energy" ->
                                withParams(ParamType.NORMAL_VALUE, 0.0);
                        case "normal" -> withParams(ParamType.COOLDOWN, 0);
                        case "continuos" -> withParams(ParamType.DURATION, 0)
                                        .withParams(ParamType.TICK_INTERVAL, 0);
                        default -> {
                        }
                }
        }

        @Override
        public String type() {
                return this.type;
        }

        @Override
        public String name() {
                return this.name;
        }

        @Override
        public Component trans() {
                return XuanLing.trans(XuanlingLangPro.COMPONEN_STRING, this.name);
        }

        @Override
        public float getProficiency() {
                return this.proficiency;
        }

        @Override
        public void putComponentToPlayerByWeight() {

        }

        @Override
        public ImpComponent withParams(ParamType type, Object value) {
                this.params.put(type, value);
                return this;
        }

        @Override
        public ConcurrentHashMap<ParamType, Object> getParams() {
                return this.params;
        }
}
