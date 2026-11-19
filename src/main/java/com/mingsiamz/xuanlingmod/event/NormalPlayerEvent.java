package com.mingsiamz.xuanlingmod.event;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.joml.Vector3f;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;
import com.mingsiamz.xuanlingmod.data.component.ComponentEnergy.Type;
import com.mingsiamz.xuanlingmod.gui.ChangeGui;
import com.mingsiamz.xuanlingmod.gui.JadeSlipStorageGui;
import com.mingsiamz.xuanlingmod.items.JadeSlipItem;
import com.mingsiamz.xuanlingmod.register.RegisterAttachments;
import com.mingsiamz.xuanlingmod.register.RegisterBlocks;
import com.mingsiamz.xuanlingmod.register.RegisterEffects;
import com.mingsiamz.xuanlingmod.register.RegisterItems;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = XuanLing.MODID, value = Dist.CLIENT)
public class NormalPlayerEvent {
        @SubscribeEvent
        public static void playerJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
                Player player = event.getEntity();
                if (!player.hasData(RegisterAttachments.BASIC_DATA))
                        player.setData(RegisterAttachments.BASIC_DATA, new BasicData());
                if (!player.hasData(RegisterAttachments.DEFAULT_DATA))
                        player.setData(RegisterAttachments.DEFAULT_DATA, new DefaultData());
                if (!player.hasData(RegisterAttachments.DEFAULT_ENERGY))
                        player.setData(RegisterAttachments.DEFAULT_ENERGY, new DefaultEnergy());
        }

        @SubscribeEvent
        public static void playerIsDeath(EntityTickEvent.Post event) {

        }

        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        }

        @SubscribeEvent
        public static void onRightClickItem(RightClickItem event) {
                Player player = event.getEntity();
                Level level = player.level();
                DefaultEnergy energy = RegisterAttachments.getDefaultEnergy(player);
                ItemStack item = player.getItemInHand(event.getHand());
                if (level.isClientSide && item.getItem() == RegisterItems.JADE_SLIP.get()
                                && !JadeSlipItem.hasStoredData(item) && JadeSlipItem.getRealmData(item).isEmpty()) {
                        Minecraft.getInstance().setScreen(new JadeSlipStorageGui(XuanLingTypeEnum.ART));
                        return;
                }

                if (!level.isClientSide) {
                        pushEnergyItem(item, energy);
                }
        }

        @SubscribeEvent
        public static void onRightClickBlock(RightClickBlock event) {
                Level level = event.getLevel();
                Player player = event.getEntity();
                BlockPos blockPos = event.getPos();
                useBodyStale(level, blockPos);
        }

        @SubscribeEvent
        public static void onPlayerTickPre(PlayerTickEvent.Pre event) {
                Player player = event.getEntity();
                Level level = player.level();
                if (!level.isClientSide) {
                        onMeditation(player);
                }
        }

        private static void useBodyStale(Level level, BlockPos pos) {
                BlockState state = level.getBlockState(pos);
                if (state.is(RegisterBlocks.BODY_EX_STALE_BLOCK)) {
                        if (level.isClientSide)
                                Minecraft.getInstance().setScreen(ChangeGui.current());
                }
        }

        public static void pushEnergyItem(ItemStack item, DefaultEnergy energy) {
                if (!(item.getItem() instanceof IEnergyStorage iitem) || energy == null)
                        return;
                if (!iitem.canReceive())
                        return;
                int toPush = (int) energy.getNowValue(Type.PARRENT);
                if (toPush <= 0)
                        return;
                int space = iitem.getMaxEnergyStored() - iitem.getEnergyStored();
                int amount = Math.min(toPush, space);
                if (amount <= 0)
                        return;
                int received = iitem.receiveEnergy(amount, false);
                if (received > 0)
                        energy.setEnergyValue(Type.PARRENT, energy.getNowValue(Type.PARRENT) - received);
        }

        public static void onMeditation(LivingEntity entity) {
                if (!entity.hasData(RegisterAttachments.BASIC_DATA))
                        return;
                BasicData basicData = RegisterAttachments.getBasicData(entity);
                if (basicData.isMeditation()) {
                        MobEffectInstance instanceMeditation = new MobEffectInstance(RegisterEffects.EFFECT_MEDITATION, 20);
                        entity.addEffect(instanceMeditation);
                        if (!basicData.getLastPos().equals(entity.position())) {
                                basicData.setMeditation(false);
                                if (entity.getEffect(RegisterEffects.EFFECT_MEDITATION) != null)
                                        entity.removeEffect(RegisterEffects.EFFECT_MEDITATION);
                        }
                }
        }
}
