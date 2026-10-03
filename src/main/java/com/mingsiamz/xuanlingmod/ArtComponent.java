package com.mingsiamz.xuanlingmod;

import java.util.Map;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.gameevent.GameEvent;

public class ArtComponent implements IComponent {
    private static final XuanLingTypeEnum DEFAULT_TYPE = XuanLingTypeEnum.ART;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public int getRarity() {
        return 0;
    }

    @Override
    public Map<String, Object> getProperties() {
        return null;
    }

    @Override
    public void onAttach(Entity target) {
    }

    @Override
    public void onTick(Entity target, int tick) {
    }

    @Override
    public void onDetach(Entity target) {
    }

    @Override
    public void onEvent(Entity target, GameEvent event) {
    }

}
