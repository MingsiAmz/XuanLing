package com.mingsiamz.xuanlingmod.register;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.entities.SkillEntity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RegisterEntities {
        public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(Registries.ENTITY_TYPE, XuanLing.MODID);

        public static final DeferredHolder<EntityType<?>, EntityType<SkillEntity>> SKILL_ENTITY = ENTITY_TYPE.register("test", () -> EntityType.Builder.<SkillEntity>of(SkillEntity::new, MobCategory.MISC).sized(1f, 1f).noSummon().noSave().fireImmune().canSpawnFarFromPlayer().updateInterval(60).build(XuanLing.prefix("skill_entity").toString()));

        public static void register(IEventBus bus) {
                ENTITY_TYPE.register(bus);
        }

        public static void registerEntityAtb(EntityAttributeCreationEvent event) {
                event.put(SKILL_ENTITY.get(), SkillEntity.createAttribute().build());
        }
}
