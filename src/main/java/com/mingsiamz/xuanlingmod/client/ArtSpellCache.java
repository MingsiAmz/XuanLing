package com.mingsiamz.xuanlingmod.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;


@EventBusSubscriber(modid = XuanLing.MODID, value = Dist.CLIENT)
public class ArtSpellCache {

    private static List<String> cachedArts = Collections.emptyList();
    private static List<String> cachedSpells = Collections.emptyList();
    private static boolean initialized = false;

    @SubscribeEvent
    public static void onPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        if (event.getPlayer() == null) return;
        
        CompoundTag persistentData = event.getPlayer().getPersistentData();
        
        String artKey = XuanLing.MODID + XuanLingTypeEnum.ART.type;
        if (persistentData.contains(artKey, Tag.TAG_COMPOUND)) {
            CompoundTag artData = persistentData.getCompound(artKey);
            cachedArts = extractNames(artData);
        } else {
            cachedArts = Collections.emptyList();
        }
        
        String spellKey = XuanLing.MODID + XuanLingTypeEnum.SPELL.type;
        if (persistentData.contains(spellKey, Tag.TAG_COMPOUND)) {
            CompoundTag spellData = persistentData.getCompound(spellKey);
            cachedSpells = extractNames(spellData);
        } else {
            cachedSpells = Collections.emptyList();
        }
        
        initialized = true;
        XuanLing.LOGGER.info("[ArtSpellCache] Initialized cache: {} arts, {} spells", cachedArts.size(), cachedSpells.size());
    }

    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        clear();
        XuanLing.LOGGER.info("[ArtSpellCache] Cleared cache on logout");
    }

    @SubscribeEvent
    public static void onPlayerClone(ClientPlayerNetworkEvent.Clone event) {
        if (event.getPlayer() == null) return;
        
        CompoundTag persistentData = event.getPlayer().getPersistentData();
        
        String artKey = XuanLing.MODID + XuanLingTypeEnum.ART.type;
        if (persistentData.contains(artKey, Tag.TAG_COMPOUND)) {
            CompoundTag artData = persistentData.getCompound(artKey);
            cachedArts = extractNames(artData);
        } else {
            cachedArts = Collections.emptyList();
        }
        
        String spellKey = XuanLing.MODID + XuanLingTypeEnum.SPELL.type;
        if (persistentData.contains(spellKey, Tag.TAG_COMPOUND)) {
            CompoundTag spellData = persistentData.getCompound(spellKey);
            cachedSpells = extractNames(spellData);
        } else {
            cachedSpells = Collections.emptyList();
        }
        
        initialized = true;
        XuanLing.LOGGER.info("[ArtSpellCache] Re-initialized cache on respawn: {} arts, {} spells", cachedArts.size(), cachedSpells.size());
    }

    public static void updateCache(String key, CompoundTag data) {
        if (key == null || data == null) return;
        
        String artKey = XuanLing.MODID + XuanLingTypeEnum.ART.type;
        String spellKey = XuanLing.MODID + XuanLingTypeEnum.SPELL.type;
        
        if (key.equals(artKey)) {
            cachedArts = extractNames(data);
            XuanLing.LOGGER.debug("[ArtSpellCache] Updated arts cache: {} items", cachedArts.size());
        } else if (key.equals(spellKey)) {
            cachedSpells = extractNames(data);
            XuanLing.LOGGER.debug("[ArtSpellCache] Updated spells cache: {} items", cachedSpells.size());
        }
    }

    public static List<String> getArts() {
        return cachedArts;
    }

    public static List<String> getSpells() {
        return cachedSpells;
    }

    public static List<String> getByType(XuanLingTypeEnum type) {
        return switch (type) {
            case ART -> cachedArts;
            case SPELL -> cachedSpells;
            default -> Collections.emptyList();
        };
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void clear() {
        cachedArts = Collections.emptyList();
        cachedSpells = Collections.emptyList();
        initialized = false;
    }

    private static List<String> extractNames(CompoundTag tag) {
        if (tag == null || tag.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        Iterator<String> it = tag.getAllKeys().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (tag.get(key) instanceof net.minecraft.nbt.StringTag stringTag) {
                list.add(stringTag.getAsString());
            }
        }
        return list;
    }
}
