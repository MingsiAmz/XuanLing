package com.mingsiamz.xuanlingmod.api;

import net.minecraft.world.entity.Entity;

public interface IEnergyProducer {

        double producerEnergy(Entity entigy, EnergyTrigger trigger, double baseAmount);

        public static enum EnergyTrigger{
                KILL_ENEITY, // 击杀生物
                MEDITATION, // 打坐
        }
}
