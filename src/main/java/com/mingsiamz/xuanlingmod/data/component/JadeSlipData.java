package com.mingsiamz.xuanlingmod.data.component;

import com.mingsiamz.xuanlingmod.auto.config.Rarity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;


public record JadeSlipData(
        ComponentRealm realm,
        String rarity,
        String source,
        String creator) {

    public static final JadeSlipData EMPTY = new JadeSlipData(null, "common", "unknown", "");

    public boolean hasRealmData() {
        return realm != null;
    }

    public static final Codec<JadeSlipData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ComponentRealm.CODEC.optionalFieldOf("realm").forGetter(d -> Optional.ofNullable(d.realm())),
                    Codec.STRING.fieldOf("rarity").forGetter(JadeSlipData::rarity),
                    Codec.STRING.fieldOf("source").forGetter(JadeSlipData::source),
                    Codec.STRING.fieldOf("creator").forGetter(JadeSlipData::creator))
                    .apply(instance, (realmOpt, rarity, source, creator) -> 
                        new JadeSlipData(realmOpt.orElse(null), rarity, source, creator)));

    public static final StreamCodec<RegistryFriendlyByteBuf, JadeSlipData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public JadeSlipData decode(RegistryFriendlyByteBuf buf) {
            ComponentRealm realm = buf.readBoolean() ? ComponentRealm.STREAM_CODEC.decode(buf) : null;
            String rarity = buf.readUtf(32);
            String source = buf.readUtf(64);
            String creator = buf.readUtf(64);
            return new JadeSlipData(realm, rarity, source, creator);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, JadeSlipData value) {
            buf.writeBoolean(value.realm != null);
            if (value.realm != null) {
                ComponentRealm.STREAM_CODEC.encode(buf, value.realm);
            }
            buf.writeUtf(value.rarity, 32);
            buf.writeUtf(value.source, 64);
            buf.writeUtf(value.creator, 64);
        }
    };

    public static final net.minecraft.core.component.DataComponentType<JadeSlipData> TYPE = net.minecraft.core.component.DataComponentType
                    .<JadeSlipData>builder()
                    .persistent(JadeSlipData.CODEC)
                    .networkSynchronized(JadeSlipData.STREAM_CODEC)
                    .build();

    public JadeSlipData withRealm(ComponentRealm realm) {
        return new JadeSlipData(realm, rarity, source, creator);
    }

    public JadeSlipData withRarity(Rarity rarity) {
        return new JadeSlipData(realm, rarity.getId(), source, creator);
    }

    public JadeSlipData withSource(String source) {
        return new JadeSlipData(realm, rarity, source, creator);
    }

    public JadeSlipData withCreator(String creator) {
        return new JadeSlipData(realm, rarity, source, creator);
    }

    public Rarity getRarityEnum() {
        try {
            return Rarity.valueOf(rarity.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Rarity.COMMON;
        }
    }
}