package com.mingsiamz.xuanlingmod.data;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.controller.DataLoader;
import com.mingsiamz.xuanlingmod.type.ImpComponent;
import com.mingsiamz.xuanlingmod.type.RegistryComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;
import com.mingsiamz.xuanlingmod.type.spell.ParamType;

public final class ComponentCodec {

        private ComponentCodec() {
        }

        public static JsonObject encodeGroup(ComponentGroup group) {
                JsonObject root = new JsonObject();
                if (group == null)
                        return root;
                for (Map.Entry<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<ImpComponent>>> subTypes : group
                                .getMaps().entrySet()) {
                        for (Map.Entry<String, ConcurrentLinkedQueue<ImpComponent>> entry : subTypes.getValue()
                                        .entrySet()) {
                                JsonArray components = new JsonArray();
                                for (ImpComponent component : entry.getValue())
                                        components.add(encodeComponent(component));
                                root.add(entry.getKey(), components);
                        }
                }
                return root;
        }

        private static JsonObject encodeComponent(ImpComponent component) {
                JsonObject obj = new JsonObject();
                obj.addProperty("name", component.name());
                obj.addProperty("proficiency", component.getProficiency());

                JsonObject params = new JsonObject();
                if (component.getParams() != null) {
                        for (Map.Entry<ParamType, Object> param : component.getParams().entrySet())
                                params.add(param.getKey().name(), encodeValue(param.getValue()));
                }
                obj.add("params", params);
                return obj;
        }

        private static JsonElement encodeValue(Object value) {
                if (value instanceof Boolean b)
                        return new JsonPrimitive(b);
                if (value instanceof Number n)
                        return new JsonPrimitive(n);
                if (value instanceof String s)
                        return new JsonPrimitive(s);
                return new JsonPrimitive(String.valueOf(value));
        }

        public static ComponentGroup decodeGroup(XuanLingTypeEnum type, JsonObject root) {
                ComponentGroup group = new ComponentGroup();
                if (root != null)
                        decodeInto(type, group, root);
                return group;
        }

        private static void decodeInto(XuanLingTypeEnum type, ComponentGroup group, JsonObject obj) {
                for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                        if (entry.getValue().isJsonArray()) {
                                for (JsonElement element : entry.getValue().getAsJsonArray()) {
                                        if (!element.isJsonObject())
                                                continue;
                                        ImpComponent component = decodeComponent(type, element.getAsJsonObject());
                                        if (component != null)
                                                group.addComponent(type, entry.getKey(), component);
                                }
                        } else if (entry.getValue().isJsonObject()) {
                                decodeInto(type, group, entry.getValue().getAsJsonObject());
                        }
                }
        }

        private static ImpComponent decodeComponent(XuanLingTypeEnum type, JsonObject obj) {
                String name = obj.has("name") && obj.get("name").isJsonPrimitive()
                                ? obj.get("name").getAsString()
                                : null;
                if (name == null || name.isEmpty())
                        return null;

                ImpComponent proto = RegistryComponent.getComponent(type, name);
                if (proto == null) {
                        XuanLing.LOGGER.warn("ComponentCodec: unknown component '{}' for {}", name, type);
                        return null;
                }

                ImpComponent component;
                try {
                        component = proto.getClass().getDeclaredConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                        XuanLing.LOGGER.warn("ComponentCodec: cannot instantiate {}", proto.getClass(), e);
                        return null;
                }

                applyProficiency(component, obj);

                if (obj.has("params") && obj.get("params").isJsonObject()) {
                        for (Map.Entry<String, JsonElement> param : obj.getAsJsonObject("params").entrySet()) {
                                if (!param.getValue().isJsonPrimitive())
                                        continue;
                                ParamType paramType = paramTypeOf(param.getKey());
                                if (paramType != null)
                                        component.withParams(paramType,
                                                        decodeValue(param.getValue().getAsJsonPrimitive()));
                        }
                }
                return component;
        }

        private static void applyProficiency(ImpComponent component, JsonObject obj) {
                if (!obj.has("proficiency") || !obj.get("proficiency").isJsonPrimitive())
                        return;
                try {
                        Field field = ImpComponent.class.getDeclaredField("proficiency");
                        field.setAccessible(true);
                        field.setFloat(component, obj.get("proficiency").getAsFloat());
                } catch (ReflectiveOperationException e) {
                        XuanLing.LOGGER.debug("ComponentCodec: cannot restore proficiency for {}", component.name());
                }
        }

        private static ParamType paramTypeOf(String key) {
                for (ParamType type : ParamType.values())
                        if (type.name().equals(key))
                                return type;
                return null;
        }

        private static Object decodeValue(JsonPrimitive value) {
                if (value.isBoolean())
                        return value.getAsBoolean();
                if (value.isNumber()) {
                        BigDecimal number = value.getAsBigDecimal();
                        try {
                                if (number.scale() <= 0)
                                        return number.intValueExact();
                        } catch (ArithmeticException ignored) {
                        }
                        return number.doubleValue();
                }
                return value.getAsString();
        }

        public static ComponentGroup loadGroup(XuanLingTypeEnum type, String entityUuid, String name) {
                JsonObject root = DataLoader.loadJson(type, entityUuid, name);
                return root == null ? null : decodeGroup(type, root);
        }
}
