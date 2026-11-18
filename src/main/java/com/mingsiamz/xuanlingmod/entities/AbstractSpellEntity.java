package com.mingsiamz.xuanlingmod.entities;

import java.util.Optional;
import java.util.UUID;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.ComponentGroup;
import com.mingsiamz.xuanlingmod.data.ComponentCodec;
import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class AbstractSpellEntity extends SkillEntity {

        private static final EntityDataAccessor<Optional<UUID>> DATA_CASTER_UUID = SynchedEntityData
                        .defineId(AbstractSpellEntity.class, EntityDataSerializers.OPTIONAL_UUID);

        protected XuanLingTypeEnum sourceType;
        protected String sourceName;
        protected float radius = 4.0f;
        protected float damage = 6.0f;
        protected boolean removeOnTrigger = true;

        public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level level) {
                super(entityType, level);
                this.noPhysics = true;
                setInvulnerable(true);
        }

        public AbstractSpellEntity(EntityType<? extends AbstractSpellEntity> entityType, Level level,
                        LivingEntity caster, XuanLingTypeEnum sourceType, String sourceName) {
                this(entityType, level);
                setOwnerUUID(caster);
                setPos(caster.getX(), caster.getEyeY(), caster.getZ());
                this.sourceType = sourceType;
                this.sourceName = sourceName;
        }

        @Override
        protected void defineSynchedData(SynchedEntityData.Builder builder) {
                super.defineSynchedData(builder);
                builder.define(DATA_CASTER_UUID, Optional.empty());
        }

        public void setCasterUUID(LivingEntity caster) {
                this.entityData.set(DATA_CASTER_UUID, Optional.ofNullable(caster == null ? null : caster.getUUID()));
        }

        public Optional<UUID> getCasterUUID() {
                return this.entityData.get(DATA_CASTER_UUID);
        }

        public LivingEntity getCaster() {
                return getCasterUUID().map(uuid -> {
                        LivingEntity caster = level().getPlayerByUUID(uuid);
                        if (caster != null)
                                return caster;
                        for (Entity e : level().getEntities(this, this.getBoundingBox().expandTowards(64, 64, 64))) {
                                if (e.getUUID().equals(uuid) && e instanceof LivingEntity living) {
                                        return living;
                                }
                        }
                        return null;
                }).orElse(null);
        }

        public AbstractSpellEntity setSource(XuanLingTypeEnum type, String name, LivingEntity caster) {
                this.sourceType = type;
                this.sourceName = name;
                setOwnerUUID(caster);
                loadParamsFromData();
                return this;
        }

        public AbstractSpellEntity setRadius(float radius) {
                this.radius = Math.max(0.5f, radius);
                return this;
        }

        public AbstractSpellEntity setDamage(float damage) {
                this.damage = damage;
                return this;
        }

        public AbstractSpellEntity setRemoveOnTrigger(boolean removeOnTrigger) {
                this.removeOnTrigger = removeOnTrigger;
                return this;
        }

        protected void loadParamsFromData() {
                if (sourceType == null || sourceName == null || sourceName.isEmpty()) {
                        return;
                }
                LivingEntity caster = getCaster();
                String uuid = caster == null ? java.util.UUID.randomUUID().toString() : caster.getStringUUID();
                ComponentGroup group = ComponentCodec.loadGroup(sourceType, uuid, sourceName);
                if (group == null || group.isEmpty()) {
                        return;
                }
                group.getMaps().forEach((typeKey, subTypeMap) -> subTypeMap.forEach((subTypeKey, components) -> {
                        for (ImpComponent comp : components) {
                                applyComponentParams(comp);
                        }
                }));
        }

        protected void applyComponentParams(ImpComponent comp) {
                Object dmg = comp.getParams().get(com.mingsiamz.xuanlingmod.type.spell.ParamType.DAMAGE);
                if (dmg instanceof Number n) {
                        this.damage = n.floatValue();
                }
                Object spd = comp.getParams().get(com.mingsiamz.xuanlingmod.type.spell.ParamType.SPEED);
                if (spd instanceof Number n) {
                        this.radius = Math.max(0.5f, n.floatValue());
                }
        }

        protected void applyEffectsAt(Level level, Vec3 pos) {
                if (level.isClientSide) {
                        return;
                }
                LivingEntity caster = getCaster();
                AABB area = new AABB(pos.x - radius, pos.y - radius, pos.z - radius,
                                pos.x + radius, pos.y + radius, pos.z + radius);
                for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area)) {
                        if (target == this || target == caster) {
                                continue;
                        }
                        applyEffectToTarget(level, target, caster, pos);
                }
        }

        protected void applyExplosion(Level level, Vec3 pos, float explosionRadius) {
                if (level.isClientSide || explosionRadius <= 0) {
                        return;
                }
                level.explode(null, pos.x, pos.y, pos.z, explosionRadius, ExplosionInteraction.NONE);
        }

        protected void applyEffectToTarget(Level level, LivingEntity target, LivingEntity caster, Vec3 pos) {
                target.hurt(level.damageSources().generic(), damage);
        }

        public void trigger(Level level, Vec3 pos) {
                applyEffectsAt(level, pos);
                if (removeOnTrigger && !level.isClientSide) {
                        this.remove(RemovalReason.KILLED);
                }
        }

        public XuanLingTypeEnum getSourceType() {
                return sourceType;
        }

        public String getSourceName() {
                return sourceName;
        }
}
