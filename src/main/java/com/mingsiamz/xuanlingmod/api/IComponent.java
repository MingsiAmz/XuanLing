package com.mingsiamz.xuanlingmod.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mingsiamz.xuanlingmod.type.spell.ParamType;

import net.minecraft.network.chat.Component;

public interface IComponent<T extends IComponent<T>> {
        String type();

        String name();

        void run();

        float getProficiency();

        void putComponentToPlayerByWeight();

        T withParams(ParamType type, Object value);

        ConcurrentHashMap<ParamType, Object> getParams();

        Component trans();
}
