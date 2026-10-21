package com.mingsiamz.xuanlingmod.register;

import org.lwjgl.glfw.GLFW;

import com.mingsiamz.xuanlingmod.XuanLing;

import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class RegisterKey {
        public static final KeyMapping KEY_R = reKey("opengui", GLFW.GLFW_KEY_R);
        public static final KeyMapping KEY_MEDITATION = reKey("meditation", GLFW.GLFW_KEY_DOWN);

        private static KeyMapping reKey(String name, int glfw) {
                return new KeyMapping("key." + XuanLing.MODID + "." + name, glfw, "key." + XuanLing.MODID + ".keys");
        }

        public static void register(RegisterKeyMappingsEvent event) {
                event.register(KEY_R);
                event.register(KEY_MEDITATION);
        }
}
