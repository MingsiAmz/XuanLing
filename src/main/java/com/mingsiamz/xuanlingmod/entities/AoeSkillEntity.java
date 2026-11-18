package com.mingsiamz.xuanlingmod.entities;

import com.mingsiamz.xuanlingmod.register.RegisterEntities;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AoeSkillEntity extends AbstractSpellEntity {

    private int tickAlive;
    private boolean triggered;

    public AoeSkillEntity(EntityType<? extends AbstractSpellEntity> entityType, Level level) {
        super(entityType, level);
    }

    public AoeSkillEntity(Level level, LivingEntity caster, XuanLingTypeEnum sourceType, String sourceName,
                          float radius) {
        super(RegisterEntities.AOE_SPELL_ENTITY.get(), level, caster, sourceType, sourceName);
        setPos(caster.getX(), caster.getEyeY(), caster.getZ());
        setRadius(radius);
        setLifeDuretion(3.0f);
        setRemoveOnTrigger(false);
    }

    @Override
    public void tick() {
        if (!level().isClientSide) {
            tickAlive++;
            if (!triggered) {
                triggered = true;
                trigger(level(), this.position());
            }
            if (tickAlive >= (int) (getLifeDuretion() * 20)) {
                this.remove(RemovalReason.KILLED);
            }
        }

        super.tick();
    }

    public static AoeSkillEntity spawn(Level level, LivingEntity caster, XuanLingTypeEnum sourceType,
                                       String sourceName, float radius) {
        AoeSkillEntity entity = new AoeSkillEntity(level, caster, sourceType, sourceName, radius);
        level.addFreshEntity(entity);
        return entity;
    }

    public static AoeSkillEntity spawn(Level level, Vec3 pos, LivingEntity caster, XuanLingTypeEnum sourceType,
                                       String sourceName, float radius) {
        AoeSkillEntity entity = new AoeSkillEntity(level, caster, sourceType, sourceName, radius);
        entity.setPos(pos.x, pos.y, pos.z);
        level.addFreshEntity(entity);
        return entity;
    }

    public static AttributeSupplier.Builder createAttribute() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0);
    }
}
