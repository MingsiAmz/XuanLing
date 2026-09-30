package com.mingsiamz.xuanlingmod;

import java.util.Map;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;

public interface IComponent {
    String getId();
    String getType();
    int getRarity();
    Map<String, Object> getProperties();

    void onAttach(Entity target);
    void onTick(Entity target, int tick);
    void onDetach(Entity target);
    void onEvent(Entity target, GameEvent event);
}
