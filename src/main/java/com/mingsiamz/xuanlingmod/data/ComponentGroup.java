package com.mingsiamz.xuanlingmod.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

public class ComponentGroup {
        private Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<ImpComponent>>> maps = new ConcurrentHashMap<>();

        public ConcurrentLinkedQueue<ImpComponent> getComponents(XuanLingTypeEnum type, String subType) {
                ConcurrentLinkedQueue<ImpComponent> tmp = this.maps.get(type.type).get(subType);
                if (tmp == null || tmp.isEmpty())
                        return null;
                return tmp;
        }

        public boolean contains(XuanLingTypeEnum type, String subType, ImpComponent component) {
                return this.getComponents(type, subType).contains(component);
        }

        public boolean contains(ImpComponent component) {
                for (ConcurrentHashMap<String, ConcurrentLinkedQueue<ImpComponent>> value : this.maps.values())
                        for (Map.Entry<String, ConcurrentLinkedQueue<ImpComponent>> list : value.entrySet())
                                if (list.getValue().contains(component))
                                        return true;
                return false;
        }

        public ComponentGroup addComponent(XuanLingTypeEnum type, String subType, ImpComponent component) {
                this.maps.computeIfAbsent(type.type, k -> new ConcurrentHashMap<>())
                                .computeIfAbsent(subType, k -> new ConcurrentLinkedQueue<>()).add(component);
                return this;
        }

        public ComponentGroup removeComponent(XuanLingTypeEnum type, String subType, ImpComponent component) {
                ConcurrentLinkedQueue<ImpComponent> components = getComponents(type, subType);
                if (components.contains(component))
                        components.remove(component);
                return this;
        }

        public void run() {
                this.maps.values().forEach(
                                map -> map.values().forEach(list -> list.forEach(component -> component.run())));
        }
}
