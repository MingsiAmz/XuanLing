package com.mingsiamz.xuanlingmod.effects;

import java.awt.Color;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.BasicEnergy;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectMeditation extends MobEffect {

        public EffectMeditation() {
                super(MobEffectCategory.NEUTRAL, Color.WHITE.getRGB());
        }

        @Override
        public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
                DefaultEnergy defaultEnergy = RegisterAttachments.getDefaultEnergy(livingEntity);
                BasicData basicData = RegisterAttachments.getBasicData(livingEntity);

                // if (defaultEnergy == null || basicData == null)
                //         return false;

                // double maxPValue = defaultEnergy.getMaxValue(ComponentEnergy.Type.PARRENT);
                // double maxCValue = defaultEnergy.getMaxValue(ComponentEnergy.Type.CHILD);
                // double nowPValue = defaultEnergy.getNowValue(ComponentEnergy.Type.PARRENT);
                // double nowCValue = defaultEnergy.getNowValue(ComponentEnergy.Type.CHILD);
                // double target = 10;

                // if (maxPValue == 0 && maxCValue == 0) {
                //         basicData.setMeditation(false);
                //         return false;
                // }

                // if (maxPValue >= 1)
                //         defaultEnergy.setEnergyValue(ComponentEnergy.Type.PARRENT, nowPValue + target);

                // if (maxCValue >= 1)
                //         defaultEnergy.setEnergyValue(ComponentEnergy.Type.CHILD, nowCValue + target);

                return super.applyEffectTick(livingEntity, amplifier);
        }

        @Override
        public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
                return tickCount % 20 == 0;
        }

}
