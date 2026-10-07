package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;

/**
 * @param size                   小境界层数
 * @param level                  境界所属层级
 * @param addHealth              小境界增加血量
 * @param addEnergy              小境界增加能量
 * @param addNormalLife          小境界增加身寿
 * @param addSoulLife            小境界增加灵寿
 * @param nextLevelAddHealth     大境界增加血量
 * @param nextLevelAddEnergy     大境界增加能量
 * @param nextLevelAddNormalLife 大境界增加神寿
 * @param nextLevelAddSoulLife   大境界增加灵寿
 */
public class ComponentRealm {

        private final int size;
        private final int level;
        private final double addHealth;
        private final double addEnergy;
        private final double addNormalLife;
        private final double addSoulLife;
        private final double nextLevelAddHealth;
        private final double nextLevelAddEnergy;
        private final double nextLevelAddNormalLife;
        private final double nextLevelAddSoulLife;
        private final String sName;
        private transient final Component name;

        private ComponentRealm(Builder builder) {
                this.size = builder.size;
                this.level = builder.level;
                this.addHealth = builder.addHealth;
                this.addEnergy = builder.addEnergy;
                this.addNormalLife = builder.addNormalLife;
                this.addSoulLife = builder.addSoulLife;
                this.nextLevelAddHealth = builder.nextLevelAddHealth;
                this.nextLevelAddEnergy = builder.nextLevelAddEnergy;
                this.nextLevelAddNormalLife = builder.nextLevelAddNormalLife;
                this.nextLevelAddSoulLife = builder.nextLevelAddSoulLife;
                this.name = builder.name;
                this.sName = this.name.getString();
        }

        public int size() {
                return size;
        }

        public int getIndex() {
                return level;
        }

        public double getAddHealth() {
                return addHealth;
        }

        public double getAddEnergy() {
                return addEnergy;
        }

        public double getAddNormalLife() {
                return addNormalLife;
        }

        public double getAddSoulLife() {
                return addSoulLife;
        }

        public double getNextLevelAddHealth() {
                return nextLevelAddHealth;
        }

        public double getNextLevelAddEnergy() {
                return nextLevelAddEnergy;
        }

        public double getNextLevelAddNormalLife() {
                return nextLevelAddNormalLife;
        }

        public double getNextLevelAddSoulLife() {
                return nextLevelAddSoulLife;
        }

        public Component getName() {
                return name;
        }

        public static Builder builder(String name) {
                Component com = XuanLing.trans(XuanlingLangPro.REALM_STRING, name);
                return new Builder(com);
        }

        public static class Builder {
                private int size = 0;
                private int level = 0;
                private double addHealth = 0;
                private double addEnergy = 0;
                private double addNormalLife = 0;
                private double addSoulLife = 0;
                private double nextLevelAddHealth = 0;
                private double nextLevelAddEnergy = 0;
                private double nextLevelAddNormalLife = 0;
                private double nextLevelAddSoulLife = 0;
                private Component name;

                public Builder(Component name) {
                        this.name = name;
                }

                public Builder size(int size) {
                        this.size = size;
                        return this;
                }

                public Builder level(int level) {
                        this.level = level;
                        return this;
                }

                public Builder addHealth(double addHealth) {
                        this.addHealth = addHealth;
                        return this;
                }

                public Builder addEnergy(double addEnergy) {
                        this.addEnergy = addEnergy;
                        return this;
                }

                public Builder addNormalLife(double addNormalLife) {
                        this.addNormalLife = addNormalLife;
                        return this;
                }

                public Builder addSoulLife(double addSoulLife) {
                        this.addSoulLife = addSoulLife;
                        return this;
                }

                public Builder nextLevelAddHealth(double nextLevelAddHealth) {
                        this.nextLevelAddHealth = nextLevelAddHealth;
                        return this;
                }

                public Builder nextLevelAddEnergy(double nextLevelAddEnergy) {
                        this.nextLevelAddEnergy = nextLevelAddEnergy;
                        return this;
                }

                public Builder nextLevelAddNormalLife(double nextLevelAddNormalLife) {
                        this.nextLevelAddNormalLife = nextLevelAddNormalLife;
                        return this;
                }

                public Builder nextLevelAddSoulLife(double nextLevelAddSoulLife) {
                        this.nextLevelAddSoulLife = nextLevelAddSoulLife;
                        return this;
                }

                public ComponentRealm build() {
                        if (level < 0) {
                                throw new IllegalStateException("level 不能为负数");
                        }
                        if (size < 0) {
                                throw new IllegalStateException("size 不能为负数");
                        }
                        return new ComponentRealm(this);
                }
        }
}
