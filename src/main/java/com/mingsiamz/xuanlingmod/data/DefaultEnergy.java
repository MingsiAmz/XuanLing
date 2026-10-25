package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy;
import com.mingsiamz.xuanlingmod.data.component.ComponentRealm;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class DefaultEnergy {

        private String energyType = "normal";
        private String parrentName = ComponentEnergy.Type.PARRENT.name();
        private String childName = ComponentEnergy.Type.CHILD.name();
        private double nowPValue = 0;
        private double maxPValue = 0;
        private double nowCValue = 0;
        private double maxCValue = 0;

        public DefaultEnergy() {

        }

        public DefaultEnergy(RegistryFriendlyByteBuf buf) {
                this.energyType = buf.readUtf();
                this.parrentName = buf.readUtf();
                this.childName = buf.readUtf();
                this.nowPValue = buf.readDouble();
                this.maxPValue = buf.readDouble();
                this.nowCValue = buf.readDouble();
                this.maxCValue = buf.readDouble();
        }

        public DefaultEnergy(String energyType,
                             String pName,
                             String cName,
                             double nowPValue,
                             double maxPValue,
                             double nowCValue,
                             double maxCValue) {
                this.energyType = energyType;
                this.parrentName = pName;
                this.childName = cName;
                this.nowPValue = nowPValue;
                this.maxPValue = maxPValue;
                this.nowCValue = nowCValue;
                this.maxCValue = maxCValue;
        }

        public static final Codec<DefaultEnergy> CODEC = RecordCodecBuilder.create(builder -> builder
                        .group(
                                        Codec.STRING.fieldOf("energy_type").forGetter(d -> d.energyType),
                                        Codec.STRING.fieldOf("parrent_energy_name").forGetter(d -> d.parrentName),
                                        Codec.STRING.fieldOf("child_energy_name").forGetter(d -> d.childName),
                                        Codec.DOUBLE.fieldOf("now_parrent_value").forGetter(d -> d.nowPValue),
                                        Codec.DOUBLE.fieldOf("max_parrent_value").forGetter(d -> d.maxPValue),
                                        Codec.DOUBLE.fieldOf("now_child_value").forGetter(d -> d.nowCValue),
                                        Codec.DOUBLE.fieldOf("max_child_value").forGetter(d -> d.maxCValue))
                        .apply(builder, DefaultEnergy::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, DefaultEnergy> STREAM_CODEC = StreamCodec
                        .ofMember(DefaultEnergy::encode, DefaultEnergy::new);

        public void encode(RegistryFriendlyByteBuf buf) {
                buf.writeUtf("energy_type");
                buf.writeUtf("parrent_energy_name");
                buf.writeUtf("child_energy_name");
                buf.writeDouble(this.nowPValue);
                buf.writeDouble(this.maxPValue);
                buf.writeDouble(this.nowCValue);
                buf.writeDouble(this.maxCValue);
        }

        public void setEnergyValue(ComponentEnergy.Type type, double energy) {
                if (type == ComponentEnergy.Type.PARRENT) {
                        if (energy >= this.maxPValue) {
                                energy = this.maxPValue;
                        } else if (energy <= 0) {
                                energy = 0;
                        }
                        this.nowPValue = energy;
                } else if (type == ComponentEnergy.Type.CHILD) {
                        if (energy >= this.maxCValue) {
                                energy = this.maxCValue;
                        } else if (energy <= 0) {
                                energy = 0;
                        }
                        this.nowCValue = energy;
                }
        }

        public void setMaxEnergyValue(ComponentEnergy.Type type, double energy) {
                ComponentEnergy tmpEnergy = BasicEnergy.getEnergy(Minecraft.getInstance().player.getUUID(),
                                this.energyType, type.name());
                if (type == ComponentEnergy.Type.PARRENT) {
                        if (energy >= tmpEnergy.getMaxValue()) {
                                energy = tmpEnergy.getMaxValue();
                        } else if (energy <= 0) {
                                energy = 0;
                        }
                        this.maxPValue = energy;
                } else if (type == ComponentEnergy.Type.CHILD) {
                        if (energy >= tmpEnergy.getMaxValue()) {
                                energy = tmpEnergy.getMaxValue();
                        } else if (energy <= 0) {
                                energy = 0;
                        }
                        this.maxCValue = energy;
                }
        }

        public void setEnergyType(String energyType) {
                this.energyType = energyType;
        }

        public String getEnergyType() {
                return this.energyType;
        }

        public double getNowValue(ComponentEnergy.Type type) {
                switch (type) {
                        case PARRENT:
                                return this.nowPValue;
                        case CHILD:
                                return this.nowCValue;
                }
                return 0;
        }

        public double getMaxValue(ComponentEnergy.Type type) {
                switch (type) {
                        case PARRENT:
                                return this.maxPValue;
                        case CHILD:
                                return this.maxCValue;
                }
                return 0;
        }

        public double getMaxPValue() {
                return maxPValue;
        }

        public double getMaxCValue() {
                return maxCValue;
        }

        public void initFromTemplete(String energyTypeKey) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (!player.hasData(RegisterAttachments.DEFAULT_DATA)
                                || !player.hasData(RegisterAttachments.DEFAULT_ENERGY))
                        return;
                DefaultData defaultData = RegisterAttachments.getDefaultData(player);
                ComponentRealm realm = BasicRealm.getRealm(player.getUUID(), defaultData.getRealmType(),
                                defaultData.getRealmLevel());
                if (energyTypeKey.equals(ComponentEnergy.Type.PARRENT.name())) {
                        this.maxPValue = realm.getAddParrentEnergy();
                } else {
                        this.maxCValue = realm.getAddChildEnergy();
                }
        }
}
