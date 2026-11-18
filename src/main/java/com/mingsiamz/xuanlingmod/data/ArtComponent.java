package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.data.controller.DataManager;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.world.entity.LivingEntity;


public class ArtComponent {

        private final XuanLingTypeEnum type = XuanLingTypeEnum.ART;
        private String name;
        private ComponentGroup group;

        public ArtComponent(String name, ComponentGroup group) {
                this.name = name;
                this.group = group;
        }

        public void run() {
                if (!check())
                        return;
                this.group.run();
        }

        
        public boolean check() {
                return this.group != null && !this.group.isEmpty();
        }

        
        public boolean toJson(LivingEntity entity) {
                if (!check())
                        return false;
                DataManager.saveComponent(this.type, entity.getStringUUID(), this.name, this.group);
                return true;
        }
}
