package com.mingsiamz.xuanlingmod.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

public class ComponentGroup {
        private XuanLingTypeEnum type;
        private Map<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<ImpComponent>>> maps = new ConcurrentHashMap<>();

        public ComponentGroup(XuanLingTypeEnum type) {
                this.type = type;
        }

        public ComponentGroup(XuanLingTypeEnum type, String componentType, ConcurrentLinkedQueue<ImpComponent> list) {
                this.maps.computeIfAbsent(type.type, k -> new ConcurrentHashMap<>()).put(componentType, list);
        }

        public ComponentGroup addComponent(String type, ImpComponent component) {
                this.maps.computeIfAbsent(this.type.type, k -> new ConcurrentHashMap<>()).computeIfAbsent(type, k -> new ConcurrentLinkedQueue<>()).add(component);
                return this;
        }

        public void run() {
                this.maps.values().forEach(map -> map.values().forEach(list -> list.forEach(component -> component.run())));
        }
}
