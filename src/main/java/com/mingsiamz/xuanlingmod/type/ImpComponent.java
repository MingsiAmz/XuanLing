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
                this.params.putIfAbsent(type, value);
                return this;
        }

        @Override
        public ConcurrentHashMap<ParamType, Object> getParams() {
                return this.params;
        }
}
