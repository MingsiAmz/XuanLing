package com.mingsiamz.xuanlingmod.auto.config;

import com.mingsiamz.xuanlingmod.data.ComponentGroup;
import com.mingsiamz.xuanlingmod.type.ImpComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public record WeightedComponent(
    String componentName,           
    String subType,                 
    int weight,                     
    Map<String, Object> defaultParams, 
    boolean required                
) {
    public WeightedComponent {
        defaultParams = defaultParams == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(defaultParams);
        if (weight < 0) weight = 0;
    }

    public static WeightedComponent of(String componentName, String subType, int weight) {
        return new WeightedComponent(componentName, subType, weight, null, false);
    }

    public static WeightedComponent required(String componentName, String subType) {
        return new WeightedComponent(componentName, subType, Integer.MAX_VALUE, null, true);
    }

    public WeightedComponent withParam(String key, Object value) {
        var newParams = new ConcurrentHashMap<>(defaultParams);
        newParams.put(key, value);
        return new WeightedComponent(componentName, subType, weight, newParams, required);
    }
}