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

        
        public Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<ImpComponent>>> getMaps() {
                return this.maps;
        }

        public boolean contains(XuanLingTypeEnum type) {
                return this.maps.containsKey(type.type);
        }

        public boolean contains(XuanLingTypeEnum type, String subType) {
                return this.maps.get(type.type).containsKey(subType);
        }

        public boolean contains(XuanLingTypeEnum type, ImpComponent subType) {
                return this.maps.get(type.type).containsKey(subType.type());
        }

        public boolean contains(XuanLingTypeEnum type, ImpComponent subType, ImpComponent component) {
                return this.getComponents(type, subType.type()).contains(component);
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

        public ComponentGroup removeComponent(XuanLingTypeEnum type, ImpComponent subType, ImpComponent component) {
                ConcurrentLinkedQueue<ImpComponent> components = getComponents(type, subType.type());
                if (components.contains(component))
                        components.remove(component);
                return this;
        }

        public void run() {
                this.maps.values().forEach(
                                map -> map.values().forEach(list -> list.forEach(component -> component.run())));
        }

        public int size() {
                return this.maps.size();
        }

        public int size(XuanLingTypeEnum type) {
                return this.maps.get(type.type).size();
        }

        public int size(XuanLingTypeEnum type, ImpComponent subType) {
                return this.maps.get(type.type).get(subType.type()).size();
        }

        public boolean isEmpty() {
                return this.maps == null || this.maps.isEmpty();
        }
}
