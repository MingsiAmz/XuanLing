package com.mingsiamz.xuanlingmod.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.joml.Vector3f;

import com.mingsiamz.xuanlingmod.network.SkillFxPacket;
import com.mingsiamz.xuanlingmod.register.RegisterEntities;
import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ProjectileSkillEntity extends AbstractSpellEntity {

    private static final EntityDataAccessor<Vector3f> DATA_DIR =
            SynchedEntityData.defineId(ProjectileSkillEntity.class, EntityDataSerializers.VECTOR3);

    private double speed = 1.5;
    private int tickAlive;
    private int hitCount;
    private final Set<UUID> hitTargets = new HashSet<>();

    public ProjectileSkillEntity(EntityType<? extends AbstractSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ProjectileSkillEntity(Level level, LivingEntity caster, Vec3 direction, XuanLingTypeEnum sourceType,
                                 String sourceName) {
        super(RegisterEntities.PROJECTILE_SPELL_ENTITY.get(), level, caster, sourceType, sourceName);
        setPos(caster.getX(), caster.getEyeY(), caster.getZ());
        setDir(direction.normalize());
        setDeltaMovement(getDir().scale(speed));
        setLifeDuretion(6.0f);
    }

    public ProjectileSkillEntity setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public ProjectileSkillEntity setDir(Vec3 vec) {
        this.entityData.set(DATA_DIR, new Vector3f((float) vec.x, (float) vec.y, (float) vec.z));
        return this;
    }

    public Vec3 getDir() {
        return new Vec3(this.entityData.get(DATA_DIR));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DIR, new Vector3f());
    }

    @Override
    public void tick() {
        this.move(MoverType.SELF, getDeltaMovement());

        if (!level().isClientSide) {
            tickAlive++;
            if (tickAlive % 3 == 0) {
                SkillFxPacket.broadcast(this, true);
            }
            AABB hitBox = this.getBoundingBox().inflate(1.0, 1.0, 1.0);
            for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, hitBox)) {
                if (isOwner(target) || target == this || hitTargets.contains(target.getUUID())) {
                    continue;
                }
                hitTargets.add(target.getUUID());
                hitCount++;
                trigger(level(), this.position());
                if (maxTargets > 0 && hitCount >= maxTargets) {
                    this.remove(RemovalReason.KILLED);
                }
                return;
            }
            if (this.horizontalCollision || this.verticalCollision || tickAlive >= (int) (getLifeDuretion() * 20)) {
                trigger(level(), this.position());
                this.remove(RemovalReason.KILLED);
                return;
            }
        }

        super.tick();
    }

    @Override
    protected void applyComponentParams(ImpComponent comp) {
        super.applyComponentParams(comp);
        if (maxTargets > 1) {
            this.removeOnTrigger = false;
        }
        Object speed = comp.getParams().get(com.mingsiamz.xuanlingmod.type.spell.ParamType.SPEED);
        if (!(speed instanceof Number number) || number.doubleValue() <= 0) {
            speed = comp.getParams().get(com.mingsiamz.xuanlingmod.type.spell.ParamType.INIT_SPEED);
        }
        if (speed instanceof Number number && number.doubleValue() > 0) {
            this.speed = number.doubleValue();
            setDeltaMovement(getDir().scale(this.speed));
        }
        Object lifetime = comp.getParams().get(com.mingsiamz.xuanlingmod.type.spell.ParamType.LIFETIME);
        if (lifetime instanceof Number number && number.floatValue() > 0) {
            setLifeDuretion(number.floatValue());
        }
    }

    public static ProjectileSkillEntity spawn(Level level, LivingEntity caster, Vec3 direction,
                                               XuanLingTypeEnum sourceType, String sourceName) {
        ProjectileSkillEntity entity = new ProjectileSkillEntity(level, caster, direction, sourceType, sourceName);
        entity.setSource(sourceType, sourceName, caster);
        level.addFreshEntity(entity);
        SkillFxPacket.broadcast(entity);
        return entity;
    }

    public static AttributeSupplier.Builder createAttribute() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0);
    }
}
