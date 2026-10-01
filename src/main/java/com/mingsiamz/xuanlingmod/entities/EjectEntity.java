package com.mingsiamz.xuanlingmod.entities;

import java.util.Iterator;

import org.joml.Vector3f;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.register.RegisterEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.Tags.Fluids;

public class EjectEntity extends SkillEntity {

        private static final EntityDataAccessor<Vector3f> DATA_VEC3 = SynchedEntityData.defineId(SkillEntity.class,
                                                                                                 EntityDataSerializers.VECTOR3);
        private double speed = 1.0;

        public EjectEntity(Level level, LivingEntity owner, float damage) {
                super(RegisterEntities.EJECT_ENTITY.get(), level);
                setOwnerUUID(owner);
                setPos(owner.getX(), owner.getEyeY(), owner.getZ());
                setDamage(damage);
                setDir(getOwner().getLookAngle().normalize());
        }

        public EjectEntity(EntityType<? extends SkillEntity> entityType, Level level) {
                super(entityType, level);
        }

        @Override
        protected void defineSynchedData(Builder builder) {
                super.defineSynchedData(builder);
                builder.define(DATA_VEC3, new Vector3f());
        }

        public EjectEntity setDir(Vec3 vec) {
                this.entityData.set(DATA_VEC3, new Vector3f((float) vec.x, (float) vec.y, (float) vec.z));
                return this;
        }

        public Vec3 getDir() {
                return new Vec3(this.entityData.get(DATA_VEC3));
        }

        @Override
        public void tick() {
                setDeltaMovement(getDir().scale(speed));
                this.move(MoverType.SELF, getDeltaMovement());


                if (!level().isClientSide) {
                        AABB bound = this.getBoundingBox();
                        for (Entity e : level().getEntities(this, bound)) {
                                if (getOwner() != null && e instanceof LivingEntity living) {
                                        if (living == getOwner() || living == this)
                                                continue;
                                        DamageSource source = new DamageSource(
                                                                               level().registryAccess().holderOrThrow(DamageTypes.GENERIC),
                                                                               this,
                                                                               this.getOwner());
                                        living.hurt(source, getDamage());
                                }
                        }

                        if (this.horizontalCollision || this.verticalCollision) {
                                level().explode(this, this.getX(), this.getY(), this.getZ(), 8, ExplosionInteraction.TNT);
                                AABB boudBox = this.getBoundingBox().expandTowards(4, 4, 4);
                                Iterator<BlockPos> it = BlockPos.betweenClosed(
                                                                               (int)boudBox.minX, 
                                                                               (int)boudBox.minY, 
                                                                               (int)boudBox.minZ, 
                                                                               (int)boudBox.maxX, 
                                                                               (int)boudBox.maxY, 
                                                                               (int)boudBox.maxZ)
                                        .iterator();
                                while (it.hasNext()) {
                                        BlockState block = level().getBlockState(it.next());
                                        FluidState fluid = level().getFluidState(it.next());
                                        if (block.is(Blocks.WATER) || fluid.is(Tags.Fluids.WATER) || fluid.is(Tags.Fluids.LAVA)) {
                                                level().removeBlock(it.next(), true);
                                        }
                                }
                        }


                }
                
                super.tick();
                
        }
}
