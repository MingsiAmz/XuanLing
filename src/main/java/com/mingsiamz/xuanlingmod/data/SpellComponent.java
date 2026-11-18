package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.data.controller.DataManager;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;
import com.mingsiamz.xuanlingmod.type.spell.BasicType;
import com.mingsiamz.xuanlingmod.type.spell.special.Normal;

import net.minecraft.world.entity.LivingEntity;


public class SpellComponent {

        private final XuanLingTypeEnum type = XuanLingTypeEnum.SPELL;
        private String name;
        private ComponentGroup group;

        public SpellComponent(String name, ComponentGroup group) {
                this.name = name;
                this.group = group;
        }

        public void run() {
                if (!check())
                        return;
                this.group.run();
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
