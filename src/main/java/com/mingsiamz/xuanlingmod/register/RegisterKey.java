package com.mingsiamz.xuanlingmod.register;

import org.lwjgl.glfw.GLFW;

import com.mingsiamz.xuanlingmod.XuanLing;

import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class RegisterKey {
        public static final KeyMapping KEY_LALT = reKey("left_alt", GLFW.GLFW_KEY_LEFT_ALT);
        public static final KeyMapping KEY_R = reKey("opengui", GLFW.GLFW_KEY_R);
        public static final KeyMapping KEY_MEDITATION = reKey("meditation", GLFW.GLFW_KEY_DOWN);
        public static final KeyMapping KEY_G = reKey("g", GLFW.GLFW_KEY_G);
        public static final KeyMapping KEY_C = reKey("c", GLFW.GLFW_KEY_C);
        public static final KeyMapping KEY_V = reKey("v", GLFW.GLFW_KEY_V);
        public static final KeyMapping KEY_B = reKey("b", GLFW.GLFW_KEY_B);
        public static final KeyMapping KEY_RELEASE = reKey("release", GLFW.GLFW_KEY_GRAVE_ACCENT);
        public static final KeyMapping KEY_HIGH_USE = reKey("expend_energy_use", GLFW.GLFW_KEY_H);

        private static KeyMapping reKey(String name, int glfw) {
                return new KeyMapping("key." + XuanLing.MODID + "." + name, glfw, "key." + XuanLing.MODID + ".keys");
        }

        public static void register(RegisterKeyMappingsEvent event) {
                event.register(KEY_LALT);
                event.register(KEY_R);
                event.register(KEY_MEDITATION);
                event.register(KEY_G);
                event.register(KEY_C);
                event.register(KEY_V);
                event.register(KEY_B);
                event.register(KEY_RELEASE);
                event.register(KEY_HIGH_USE);
        }
}
