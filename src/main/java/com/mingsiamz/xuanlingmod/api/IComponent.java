package com.mingsiamz.xuanlingmod.api;

import java.util.LinkedHashMap;

import net.minecraft.network.chat.Component;

public interface IComponent<T extends IComponent<T>> {
        String type();

        String name();

        void run();

        float getProficiency();

        void putComponentToPlayerByWeight();

        T withParams(LinkedHashMap<String, Object> params);

        LinkedHashMap<String, Object> getParams();

        Component trans();

        Component transType();
}
