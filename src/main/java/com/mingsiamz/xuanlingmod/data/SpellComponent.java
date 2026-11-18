package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy;
import com.mingsiamz.xuanlingmod.data.controller.DataManager;
import com.mingsiamz.xuanlingmod.entities.AoeSkillEntity;
import com.mingsiamz.xuanlingmod.entities.ProjectileSkillEntity;
import com.mingsiamz.xuanlingmod.network.SkillFxPacket;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;
import com.mingsiamz.xuanlingmod.type.spell.ParamType;
import com.mingsiamz.xuanlingmod.type.spell.special.Normal;

import net.minecraft.world.entity.LivingEntity;


public class SpellComponent {

        private final XuanLingTypeEnum type = XuanLingTypeEnum.SPELL;
        private String name;
        private ComponentGroup group;
        private static final String COOLDOWN_KEY = "xuanling_spell_cooldowns";

        public SpellComponent(String name, ComponentGroup group) {
                this.name = name;
                this.group = group;
        }

        public void run() {
                if (!check())
                        return;
                this.group.run();
        }

        public boolean cast(LivingEntity caster) {
                if (caster == null || caster.level().isClientSide || !check())
                        return false;
                long now = caster.level().getGameTime();
                if (caster.getPersistentData().getCompound(COOLDOWN_KEY).getLong(this.name) > now) {
                        return false;
                }
                for (ImpComponent component : this.group.getComponents(this.type, BasicType.GROUP)) {
                        if (BasicType.PROJECTLE.name.equals(component.name())) {
                                ProjectileSkillEntity.spawn(caster.level(), caster, caster.getLookAngle(),
                                                this.type, this.name);
                                return completeCast(caster, now);
                        }
                        if (BasicType.AOE.name.equals(component.name())) {
                                AoeSkillEntity.spawn(caster.level(), caster, this.type, this.name, 4.0f);
                                return completeCast(caster, now);
                        }
                        if (BasicType.SELF.name.equals(component.name())) {
                                castOnSelf(caster);
                                SkillFxPacket.broadcast(caster);
                                return completeCast(caster, now);
                        }
                }
                return false;
        }

        private boolean completeCast(LivingEntity caster, long now) {
                int cooldown = cooldownTicks();
                if (cooldown > 0) {
                        var cooldowns = caster.getPersistentData().getCompound(COOLDOWN_KEY);
                        cooldowns.putLong(this.name, now + cooldown);
                        caster.getPersistentData().put(COOLDOWN_KEY, cooldowns);
                }
                return true;
        }

        private int cooldownTicks() {
                int result = 0;
                for (var components : this.group.getMaps().values()) {
                        for (var list : components.values()) {
                                for (ImpComponent component : list) {
                                        Object value = component.getParams().get(ParamType.COOLDOWN);
                                        if (value instanceof Number number) {
                                                result = Math.max(result, Math.round(number.floatValue() * 20));
                                        }
                                }
                        }
                }
                return result;
        }

        private void castOnSelf(LivingEntity caster) {
                var actions = this.group.getComponents(this.type, ActionType.GROUP);
                if (actions == null) {
                        return;
                }
                for (ImpComponent component : actions) {
                        float amount = actionAmount(component);
                        if (amount <= 0) {
                                continue;
                        }
                        if (ActionType.DAMAGE.name.equals(component.name())) {
                                caster.hurt(caster.damageSources().generic(), amount);
                        } else if (ActionType.RECOVER_HEALTH.name.equals(component.name())) {
                                caster.heal(amount);
                        } else if (ActionType.RECOVER_P_ENERGY.name.equals(component.name())) {
                                recoverEnergy(caster, ComponentEnergy.Type.PARRENT, amount);
                        } else if (ActionType.RECOVER_C_ENERGY.name.equals(component.name())) {
                                recoverEnergy(caster, ComponentEnergy.Type.CHILD, amount);
                        }
                }
        }

        private float actionAmount(ImpComponent component) {
                Object value = component.getParams().get(ParamType.NORMAL_VALUE);
                if (!(value instanceof Number)) {
                        value = component.getParams().get(ParamType.DAMAGE);
                }
                return value instanceof Number number ? Math.max(0, number.floatValue()) : 0;
        }

        private void recoverEnergy(LivingEntity caster, ComponentEnergy.Type energyType, float amount) {
                DefaultEnergy energy = RegisterAttachments.getDefaultEnergy(caster);
                if (energy == null) {
                        return;
                }
                energy.setEnergyValue(energyType, energy.getNowValue(energyType) + amount);
                caster.setData(RegisterAttachments.DEFAULT_ENERGY, energy);
        }

        
        public boolean check() {
                if (this.group == null || this.group.isEmpty())
                        return false;
                if (this.group.getComponents(this.type, BasicType.GROUP) == null)
                        return false;
                if (this.group.getComponents(this.type, ActionType.GROUP) == null)
                        this.group.addComponent(this.type, ActionType.GROUP, new Normal());
                return true;
        }

        
        public boolean toJson(LivingEntity entity) {
                if (!check())
                        return false;
                DataManager.saveComponent(this.type, entity.getStringUUID(), this.name, this.group);
                return true;
        }

        
        public static SpellComponent load(String entityUuid, String name) {
                ComponentGroup group = ComponentCodec.loadGroup(XuanLingTypeEnum.SPELL, entityUuid, name);
                if (group == null || group.isEmpty())
                        return null;
                return new SpellComponent(name, group);
        }
}
