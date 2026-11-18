package com.mingsiamz.xuanlingmod.auto.registry;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.auto.config.ArtTemplate;
import com.mingsiamz.xuanlingmod.auto.config.RealmTemplate;
import com.mingsiamz.xuanlingmod.auto.config.NPCTemplate;
import com.mingsiamz.xuanlingmod.auto.config.SpellTemplate;
import com.mingsiamz.xuanlingmod.auto.config.Rarity;
import com.mingsiamz.xuanlingmod.auto.config.ElementType;
import com.mingsiamz.xuanlingmod.auto.generator.NameGenerator;
import com.mingsiamz.xuanlingmod.auto.generator.StatGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;


public class TemplateRegistry extends SimpleJsonResourceReloadListener {
    private static final String DATA_FOLDER = "auto/templates";

    
    private static TemplateRegistry INSTANCE;

    
    private final Map<String, ArtTemplate> artTemplates = new ConcurrentHashMap<>();
    private final Map<String, RealmTemplate> realmTemplates = new ConcurrentHashMap<>();
    private final Map<String, SpellTemplate> spellTemplates = new ConcurrentHashMap<>();
    private final Map<String, NPCTemplate> npcTemplates = new ConcurrentHashMap<>();

    
    private final Map<Rarity, List<ArtTemplate>> artByRarity = new ConcurrentHashMap<>();
    private final Map<Rarity, List<RealmTemplate>> realmByRarity = new ConcurrentHashMap<>();
    private final Map<Rarity, List<SpellTemplate>> spellByRarity = new ConcurrentHashMap<>();
    private final Map<Rarity, List<NPCTemplate>> npcByRarity = new ConcurrentHashMap<>();

    
    private final Map<String, List<ArtTemplate>> artByElement = new ConcurrentHashMap<>();
    private final Map<ElementType, List<SpellTemplate>> spellByElement = new ConcurrentHashMap<>();

    
    private final Map<String, List<NPCTemplate>> npcByFaction = new ConcurrentHashMap<>();

    
    private final List<NPCTemplate> topProdigyTemplates = new ArrayList<>();

    
    private NameGenerator nameGenerator;
    private StatGenerator statGenerator;

    private TemplateRegistry() {
        super(new Gson(), DATA_FOLDER);
    }

    public static TemplateRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TemplateRegistry();
        }
        return INSTANCE;
    }

    public void setGenerators(NameGenerator nameGenerator, StatGenerator statGenerator) {
        this.nameGenerator = nameGenerator;
        this.statGenerator = statGenerator;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        XuanLing.LOGGER.info("Loading auto-generation templates from data packs...");
        
        
        clearAll();

        
        if (resources != null) {
            for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
                ResourceLocation location = entry.getKey();
                JsonElement element = entry.getValue();
                
                try {
                    if (element == null || !element.isJsonObject()) continue;
                    JsonObject object = element.getAsJsonObject();
                    String type = object.has("type") ? object.get("type").getAsString() : "";
                    
                    switch (type) {
                        case "art" -> loadArtTemplate(location, object);
                        case "realm" -> loadRealmTemplate(location, object);
                        case "spell" -> loadSpellTemplate(location, object);
                        case "npc" -> loadNPCTemplate(location, object);
                        default -> XuanLing.LOGGER.warn("Unknown template type '{}' in {}", type, location);
                    }
                } catch (Exception e) {
                    XuanLing.LOGGER.error("Failed to load template from {}: {}", location, e.getMessage());
                }
            }
        }

        
        rebuildIndexes();
        
        XuanLing.LOGGER.info("Loaded templates - Art: {}, Realm: {}, Spell: {}, NPC: {}",
                artTemplates.size(), realmTemplates.size(),
                spellTemplates.size(), npcTemplates.size());
    }

    private void clearAll() {
        artTemplates.clear();
        realmTemplates.clear();
        spellTemplates.clear();
        npcTemplates.clear();
        artByRarity.clear();
        realmByRarity.clear();
        spellByRarity.clear();
        npcByRarity.clear();
        artByElement.clear();
        spellByElement.clear();
        npcByFaction.clear();
        topProdigyTemplates.clear();
    }

    private void rebuildIndexes() {
        
        for (ArtTemplate t : artTemplates.values()) {
            artByRarity.computeIfAbsent(t.rarity(), k -> new ArrayList<>()).add(t);
            if (t.elementType() != null) {
                artByElement.computeIfAbsent(t.elementType(), k -> new ArrayList<>()).add(t);
            }
        }
        
        
        for (RealmTemplate t : realmTemplates.values()) {
            realmByRarity.computeIfAbsent(t.rarity(), k -> new ArrayList<>()).add(t);
        }
        
        
        for (SpellTemplate t : spellTemplates.values()) {
            spellByRarity.computeIfAbsent(t.rarity(), k -> new ArrayList<>()).add(t);
            if (t.element() != null) {
                spellByElement.computeIfAbsent(t.element(), k -> new ArrayList<>()).add(t);
            }
        }
        
        
        for (NPCTemplate t : npcTemplates.values()) {
            npcByRarity.computeIfAbsent(t.rarity(), k -> new ArrayList<>()).add(t);
            npcByFaction.computeIfAbsent(t.faction(), k -> new ArrayList<>()).add(t);
            if (t.hasCustomRealm()) {
                topProdigyTemplates.add(t);
            }
        }
    }

    
    
    private void loadArtTemplate(ResourceLocation location, JsonObject object) {
        ArtTemplate template = ArtTemplate.fromJson(location.getPath(), object);
        if (template != null) {
            artTemplates.put(template.id(), template);
        }
    }

    private void loadRealmTemplate(ResourceLocation location, JsonObject object) {
        RealmTemplate template = RealmTemplate.fromJson(location.getPath(), object);
        if (template != null) {
            realmTemplates.put(template.id(), template);
        }
    }

    private void loadSpellTemplate(ResourceLocation location, JsonObject object) {
        SpellTemplate template = SpellTemplate.fromJson(location.getPath(), object);
        if (template != null) {
            spellTemplates.put(template.id(), template);
        }
    }

    private void loadNPCTemplate(ResourceLocation location, JsonObject object) {
        NPCTemplate template = NPCTemplate.fromJson(location.getPath(), object);
        if (template != null) {
            npcTemplates.put(template.id(), template);
        }
    }

    
    
    public void registerArt(ArtTemplate template) {
        artTemplates.put(template.id(), template);
        artByRarity.computeIfAbsent(template.rarity(), k -> new ArrayList<>()).add(template);
        if (template.elementType() != null) {
            artByElement.computeIfAbsent(template.elementType(), k -> new ArrayList<>()).add(template);
        }
    }

    public void registerRealm(RealmTemplate template) {
        realmTemplates.put(template.id(), template);
        realmByRarity.computeIfAbsent(template.rarity(), k -> new ArrayList<>()).add(template);
    }

    public void registerSpell(SpellTemplate template) {
        spellTemplates.put(template.id(), template);
        spellByRarity.computeIfAbsent(template.rarity(), k -> new ArrayList<>()).add(template);
        if (template.element() != null) {
            spellByElement.computeIfAbsent(template.element(), k -> new ArrayList<>()).add(template);
        }
    }

    public void registerNPC(NPCTemplate template) {
        npcTemplates.put(template.id(), template);
        npcByRarity.computeIfAbsent(template.rarity(), k -> new ArrayList<>()).add(template);
        npcByFaction.computeIfAbsent(template.faction(), k -> new ArrayList<>()).add(template);
        if (template.hasCustomRealm()) {
            topProdigyTemplates.add(template);
        }
    }

    
    
    public ArtTemplate getArt(String id) {
        return artTemplates.get(id);
    }

    public RealmTemplate getRealm(String id) {
        return realmTemplates.get(id);
    }

    public SpellTemplate getSpell(String id) {
        return spellTemplates.get(id);
    }

    public NPCTemplate getNPC(String id) {
        return npcTemplates.get(id);
    }

    public Collection<ArtTemplate> getAllArt() {
        return Collections.unmodifiableCollection(artTemplates.values());
    }

    public Collection<RealmTemplate> getAllRealm() {
        return Collections.unmodifiableCollection(realmTemplates.values());
    }

    public Collection<SpellTemplate> getAllSpell() {
        return Collections.unmodifiableCollection(spellTemplates.values());
    }

    public Collection<NPCTemplate> getAllNPCs() {
        return Collections.unmodifiableCollection(npcTemplates.values());
    }

    
    
    public List<ArtTemplate> getArtByRarity(Rarity rarity) {
        return artByRarity.getOrDefault(rarity, Collections.emptyList());
    }

    public List<RealmTemplate> getRealmByRarity(Rarity rarity) {
        return realmByRarity.getOrDefault(rarity, Collections.emptyList());
    }

    public List<SpellTemplate> getSpellByRarity(Rarity rarity) {
        return spellByRarity.getOrDefault(rarity, Collections.emptyList());
    }

    public List<NPCTemplate> getNPCByRarity(Rarity rarity) {
        return npcByRarity.getOrDefault(rarity, Collections.emptyList());
    }

    public List<ArtTemplate> getArtByElement(String elementType) {
        return artByElement.getOrDefault(elementType, Collections.emptyList());
    }

    public List<SpellTemplate> getSpellByElement(String elementType) {
        return spellByElement.getOrDefault(elementType, Collections.emptyList());
    }

    public List<NPCTemplate> getNPCByFaction(String faction) {
        return npcByFaction.getOrDefault(faction, Collections.emptyList());
    }

    public List<NPCTemplate> getTopProdigyTemplates() {
        return Collections.unmodifiableList(topProdigyTemplates);
    }

    
    
    
    public ArtTemplate getRandomArt(Random random, Predicate<ArtTemplate> filter) {
        List<ArtTemplate> candidates = getAllArt().stream()
                .filter(filter)
                .toList();
        return weightedRandom(random, candidates, ArtTemplate::rarity);
    }

    
    public SpellTemplate getRandomSpell(Random random, Predicate<SpellTemplate> filter) {
        List<SpellTemplate> candidates = getAllSpell().stream()
                .filter(filter)
                .toList();
        return weightedRandom(random, candidates, SpellTemplate::rarity);
    }

    
    public RealmTemplate getRandomRealm(Random random, Predicate<RealmTemplate> filter) {
        List<RealmTemplate> candidates = getAllRealm().stream()
                .filter(filter)
                .toList();
        return weightedRandom(random, candidates, RealmTemplate::rarity);
    }

    
    public NPCTemplate getRandomNPC(Random random, Predicate<NPCTemplate> filter) {
        List<NPCTemplate> candidates = getAllNPCs().stream()
                .filter(filter)
                .toList();
        return weightedRandom(random, candidates, NPCTemplate::rarity);
    }

    
    public <T> T getRandomFromRarity(Random random, Rarity rarity, 
                                     List<T> templates, 
                                     java.util.function.Function<T, Rarity> rarityGetter) {
        List<T> candidates = templates.stream()
                .filter(t -> rarityGetter.apply(t) == rarity)
                .toList();
        if (candidates.isEmpty()) return null;
        return candidates.get(random.nextInt(candidates.size()));
    }

    private <T> T weightedRandom(Random random, List<T> candidates, 
                                  java.util.function.Function<T, Rarity> rarityGetter) {
        if (candidates.isEmpty()) return null;
        
        int totalWeight = candidates.stream()
                .mapToInt(t -> rarityGetter.apply(t).getWeight())
                .sum();
        
        if (totalWeight <= 0) {
            return candidates.get(random.nextInt(candidates.size()));
        }
        
        int roll = random.nextInt(totalWeight);
        int accumulator = 0;
        
        for (T candidate : candidates) {
            accumulator += rarityGetter.apply(candidate).getWeight();
            if (roll < accumulator) {
                return candidate;
            }
        }
        
        return candidates.get(candidates.size() - 1);
    }

    
    
    public NameGenerator getNameGenerator() {
        return nameGenerator;
    }

    public StatGenerator getStatGenerator() {
        return statGenerator;
    }

    
    
    public int getArtCount() { return artTemplates.size(); }
    public int getRealmCount() { return realmTemplates.size(); }
    public int getSpellCount() { return spellTemplates.size(); }
    public int getNPCCount() { return npcTemplates.size(); }
    public int getTopProdigyCount() { return topProdigyTemplates.size(); }
}