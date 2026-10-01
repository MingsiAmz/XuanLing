package com.mingsiamz.xuanlingmod.entities;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.joml.Vector3f;

import com.mingsiamz.xuanlingmod.register.RegisterEntities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SkillEntity extends LivingEntity {

        private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID =
                SynchedEntityData.defineId(SkillEntity.class, EntityDataSerializers.OPTIONAL_UUID);

        protected String damageKey = "spawn_skill_damage";
        protected float lifeDuretion = 5f;
        protected boolean isGravity = false;
        protected CompoundTag nbt = this.getPersistentData();

        public SkillEntity(Level level, LivingEntity owner, float damage) {
                super(RegisterEntities.SKILL_ENTITY.get(), level);
                this.noPhysics = true;
                setOwnerUUID(owner);
                setPos(owner.position());
                damage = damage <= 5 ? 5 : damage;
                setDamage(damage);
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

        @Override
        public boolean isInvisible() {
                return false;
        }

        @Override
        public boolean isOnFire() {
                return false;
        }

        @Override
        public boolean isInvulnerable() {
                return true;
        }

        @Override
        protected void defineSynchedData(Builder builder) {
                super.defineSynchedData(builder);
                builder.define(DATA_OWNER_UUID, Optional.empty());
        }

        public SkillEntity setDamage(float value) {
                this.nbt.putFloat(this.damageKey, value);
                return this;
        }
        
        public float getDamage() {
                if (!this.nbt.contains(this.damageKey))
                        return 0;
                return this.nbt.getFloat(this.damageKey);
        }

        public LivingEntity getOwner() {
                return getOwnerUUID().map(uuid -> {
                                Player player = level().getPlayerByUUID(uuid);
                                if (player != null) return player;
                                for (Entity entity : level().getEntities(this, this.getBoundingBox().expandTowards(64, 64, 64))) {
                                        if (entity.getUUID().equals(uuid) && entity instanceof LivingEntity living) {
                                                return living;
                                        }
                                }
                                return null;
                        }).orElse(null);
        }
        
        public SkillEntity setOwnerUUID(LivingEntity owner) {
                this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(owner.getUUID()));
                return this;
        }

        public Optional<UUID> getOwnerUUID() {
                return this.entityData.get(DATA_OWNER_UUID);
        }

        public SkillEntity setLifeDuretion(float lifeDuretion) {
                this.lifeDuretion = lifeDuretion;
                return this;
        }

        public float getLifeDuretion() {
                return lifeDuretion;
        }

        public static AttributeSupplier.Builder createAttribute() {
                return Mob.createMobAttributes()
                        .add(Attributes.MAX_HEALTH);
        }
}
