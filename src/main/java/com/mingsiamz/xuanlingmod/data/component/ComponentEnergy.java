package com.mingsiamz.xuanlingmod.data.component;

import java.util.ArrayList;
import java.util.List;

import com.mingsiamz.xuanlingmod.Config;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.api.IEnergyProducer;
import com.mingsiamz.xuanlingmod.api.IEnergyProducer.EnergyTrigger;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.energy.IEnergyStorage;

public class ComponentEnergy implements IEnergyStorage {
        public static enum Type {
                PARRENT("parrent"),
                CHILD("child");

                private String type;

                private Type(String type) {
                        this.type = type;
                }
        }

        private String name;
        private Type type;
        private double value;
        private double maxValue;
        private boolean canExtract;
        private boolean canReceive;
        private boolean canTrans = false;
        private transient Component trans;
        private List<IEnergyProducer> produces = new ArrayList<>();

        private ComponentEnergy(ComponentEnergy c) {
                this.name = c.name;
                this.value = c.value;
                this.maxValue = c.maxValue;
                this.type = c.type;
                this.canExtract = c.canExtract;
                this.canReceive = c.canReceive;
                this.trans = c.trans;
                this.canTrans = c.canTrans;
                this.produces = c.produces;
        }

        private ComponentEnergy(Builder builder) {
                this.name = builder.name;
                this.value = builder.value;
                this.maxValue = builder.maxValue;
                this.type = builder.type;
                this.canExtract = builder.canExtract;
                this.canReceive = builder.canReceive;
                this.trans = builder.trans;
                this.canTrans = builder.canTrans;
                this.produces = builder.produces;
        }

        public String getName() {
                return this.name;
        }

        public double getValue() {
                return this.value;
        }

        public double getMaxValue() {
                return this.maxValue;
        }

        public Type getType() {
                return this.type;
        }

        public Component getTrans() {
                return this.trans;
        }

        public boolean canTrans() {
                return this.canTrans;
        }

        public ComponentEnergy copy() {
                return new ComponentEnergy(this);
        }
        
        public static Builder builder(String name, Type type) {
                return new Builder(name, type);
        }

        @Override
        public int receiveEnergy(int toReceive, boolean simulate) {
                if (!simulate) {
                        if (this.type == Type.PARRENT) {
                                if (this.value + toReceive >= Config.getMaxValue(Config.PARRENT_ENERGY_RANGE))
                                        this.value = Config.getMaxValue(Config.PARRENT_ENERGY_RANGE);
                                else
                                        this.value += toReceive;
                        } else {
                                if (this.value + toReceive >= Config.getMaxValue(Config.CHILD_ENERGY_RANGE))
                                        this.value = Config.getMaxValue(Config.CHILD_ENERGY_RANGE);
                                else
                                        this.value += toReceive;
                        }
                }
                return (int) this.value;
        }

        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
                if (!simulate) {
                        if (this.type == Type.PARRENT) {
                                if (this.value - toExtract <= 0)
                                        this.value = 0;
                                else
                                        this.value -= toExtract;
                        } else {
                                if (this.value - toExtract <= 0)
                                        this.value = 0;
                                else
                                        this.value -= toExtract;
                        }
                }
                return (int) this.value;
        }

        @Override
        public int getEnergyStored() {
                return (int) this.value;
        }

        @Override
        public int getMaxEnergyStored() {
                return (int) this.maxValue;
        }

        @Override
        public boolean canExtract() {
                return this.canExtract;
        }

        @Override
        public boolean canReceive() {
                return this.canReceive;
        }

        public void triggerProducer(Entity entity, EnergyTrigger trigger, double baseAmount) {
                for (IEnergyProducer producer: this.produces) {
                        double produce = producer.producerEnergy(entity, trigger, baseAmount);
                        this.receiveEnergy((int)produce, false);
                }
        }

        public static class Builder {
                private String name;
                private double value;
                private double maxValue;
                private Type type;
                private Component trans;
                private boolean canExtract = true;
                private boolean canReceive = true;
                private boolean canTrans = false;
                private List<IEnergyProducer> produces = new ArrayList<>();

                public Builder(String name, Type type) {
                        this.name = name;
                        this.type = type;
                }

                public Builder value(double value) {
                        this.value = value;
                        return this;
                }

                public Builder maxValue(double maxValue) {
                        this.maxValue = maxValue;
                        return this;
                }

                public Builder canExtract(boolean canExtract) {
                        this.canExtract = canExtract;
                        return this;
                }

                public Builder canReceive(boolean canReceive) {
                        this.canReceive = canReceive;
                        return this;
                }

                public Builder addProducer(IEnergyProducer producer) {
                        this.produces.add(producer);
                        return this;
                }

                public Builder trans() {
                        this.trans = XuanLing.trans(XuanlingLangPro.ENERGY_STRING, this.name);
                        this.canTrans = true;
                        return this;
                }

                public ComponentEnergy build() {
                        return new ComponentEnergy(this);
                }
        }

}
