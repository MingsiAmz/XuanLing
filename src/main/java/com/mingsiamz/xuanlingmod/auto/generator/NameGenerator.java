package com.mingsiamz.xuanlingmod.auto.generator;

import com.mingsiamz.xuanlingmod.auto.config.ArtTemplate;
import com.mingsiamz.xuanlingmod.auto.config.RealmTemplate;
import com.mingsiamz.xuanlingmod.auto.config.NPCTemplate;
import com.mingsiamz.xuanlingmod.auto.config.SpellTemplate;
import com.mingsiamz.xuanlingmod.auto.config.Rarity;
import com.mingsiamz.xuanlingmod.auto.registry.TemplateRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class NameGenerator {
    
    private final Map<String, List<String>> prefixPools = new ConcurrentHashMap<>();
    private final Map<String, List<String>> suffixPools = new ConcurrentHashMap<>();
    private final Map<String, List<String>> baseNamePools = new ConcurrentHashMap<>();
    private final Map<String, List<String>> titlePools = new ConcurrentHashMap<>();

    
    private TemplateRegistry registry;

    public NameGenerator() {
        initializeDefaultPools();
    }

    public void setRegistry(TemplateRegistry registry) {
        this.registry = registry;
    }

    private void initializeDefaultPools() {
        
        prefixPools.put("art:common", Arrays.asList(
                "入门", "基础", "普通", "简易", "粗浅", "初级", "浅显"
        ));
        prefixPools.put("art:uncommon", Arrays.asList(
                "进阶", "精修", "内门", "真传", "心得", "奥义", "玄机"
        ));
        prefixPools.put("art:rare", Arrays.asList(
                "上古", "失传", "秘传", "绝学", "至宝", "仙法", "神功"
        ));
        prefixPools.put("art:epic", Arrays.asList(
                "太古", "洪荒", "混沌", "鸿蒙", "造化", "开天", "证道"
        ));
        prefixPools.put("art:legendary", Arrays.asList(
                "永恒", "不朽", "超脱", "彼岸", "道祖", "天道", "本源"
        ));

        
        suffixPools.put("art:common", Arrays.asList(
                "诀", "法", "术", "式", "功", "篇", "章"
        ));
        suffixPools.put("art:uncommon", Arrays.asList(
                "心法", "内功", "吐纳", "炼气", "引气", "聚气"
        ));
        suffixPools.put("art:rare", Arrays.asList(
                "真经", "宝典", "秘录", "神篇", "仙经", "大法"
        ));
        suffixPools.put("art:epic", Arrays.asList(
                "圣典", "道经", "天书", "神典", "帝经", "皇经"
        ));
        suffixPools.put("art:legendary", Arrays.asList(
                "天道", "大道", "本源", "永恒", "不朽", "彼岸"
        ));

        
        prefixPools.put("spell:common", Arrays.asList(
                "初级", "基础", "普通", "简易", "粗浅", "入门"
        ));
        prefixPools.put("spell:uncommon", Arrays.asList(
                "进阶", "精通", "高深", "玄妙", "奇门", "异术"
        ));
        prefixPools.put("spell:rare", Arrays.asList(
                "上古", "失传", "绝学", "禁术", "神通", "仙术"
        ));
        prefixPools.put("spell:epic", Arrays.asList(
                "太古", "洪荒", "混沌", "鸿蒙", "造化", "开天"
        ));
        prefixPools.put("spell:legendary", Arrays.asList(
                "永恒", "不朽", "超脱", "彼岸", "道祖", "天道"
        ));

        
        suffixPools.put("spell:common", Arrays.asList(
                "术", "法", "诀", "式", "技", "招", "式"
        ));
        suffixPools.put("spell:uncommon", Arrays.asList(
                "神通", "秘术", "奥义", "绝技", "杀招", "必杀"
        ));
        suffixPools.put("spell:rare", Arrays.asList(
                "大法", "神掌", "剑诀", "禁术", "仙术", "天罚"
        ));
        suffixPools.put("spell:epic", Arrays.asList(
                "圣法", "道法", "天罚", "神罚", "帝术", "皇术"
        ));
        suffixPools.put("spell:legendary", Arrays.asList(
                "天道", "大道", "本源", "永恒", "不朽", "彼岸"
        ));

        
        baseNamePools.put("realm:realm_names", Arrays.asList(
                "凡人", "炼气", "筑基", "金丹", "元婴", "化神",
                "合体", "大乘", "渡劫", "飞升", "真仙", "金仙",
                "太乙", "大罗", "准圣", "圣人", "道祖"
        ));

        
        titlePools.put("npc:cultivation", Arrays.asList(
                "外门弟子", "内门弟子", "核心弟子", "真传弟子", "首席弟子",
                "长老", "大长老", "护法", "供奉", "太上长老",
                "宗主", "掌教", "教主", "盟主", "尊者"
        ));
        titlePools.put("npc:rogue", Arrays.asList(
                "散修", "游方修士", "独行客", "魔头", "凶徒",
                "邪修", "妖孽", "祸害", "恶煞", "魔尊"
        ));
        titlePools.put("npc:sect", Arrays.asList(
                "掌门", "副掌门", "长老", "护法", "执事",
                "峰主", "殿主", "院主", "阁主", "使者"
        ));
    }

    
    
    public String generateArtName(ArtTemplate template, Random random) {
        return template.generateName(random);
    }

    public String generateRandomArtName(Rarity rarity, Random random) {
        String prefix = getRandomPrefix("art", rarity, random);
        String baseName = getRandomBaseName("cultivation", random);
        String suffix = getRandomSuffix("art", rarity, random);
        
        StringBuilder name = new StringBuilder();
        if (prefix != null && random.nextDouble() < 0.7) {
            name.append(prefix).append(" ");
        }
        name.append(baseName);
        if (suffix != null) {
            name.append(suffix);
        }
        return name.toString();
    }

    
    
    public String generateSpellName(SpellTemplate template, Random random) {
        return template.generateName(random);
    }

    public String generateRandomSpellName(Rarity rarity, Random random) {
        String prefix = getRandomPrefix("spell", rarity, random);
        String baseName = getRandomBaseName("ability", random);
        String suffix = getRandomSuffix("spell", rarity, random);
        
        StringBuilder name = new StringBuilder();
        if (prefix != null && random.nextDouble() < 0.6) {
            name.append(prefix).append(" ");
        }
        name.append(baseName);
        if (suffix != null) {
            name.append(suffix);
        }
        return name.toString();
    }

    
    
    public String generateRealmName(RealmTemplate template, int minorLevel, Random random) {
        return template.name();
    }

    public String generateRandomRealmName(int level, Random random) {
        List<String> names = baseNamePools.getOrDefault("realm:realm_names", List.of("未知"));
        if (level >= 0 && level < names.size()) {
            return names.get(level);
        }
        return "未知境界";
    }

    
    
    public String generateNPCTitle(NPCTemplate template, boolean isTopProdigy, Random random) {
        if (template.title() != null && !template.title().isEmpty()) {
            return template.title();
        }
        
        String poolKey = switch (template.faction()) {
            case "rogue", "evil", "demon" -> "npc:rogue";
            case "sect", "orthodox", "righteous" -> "npc:sect";
            default -> "npc:cultivation";
        };
        
        List<String> titles = titlePools.getOrDefault(poolKey, titlePools.get("npc:cultivation"));
        if (titles.isEmpty()) return "";
        
        if (isTopProdigy && random.nextDouble() < 0.5) {
            return "天骄·" + titles.get(random.nextInt(titles.size()));
        }
        
        return titles.get(random.nextInt(titles.size()));
    }

    
    
    private String getRandomPrefix(String type, Rarity rarity, Random random) {
        String key = type + ":" + rarity.name().toLowerCase();
        List<String> pool = prefixPools.get(key);
        if (pool == null || pool.isEmpty()) {
            
            pool = prefixPools.get(type + ":common");
        }
        return pool != null && !pool.isEmpty() ? pool.get(random.nextInt(pool.size())) : null;
    }

    private String getRandomSuffix(String type, Rarity rarity, Random random) {
        String key = type + ":" + rarity.name().toLowerCase();
        List<String> pool = suffixPools.get(key);
        if (pool == null || pool.isEmpty()) {
            pool = suffixPools.get(type + ":common");
        }
        return pool != null && !pool.isEmpty() ? pool.get(random.nextInt(pool.size())) : null;
    }

    private String getRandomBaseName(String category, Random random) {
        List<String> pool = baseNamePools.getOrDefault(category, List.of("无名"));
        return pool.get(random.nextInt(pool.size()));
    }

    private String getPhaseName(int minorLevel, int totalSize, Random random) {
        if (totalSize <= 3) {
            String[] phases = {"初期", "中期", "后期"};
            return phases[Math.min(minorLevel - 1, 2)];
        } else if (totalSize <= 9) {
            
            if (minorLevel <= 3) return "初期";
            if (minorLevel <= 6) return "中期";
            if (minorLevel <= 9) return "后期";
            return "大圆满";
        } else {
            
            if (minorLevel <= totalSize / 3) return "初期";
            if (minorLevel <= 2 * totalSize / 3) return "中期";
            if (minorLevel < totalSize) return "后期";
            return "大圆满";
        }
    }

    
    
    public void addPrefixes(String key, List<String> prefixes) {
        prefixPools.computeIfAbsent(key, k -> new ArrayList<>()).addAll(prefixes);
    }

    public void addSuffixes(String key, List<String> suffixes) {
        suffixPools.computeIfAbsent(key, k -> new ArrayList<>()).addAll(suffixes);
    }

    public void addBaseNames(String key, List<String> names) {
        baseNamePools.computeIfAbsent(key, k -> new ArrayList<>()).addAll(names);
    }

    public void addTitles(String key, List<String> titles) {
        titlePools.computeIfAbsent(key, k -> new ArrayList<>()).addAll(titles);
    }

    public void clearPool(String key) {
        prefixPools.remove(key);
        suffixPools.remove(key);
        baseNamePools.remove(key);
        titlePools.remove(key);
    }
}