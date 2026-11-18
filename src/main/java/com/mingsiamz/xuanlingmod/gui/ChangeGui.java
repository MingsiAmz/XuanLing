package com.mingsiamz.xuanlingmod.gui;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;


public class ChangeGui {
        private static final Map<UUID, Integer> INDEX_BY_PLAYER = new ConcurrentHashMap<>();

        private static final List<Supplier<Screen>> SCREENS = List.of(
                        StateGui::new,
                        DetailDataGui::new);

        private static int getIndex(Player player) {
                return INDEX_BY_PLAYER.computeIfAbsent(player.getUUID(), k -> 0);
        }

        private static void setIndex(Player player, int value) {
                INDEX_BY_PLAYER.put(player.getUUID(), value);
        }

        private static int clientIndex() {
                Player p = Minecraft.getInstance().player;
                return p == null ? 0 : getIndex(p);
        }

        private static void setClientIndex(int value) {
                Player p = Minecraft.getInstance().player;
                if (p != null)
                        setIndex(p, value);
        }

        public static boolean isLast() {
                return clientIndex() == SCREENS.size() - 1;
        }

        public static boolean isValid(Player player) {
                int i = getIndex(player);
                return i >= 0 && i < SCREENS.size();
        }

        public static Screen current() {
                return SCREENS.get(clientIndex()).get();
        }

        public static Screen current(Player player) {
                return SCREENS.get(getIndex(player)).get();
        }

        public static boolean next() {
                if (clientIndex() + 1 >= SCREENS.size()) {
                        setClientIndex(0);
                        return false;
                }
                setClientIndex(clientIndex() + 1);
                return true;
        }

        public static boolean next(Player player) {
                if (getIndex(player) + 1 >= SCREENS.size()) {
                        setIndex(player, 0);
                        return false;
                }
                setIndex(player, getIndex(player) + 1);
                return true;
        }

        public static boolean previous() {
                if (clientIndex() - 1 < 0) {
                        setClientIndex(SCREENS.size() - 1);
                        return false;
                }
                setClientIndex(clientIndex() - 1);
                return true;
        }

        public static boolean previous(Player player) {
                if (getIndex(player) - 1 < 0) {
                        setIndex(player, SCREENS.size() - 1);
                        return false;
                }
                setIndex(player, getIndex(player) - 1);
                return true;
        }

        public static Screen cycle(char c) {
                switch (c) {
                case '0' -> {
                        setClientIndex(0);
                }
                case 'n' -> {
                        if (!next())
                                setClientIndex(0);
                }
                case 'p' -> {
                        if (!previous())
                                setClientIndex(SCREENS.size() - 1);
                }
                }
                return current();
        }

        public static int index() {
                return clientIndex();
        }

        public static int index(Player player) {
                return getIndex(player);
        }

        public static int length() {
                return SCREENS.size();
        }
}
