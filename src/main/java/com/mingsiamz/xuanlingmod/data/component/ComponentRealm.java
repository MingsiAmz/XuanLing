package com.mingsiamz.xuanlingmod.data.component;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy.Builder;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import net.minecraft.network.chat.Component;

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
        private final double addParrentEnergy;
        private final double addChildEnergy;
        private final double addNormalLife;
        private final double addSoulLife;
        private final double nextLevelAddHealth;
        private final double nextLevelAddParrentEnergy;
        private final double nextLevelAddChildEnergy;
        private final double nextLevelAddNormalLife;
        private final double nextLevelAddSoulLife;
        private final String name;
        private boolean canTrans;

        private ComponentRealm(Builder builder) {
                this.size = builder.size;
                this.level = builder.level;
                this.addHealth = builder.addHealth;
                this.addParrentEnergy = builder.addParrentEnergy;
                this.addChildEnergy = builder.addChildEnergy;
                this.addNormalLife = builder.addNormalLife;
                this.addSoulLife = builder.addSoulLife;
                this.nextLevelAddHealth = builder.nextLevelAddHealth;
                this.nextLevelAddParrentEnergy = builder.nextLevelAddParrentEnergy;
                this.nextLevelAddChildEnergy = builder.nextLevelAddChildEnergy;
                this.nextLevelAddNormalLife = builder.nextLevelAddNormalLife;
                this.nextLevelAddSoulLife = builder.nextLevelAddSoulLife;
                this.name = builder.name;
                this.canTrans = builder.canTrans;
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

        public double getAddParrentEnergy() {
                return addParrentEnergy;
        }

        public double getAddChildEnergy() {
                return addChildEnergy;
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

        public double getNextLevelAddParrentEnergy() {
                return nextLevelAddParrentEnergy;
        }

        public double getNextLevelAddChildEnergy() {
                return nextLevelAddChildEnergy;
        }

        public double getNextLevelAddNormalLife() {
                return nextLevelAddNormalLife;
        }

        public double getNextLevelAddSoulLife() {
                return nextLevelAddSoulLife;
        }

        public boolean canTrans() {
                return canTrans;
        }

        public String getName() {
                return name;
        }

        public static Builder builder(String name) {
                return new Builder(name);
        }

        public static class Builder {
                private int size = 0;
                private int level = 0;
                private double addHealth = 0;
                private double addParrentEnergy = 0;
                private double addChildEnergy = 0;
                private double addNormalLife = 0;
                private double addSoulLife = 0;
                private double nextLevelAddHealth = 0;
                private double nextLevelAddParrentEnergy = 0;
                private double nextLevelAddChildEnergy = 0;
                private double nextLevelAddNormalLife = 0;
                private double nextLevelAddSoulLife = 0;
                private String name;
                private boolean canTrans = false;

                public Builder(String name) {
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

                public Builder addParrentEnergy(double addParrentEnergy) {
                        this.addParrentEnergy = addParrentEnergy;
                        return this;
                }

                public Builder addChildEnergy(double addChildEnergy) {
                        this.addChildEnergy = addChildEnergy;
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

                public Builder nextLevelAddParrentEnergy(double nextLevelAddParrentEnergy) {
                        this.nextLevelAddParrentEnergy = nextLevelAddParrentEnergy;
                        return this;
                }

                public Builder nextLevelAddChildEnergy(double nextLevelAddChildEnergy) {
                        this.nextLevelAddChildEnergy = nextLevelAddChildEnergy;
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

                public Builder trans() {
                        this.canTrans = true;
                        this.name = XuanLing.trans(XuanlingLangPro.ENERGY_STRING, this.name).getString();
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
