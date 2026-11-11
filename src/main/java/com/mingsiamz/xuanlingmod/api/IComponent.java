package com.mingsiamz.xuanlingmod.api;

import net.minecraft.network.chat.Component;

public interface IComponent {
        String type();
        String name();
        void run();
        float getProficiency();
        void putComponentToPlayerByWeight();
        Component trans();
        Component transType();
}
