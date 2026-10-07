package com.mingsiamz.xuanlingmod.gui;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.gui.screens.Screen;

public class ChangeGui {
        private static int index = 0;

        private static final List<Supplier<Screen>> SCREENS = List.of(
                        StateGui::new,
                        DetailDataGui::new);

        public static boolean isLast() {
                return index == SCREENS.size() - 1;
        }

        public static boolean isValid() {
                return index >= 0 && index < length();
        }

        public static Screen current() {
                return SCREENS.get(index).get();
        }

        public static boolean next() {
                if (index + 1 >= length())
                        return false;
                index++;
                return true;
        }

        public static boolean previous() {
                if (index - 1 < 0)
                        return false;
                index--;
                return true;
        }

        public static Screen cycle(char c) {
                switch (c) {
                case 'n' -> {if (!next()) index = 0;}
                case 'p' -> {if (!previous()) index = length() - 1;}
                }
                return current();
        }

        public static int index() {
                return index;
        }
        
        public static int length() {
                return SCREENS.size();
        }
}
