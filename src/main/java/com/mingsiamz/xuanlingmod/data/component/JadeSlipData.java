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
        String creator,
        String system,
        String storedType,
        String storedPayload) {

    public static final String DEFAULT_SYSTEM = "仙修";

    public JadeSlipData {
        if (system == null || system.isEmpty()) {
            system = DEFAULT_SYSTEM;
        }
    }

    public static final JadeSlipData EMPTY = new JadeSlipData(null, "common", "unknown", "", DEFAULT_SYSTEM, null, null);

    public boolean hasRealmData() {
        return realm != null;
    }

    public boolean hasStoredData() {
        return storedType != null && !storedType.isEmpty() && storedPayload != null && !storedPayload.isEmpty();
    }

    public static final Codec<JadeSlipData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    ComponentRealm.CODEC.optionalFieldOf("realm").forGetter(d -> Optional.ofNullable(d.realm())),
                    Codec.STRING.fieldOf("rarity").forGetter(JadeSlipData::rarity),
                    Codec.STRING.fieldOf("source").forGetter(JadeSlipData::source),
                    Codec.STRING.fieldOf("creator").forGetter(JadeSlipData::creator),
                    Codec.STRING.optionalFieldOf("system", DEFAULT_SYSTEM).forGetter(JadeSlipData::system),
                    Codec.STRING.optionalFieldOf("stored_type")
                            .forGetter(d -> Optional.ofNullable(d.storedType)),
                    Codec.STRING.optionalFieldOf("stored_payload")
                            .forGetter(d -> Optional.ofNullable(d.storedPayload)))
                    .apply(instance, (realmOpt, rarity, source, creator, system, storedType, storedPayload) ->
                        new JadeSlipData(realmOpt.orElse(null), rarity, source, creator, system,
                                storedType.orElse(null), storedPayload.orElse(null))));

    public static final StreamCodec<RegistryFriendlyByteBuf, JadeSlipData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public JadeSlipData decode(RegistryFriendlyByteBuf buf) {
            ComponentRealm realm = buf.readBoolean() ? ComponentRealm.STREAM_CODEC.decode(buf) : null;
            String rarity = buf.readUtf(32);
            String source = buf.readUtf(64);
            String creator = buf.readUtf(64);
            String system = buf.readUtf(32);
            boolean hasStored = buf.readBoolean();
            String storedType = null;
            String storedPayload = null;
            if (hasStored) {
                storedType = buf.readUtf(64);
                storedPayload = buf.readUtf(262144);
            }
            return new JadeSlipData(realm, rarity, source, creator, system, storedType, storedPayload);
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
            buf.writeUtf(value.system, 32);
            boolean hasStored = value.storedType != null && !value.storedType.isEmpty();
            buf.writeBoolean(hasStored);
            if (hasStored) {
                buf.writeUtf(value.storedType, 64);
                buf.writeUtf(value.storedPayload, 262144);
            }
        }
    };

    public static final net.minecraft.core.component.DataComponentType<JadeSlipData> TYPE = net.minecraft.core.component.DataComponentType
                    .<JadeSlipData>builder()
                    .persistent(JadeSlipData.CODEC)
                    .networkSynchronized(JadeSlipData.STREAM_CODEC)
                    .build();

    public JadeSlipData withRealm(ComponentRealm realm) {
        return new JadeSlipData(realm, rarity, source, creator, system, storedType, storedPayload);
    }

    public JadeSlipData withRarity(Rarity rarity) {
        return new JadeSlipData(realm, rarity.getId(), source, creator, system, storedType, storedPayload);
    }

    public JadeSlipData withSource(String source) {
        return new JadeSlipData(realm, rarity, source, creator, system, storedType, storedPayload);
    }

    public JadeSlipData withCreator(String creator) {
        return new JadeSlipData(realm, rarity, source, creator, system, storedType, storedPayload);
    }

    public JadeSlipData withSystem(String system) {
        return new JadeSlipData(realm, rarity, source, creator, system, storedType, storedPayload);
    }

    public JadeSlipData withStored(String storedType, String storedPayload) {
        return new JadeSlipData(realm, rarity, source, creator, system, storedType, storedPayload);
    }

    public Rarity getRarityEnum() {
        try {
            return Rarity.valueOf(rarity.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Rarity.COMMON;
        }
    }
}
