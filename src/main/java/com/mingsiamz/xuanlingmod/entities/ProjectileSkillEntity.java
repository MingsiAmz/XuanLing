package com.mingsiamz.xuanlingmod.entities;

import org.joml.Vector3f;

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
            AABB hitBox = this.getBoundingBox().inflate(1.0, 1.0, 1.0);
            for (LivingEntity target : level().getEntitiesOfClass(LivingEntity.class, hitBox)) {
                if (target == getCaster() || target == this) {
                    continue;
                }
                trigger(level(), this.position());
                return;
            }
            if (this.horizontalCollision || this.verticalCollision || tickAlive >= (int) (getLifeDuretion() * 20)) {
                trigger(level(), this.position());
                return;
            }
        }

        super.tick();
    }

    @Override
    protected void applyComponentParams(ImpComponent comp) {
    }

    public static AttributeSupplier.Builder createAttribute() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0);
    }
}
