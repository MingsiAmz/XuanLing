package com.mingsiamz.xuanlingmod.data;

import com.mingsiamz.xuanlingmod.data.controller.DataManager;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.type.spell.ActionType;
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
                if (this.group.isEmpty())
                        return;
                this.group.run();
        }

        private void check() {
                if (this.group.isEmpty())
                        return;
                if (this.group.getComponents(this.type, ActionType.GROUP).isEmpty())
                        this.group.addComponent(this.type, ActionType.GROUP, new Normal());
        }

        public void toJson(LivingEntity entity) {
                DataManager.saveData(this, this.type, entity.getStringUUID(), this.name);
        }
}

        // private SpellComponent(Builder builder) {
        //         name = builder.name;
        //         group = builder.group;
        // }
        

//         public XuanLingTypeEnum getType() {
//                 return type;
//         }

//         public ComponentGroup getGroup() {
//                 return this.group;
//         }

//         public static Builder build(String name) {
//                 Builder builder = new Builder(name);
//                 return builder;
//         }

//         public static void run(SpellComponent spell) {
//                 spell.group.run();
//         }

//         public static class Builder {
//                 private ComponentGroup group = new ComponentGroup();
//                 private String name;

//                 public Builder(String name) {
//                         this.name = name;
//                 }

//                 public Builder addComponent(String name, ImpComponent component) {
//                         this.group.addComponent(XuanLingTypeEnum.SPELL, name, component);
//                         return this;
//                 }
//         }
// }
