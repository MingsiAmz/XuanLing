package com.mingsiamz.xuanlingmod.event;

import com.mingsiamz.xuanlingmod.ai.CultivatorSpellGoal;
import com.mingsiamz.xuanlingmod.auto.config.GenerationConfig;
import com.mingsiamz.xuanlingmod.auto.config.NPCTemplate;
import com.mingsiamz.xuanlingmod.auto.config.Rarity;
import com.mingsiamz.xuanlingmod.auto.generator.GenerationEngine;
import com.mingsiamz.xuanlingmod.auto.generator.StatGenerator;
import com.mingsiamz.xuanlingmod.items.JadeSlipItem;
import com.mingsiamz.xuanlingmod.XuanLing;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class AutoGenerationEvents {
    private static final Random RANDOM = ThreadLocalRandom.current();
    private static final GenerationEngine ENGINE = GenerationEngine.getInstance();

    public static final String NPC_DATA = XuanLing.MODID + "_npc";
    public static final String GENERATED = "generated";
    public static final String CULTIVATOR = "cultivator";
    public static final String REALM_LEVEL = "realm_level";
    public static final String ART_NAME = "art_name";
    public static final String ART_POWER = "art_power";
    public static final String SPELL_NAME = "spell_name";
    public static final String SPELL_CATEGORY = "spell_category";
    public static final String SPELL_DAMAGE = "spell_damage";
    public static final String SPELL_HEAL = "spell_heal";
    public static final String SPELL_COOLDOWN = "spell_cooldown";

    
    private static final Map<EntityType<?>, NPCTemplate> ENTITY_TYPE_CACHE = new HashMap<>();

    
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!GenerationConfig.ENABLE_JADE_SLIP_DROPS.get()) return;

        LivingEntity victim = event.getEntity();
        Level level = victim.level();

        if (level.isClientSide()) return;

        
        if (victim instanceof Player) return;

        
        Entity killer = null;
        if (event.getSource() != null) {
            killer = event.getSource().getEntity();
        }
        String killerName = killer != null ? killer.getName().getString() : "unknown";

        
        NPCTemplate npcTemplate = getNPCTemplateForEntity(victim.getType());
        if (npcTemplate == null) return;

        
        double dropChance = calculateJadeSlipDropChance(npcTemplate, victim, killer);

        if (RANDOM.nextDouble() < dropChance) {
            
            boolean isTopProdigy = npcTemplate.hasCustomRealm() &&
                    RANDOM.nextDouble() < npcTemplate.topProdigyChance();

            ItemStack jadeSlip;
            if (isTopProdigy) {
                
                int realmLevel = getRealmLevel(victim);
                var npcStats = ENGINE.generateNPC(npcTemplate.id(), realmLevel);
                if (npcStats != null && npcStats.isTopProdigy()) {
                    jadeSlip = JadeSlipItem.createFromTopProdigy(npcStats, killerName);
                } else {
                    jadeSlip = JadeSlipItem.createRandom(npcTemplate.rarity(), realmLevel, "loot");
                }
            } else {
                int realmLevel = getRealmLevel(victim);
                jadeSlip = JadeSlipItem.createRandom(npcTemplate.rarity(), realmLevel, "loot");
            }

            if (!jadeSlip.isEmpty()) {
                Vec3 pos = victim.position();
                level.addFreshEntity(new ItemEntity(level, pos.x, pos.y, pos.z, jadeSlip));
            }
        }
    }

    
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!GenerationConfig.ENABLE_NPC_GENERATION.get()) return;

        Entity entity = event.getEntity();
        Level level = entity.level();

        if (level.isClientSide() || !(entity instanceof LivingEntity living)) return;

        
        if (living instanceof Player) return;

        NPCTemplate template = findMatchingNPCTemplate(living.getType());
        if (template == null) return;

        
        CompoundTag data = living.getPersistentData().getCompound(NPC_DATA);
        if (data.getBoolean(GENERATED)) {
            attachSpellGoal(living, data);
            return;
        }

        data.putBoolean(GENERATED, true);
        if (RANDOM.nextDouble() >= GenerationConfig.NPC_CULTIVATOR_CHANCE.get()) {
            living.getPersistentData().put(NPC_DATA, data);
            return;
        }

        int realmLevel = getRealmLevel(living);
        var npcStats = ENGINE.generateNPC(template.id(), realmLevel);

        if (npcStats != null) {
            data.putBoolean(CULTIVATOR, true);
            attachNPCData(living, npcStats, data);
        }
        living.getPersistentData().put(NPC_DATA, data);
        attachSpellGoal(living, data);
    }

    private static void attachSpellGoal(LivingEntity living, CompoundTag data) {
        if (living instanceof Mob mob && data.getBoolean(CULTIVATOR)
                && (!data.getString(ART_NAME).isEmpty() || !data.getString(SPELL_NAME).isEmpty())) {
            mob.goalSelector.addGoal(2, new CultivatorSpellGoal(mob));
        }
    }

    

    
    private static NPCTemplate getNPCTemplateForEntity(EntityType<?> entityType) {
        return ENTITY_TYPE_CACHE.computeIfAbsent(entityType, AutoGenerationEvents::findMatchingNPCTemplate);
    }

    
    private static NPCTemplate findMatchingNPCTemplate(EntityType<?> entityType) {
        String entityId = BuiltInRegistries.ENTITY_TYPE.getKey(entityType).toString();

        
        for (NPCTemplate template : ENGINE.getRegistry().getAllNPCs()) {
            if (template.entityType().equals(entityId)) {
                return template;
            }
        }

        
        String baseType = entityId.split(":")[1].split("_")[0];
        for (NPCTemplate template : ENGINE.getRegistry().getAllNPCs()) {
            if (template.entityType().contains(baseType)) {
                return template;
            }
        }

        
        return ENGINE.getRegistry().getNPC("npc:rogue:casual");
    }

    
    private static double calculateJadeSlipDropChance(NPCTemplate template, LivingEntity victim, Entity killer) {
        double baseChance = GenerationConfig.getJadeSlipBaseDropChance();
        double rarityMultiplier = GenerationConfig.getJadeSlipMultiplier(template.rarity());

        
        int realmLevel = getRealmLevel(victim);
        double levelMultiplier = 1.0 + realmLevel * 0.05; 

        
        double killerMultiplier = killer instanceof Player ? 1.5 : 1.0;

        
        if (template.lootTemplate().getCustomLootTable() != null && !template.lootTemplate().getCustomLootTable().isEmpty()) {
        }

        return Math.min(1.0, baseChance * rarityMultiplier * levelMultiplier * killerMultiplier);
    }

    
    private static int getRealmLevel(LivingEntity entity) {
        return switch (BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()) {
            case "minecraft:zombie", "minecraft:skeleton" -> RANDOM.nextInt(3) + 1; 
            case "minecraft:creeper" -> RANDOM.nextInt(4) + 2; 
            case "minecraft:enderman" -> RANDOM.nextInt(5) + 4; 
            case "minecraft:witch" -> RANDOM.nextInt(4) + 3; 
            default -> RANDOM.nextInt(3) + 1;
        };
    }

    
    private static void attachNPCData(LivingEntity entity, StatGenerator.NPCGeneratedStats stats, CompoundTag data) {
        if (!stats.title().isEmpty()) {
            entity.setCustomName(Component.literal("[" + stats.title() + "] " + stats.displayName()));
            entity.setCustomNameVisible(true);
        }

        data.putInt(REALM_LEVEL, stats.realmLevel());
        double artPower = 0;
        if (stats.art() != null) {
            data.putString(ART_NAME, stats.art().name());
            for (double value : stats.art().stats().values()) {
                artPower += Math.abs(value) * 0.002;
            }
        }
        data.putFloat(ART_POWER, (float) Math.min(10.0, artPower));

        double spellPower = 0;
        if (stats.spell() != null) {
            data.putString(SPELL_NAME, stats.spell().name());
            data.putFloat(SPELL_DAMAGE, (float) stats.spell().baseDamage());
            data.putFloat(SPELL_HEAL, (float) stats.spell().baseHeal());
            data.putInt(SPELL_COOLDOWN, stats.spell().cooldown());
            String category = stats.spell().category();
            String basicType = switch (category) {
                case "HEAL", "DEFENSE", "MOVEMENT" -> "SELF";
                default -> stats.spell().maxTargets() > 1 ? "AOE" : "PROJECTLE";
            };
            data.putString(SPELL_CATEGORY, basicType);
            spellPower = 0.15 + stats.spell().baseDamage() * 0.01
                    + stats.spell().baseHeal() * 0.008 + stats.spell().maxTargets() * 0.03;
        }

        double realmPower = stats.realmLevel() * 0.18;
        double totalPower = realmPower + artPower * 0.03 + spellPower;
        if (stats.art() != null) totalPower += 0.15;
        multiplyAttribute(entity, Attributes.MAX_HEALTH, totalPower);
        multiplyAttribute(entity, Attributes.ATTACK_DAMAGE, totalPower * 0.65);
        multiplyAttribute(entity, Attributes.MOVEMENT_SPEED, Math.min(0.35, totalPower * 0.06));
        addAttribute(entity, Attributes.ARMOR, stats.realmLevel() * 0.5 + spellPower * 2);
        entity.setHealth(entity.getMaxHealth());
    }

    private static void multiplyAttribute(LivingEntity entity, Holder<Attribute> attribute, double multiplier) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(Math.max(0.01, instance.getBaseValue() * (1.0 + multiplier)));
        }
    }

    private static void addAttribute(LivingEntity entity, Holder<Attribute> attribute, double amount) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(Math.max(0.0, instance.getBaseValue() + amount));
        }
    }
}
