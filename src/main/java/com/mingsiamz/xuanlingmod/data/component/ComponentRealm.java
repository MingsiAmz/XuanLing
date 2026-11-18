package com.mingsiamz.xuanlingmod.data.component;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy.Builder;
import com.mingsiamz.xuanlingmod.provider.XuanlingLangPro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;


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

        
        ComponentRealm(int size, int level, double addHealth, double addParrentEnergy, double addChildEnergy,
                        double addNormalLife, double addSoulLife, double nextLevelAddHealth,
                        double nextLevelAddParrentEnergy, double nextLevelAddChildEnergy,
                        double nextLevelAddNormalLife, double nextLevelAddSoulLife,
                        String name, boolean canTrans) {
                this.size = size;
                this.level = level;
                this.addHealth = addHealth;
                this.addParrentEnergy = addParrentEnergy;
                this.addChildEnergy = addChildEnergy;
                this.addNormalLife = addNormalLife;
                this.addSoulLife = addSoulLife;
                this.nextLevelAddHealth = nextLevelAddHealth;
                this.nextLevelAddParrentEnergy = nextLevelAddParrentEnergy;
                this.nextLevelAddChildEnergy = nextLevelAddChildEnergy;
                this.nextLevelAddNormalLife = nextLevelAddNormalLife;
                this.nextLevelAddSoulLife = nextLevelAddSoulLife;
                this.name = name;
                this.canTrans = canTrans;
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

        public static final Codec<ComponentRealm> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codec.INT.fieldOf("size").forGetter(ComponentRealm::size),
                        Codec.INT.fieldOf("level").forGetter(ComponentRealm::getIndex),
                        Codec.DOUBLE.fieldOf("addHealth").forGetter(ComponentRealm::getAddHealth),
                        Codec.DOUBLE.fieldOf("addParrentEnergy").forGetter(ComponentRealm::getAddParrentEnergy),
                        Codec.DOUBLE.fieldOf("addChildEnergy").forGetter(ComponentRealm::getAddChildEnergy),
                        Codec.DOUBLE.fieldOf("addNormalLife").forGetter(ComponentRealm::getAddNormalLife),
                        Codec.DOUBLE.fieldOf("addSoulLife").forGetter(ComponentRealm::getAddSoulLife),
                        Codec.DOUBLE.fieldOf("nextLevelAddHealth").forGetter(ComponentRealm::getNextLevelAddHealth),
                        Codec.DOUBLE.fieldOf("nextLevelAddParrentEnergy").forGetter(ComponentRealm::getNextLevelAddParrentEnergy),
                        Codec.DOUBLE.fieldOf("nextLevelAddChildEnergy").forGetter(ComponentRealm::getNextLevelAddChildEnergy),
                        Codec.DOUBLE.fieldOf("nextLevelAddNormalLife").forGetter(ComponentRealm::getNextLevelAddNormalLife),
                        Codec.DOUBLE.fieldOf("nextLevelAddSoulLife").forGetter(ComponentRealm::getNextLevelAddSoulLife),
                        Codec.STRING.fieldOf("name").forGetter(ComponentRealm::getName),
                        Codec.BOOL.fieldOf("canTrans").forGetter(ComponentRealm::canTrans))
                        .apply(instance, ComponentRealm::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, ComponentRealm> STREAM_CODEC = new StreamCodec<>() {
                @Override
                public ComponentRealm decode(RegistryFriendlyByteBuf buf) {
                        return new ComponentRealm(
                                buf.readInt(),
                                buf.readInt(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readDouble(),
                                buf.readUtf(64),
                                buf.readBoolean()
                        );
                }

                @Override
                public void encode(RegistryFriendlyByteBuf buf, ComponentRealm value) {
                        buf.writeInt(value.size);
                        buf.writeInt(value.level);
                        buf.writeDouble(value.addHealth);
                        buf.writeDouble(value.addParrentEnergy);
                        buf.writeDouble(value.addChildEnergy);
                        buf.writeDouble(value.addNormalLife);
                        buf.writeDouble(value.addSoulLife);
                        buf.writeDouble(value.nextLevelAddHealth);
                        buf.writeDouble(value.nextLevelAddParrentEnergy);
                        buf.writeDouble(value.nextLevelAddChildEnergy);
                        buf.writeDouble(value.nextLevelAddNormalLife);
                        buf.writeDouble(value.nextLevelAddSoulLife);
                        buf.writeUtf(value.name, 64);
                        buf.writeBoolean(value.canTrans);
                }
        };
}
