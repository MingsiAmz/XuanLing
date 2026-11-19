package com.mingsiamz.xuanlingmod.ai;

import java.util.EnumSet;

import com.mingsiamz.xuanlingmod.entities.AoeSkillEntity;
import com.mingsiamz.xuanlingmod.entities.ProjectileSkillEntity;
import com.mingsiamz.xuanlingmod.event.AutoGenerationEvents;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
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

        int realmLevel = data.getInt(AutoGenerationEvents.REALM_LEVEL);
        // 仙凡有别：境界越高，施法冷却越短
        int artCd = Math.max(60, 200 - realmLevel * 30);
        int spellCd = Math.max(20, data.getInt(AutoGenerationEvents.SPELL_COOLDOWN) - realmLevel * 5);

        if (artCooldown <= 0 && !data.getString(AutoGenerationEvents.ART_NAME).isEmpty()) {
            castArt(data, target);
            artCooldown = artCd;
        }

        if (spellCooldown <= 0 && !data.getString(AutoGenerationEvents.SPELL_NAME).isEmpty()) {
            if (castSpell(data, target)) {
                spellCooldown = spellCd;
            }
        }
    }

    private void castArt(CompoundTag data, LivingEntity target) {
        if (target == null && mob.getHealth() >= mob.getMaxHealth()) {
            return;
        }
        int realmLevel = data.getInt(AutoGenerationEvents.REALM_LEVEL);
        float artPower = data.getFloat(AutoGenerationEvents.ART_POWER);
        // 仙凡有别：高境界修士的功法治疗与持续恢复远强于凡人
        float heal = (2.0f + realmLevel + artPower) * (1.0f + realmLevel * 0.6f);
        mob.heal(heal);
        mob.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, Math.min(4, realmLevel / 2)));
        if (target != null) {
            mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100,
                    Math.min(4, realmLevel / 3)));
        }
    }

    private boolean castSpell(CompoundTag data, LivingEntity target) {
        String category = data.getString(AutoGenerationEvents.SPELL_CATEGORY);
        String name = data.getString(AutoGenerationEvents.SPELL_NAME);
        int realmLevel = data.getInt(AutoGenerationEvents.REALM_LEVEL);
        // 仙凡有别：高境界修士的神通伤害远高于低境界
        float damage = (data.getFloat(AutoGenerationEvents.SPELL_DAMAGE) + realmLevel)
                * (1.0f + realmLevel * 0.8f);
        float heal = data.getFloat(AutoGenerationEvents.SPELL_HEAL)
                * (1.0f + realmLevel * 0.6f);

        // 施法者能量倍率平滑加成（0=0，50%=1×，100%=2×）；NPC 无 BasicData 时视为中性 0.5
        float multiplier = 1f;
        var basicData = RegisterAttachments.getBasicData(mob);
        if (basicData != null) {
            multiplier = BasicData.smoothMultiplier(basicData.getEnergyMultiplier());
        }
        damage *= multiplier;
        heal *= multiplier;

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
