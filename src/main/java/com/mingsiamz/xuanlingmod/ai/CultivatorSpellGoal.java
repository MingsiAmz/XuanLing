package com.mingsiamz.xuanlingmod.ai;

import java.util.EnumSet;

import com.mingsiamz.xuanlingmod.entities.AoeSkillEntity;
import com.mingsiamz.xuanlingmod.entities.ProjectileSkillEntity;
import com.mingsiamz.xuanlingmod.event.AutoGenerationEvents;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class CultivatorSpellGoal extends Goal {

    private final Mob mob;
    private int spellCooldown;
    private int artCooldown;

    public CultivatorSpellGoal(Mob mob) {
        this.mob = mob;
        this.spellCooldown = 30;
        this.artCooldown = 60;
        setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        CompoundTag data = mob.getPersistentData().getCompound(AutoGenerationEvents.NPC_DATA);
        return data.getBoolean(AutoGenerationEvents.CULTIVATOR)
                && (!data.getString(AutoGenerationEvents.SPELL_NAME).isEmpty()
                        || !data.getString(AutoGenerationEvents.ART_NAME).isEmpty());
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        if (mob.level().isClientSide) {
            return;
        }
        if (spellCooldown > 0) {
            spellCooldown--;
        }
        if (artCooldown > 0) {
            artCooldown--;
        }

        CompoundTag data = mob.getPersistentData().getCompound(AutoGenerationEvents.NPC_DATA);
        LivingEntity target = validTarget(mob.getTarget()) ? mob.getTarget() : mob.getLastHurtByMob();
        if (!validTarget(target)) {
            target = null;
        }
        if (target != null) {
            mob.getLookControl().setLookAt(target, 30.0f, 30.0f);
        }

        if (artCooldown <= 0 && !data.getString(AutoGenerationEvents.ART_NAME).isEmpty()) {
            castArt(data, target);
            artCooldown = 200;
        }

        if (spellCooldown <= 0 && !data.getString(AutoGenerationEvents.SPELL_NAME).isEmpty()) {
            if (castSpell(data, target)) {
                spellCooldown = Math.max(40, data.getInt(AutoGenerationEvents.SPELL_COOLDOWN));
            }
        }
    }

    private void castArt(CompoundTag data, LivingEntity target) {
        if (target == null && mob.getHealth() >= mob.getMaxHealth()) {
            return;
        }
        int realmLevel = data.getInt(AutoGenerationEvents.REALM_LEVEL);
        float artPower = data.getFloat(AutoGenerationEvents.ART_POWER);
        mob.heal(2.0f + realmLevel + artPower);
        mob.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, Math.min(2, realmLevel / 4)));
        if (target != null) {
            mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100,
                    Math.min(2, realmLevel / 5)));
        }
    }

    private boolean castSpell(CompoundTag data, LivingEntity target) {
        String category = data.getString(AutoGenerationEvents.SPELL_CATEGORY);
        String name = data.getString(AutoGenerationEvents.SPELL_NAME);
        float damage = data.getFloat(AutoGenerationEvents.SPELL_DAMAGE)
                + data.getInt(AutoGenerationEvents.REALM_LEVEL);
        float heal = data.getFloat(AutoGenerationEvents.SPELL_HEAL);

        if ("SELF".equals(category)) {
            if (heal > 0 && mob.getHealth() < mob.getMaxHealth()) {
                mob.heal(heal);
                return true;
            }
            if (target != null && damage > 0) {
                mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0));
                return true;
            }
            return false;
        }
        if (!validTarget(target) || mob.distanceToSqr(target) > 24 * 24) {
            return false;
        }
        if ("AOE".equals(category)) {
            AoeSkillEntity spell = AoeSkillEntity.spawn(mob.level(), target.position(), mob,
                    XuanLingTypeEnum.SPELL, name, 2.5f);
            spell.setDamage(Math.max(1.0f, damage));
            return true;
        }

        Vec3 start = new Vec3(mob.getX(), mob.getEyeY(), mob.getZ());
        Vec3 end = new Vec3(target.getX(), target.getEyeY(), target.getZ());
        ProjectileSkillEntity spell = ProjectileSkillEntity.spawn(mob.level(), mob,
                end.subtract(start).normalize(), XuanLingTypeEnum.SPELL, name);
        spell.setDamage(Math.max(1.0f, damage));
        spell.setRadius(1.5f);
        return true;
    }

    private boolean validTarget(LivingEntity target) {
        return target != null && target.isAlive() && target != mob;
    }
}
