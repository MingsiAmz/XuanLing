package com.mingsiamz.xuanlingmod.entities;

import java.util.Collections;

import com.mingsiamz.xuanlingmod.register.RegisterEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SkillEntity extends LivingEntity {

        private LivingEntity owner;
        private String damageKey = "spawn_skill_damage";
        private float lifeDuretion = 5f;
        private boolean isGravity = false;
        private CompoundTag nbt = this.getPersistentData();

        public SkillEntity(Level level, LivingEntity owner, float damage) {
                super(RegisterEntities.SKILL_ENTITY.get(), level);
                this.owner = owner;
                damage = damage <= 5 ? 5 : damage;
                setPos(this.owner.position());
                this.nbt.putFloat("my_damage", damage);
        }

        public SkillEntity(EntityType<? extends SkillEntity> entityType, Level level) {
                super(entityType, level);
        }

        @Override
        public Iterable<ItemStack> getArmorSlots() {
                return Collections.singleton(ItemStack.EMPTY);
        }

        @Override
        public ItemStack getItemBySlot(EquipmentSlot slot) {
                return ItemStack.EMPTY;
        }

        @Override
        public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        }

        @Override
        public HumanoidArm getMainArm() {
                return HumanoidArm.LEFT;
        }

        @Override
        public boolean isPushable() {
                return false;
        }

        @Override
        public boolean canBeCollidedWith() {
                return false;
        }

        @Override
        public boolean isNoGravity() {
                return !this.isGravity;
        }

        public void setDamage(float value) {
                this.nbt.putFloat(this.damageKey, value);
        }
        
        public float getDamage() {
                if (!this.nbt.contains(this.damageKey))
                        return 0;
                return this.nbt.getFloat(this.damageKey);
        }

        public static AttributeSupplier.Builder createAttribute() {
                return Mob.createMobAttributes()
                        .add(Attributes.MAX_HEALTH);
        }
}
