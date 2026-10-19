package com.mingsiamz.xuanlingmod.register;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.BasicData;
import com.mingsiamz.xuanlingmod.data.DefaultData;
import com.mingsiamz.xuanlingmod.data.DefaultEnergy;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegisterAttachments {
        public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister
                        .create(NeoForgeRegistries.ATTACHMENT_TYPES, XuanLing.MODID);

        public static final DeferredHolder<AttachmentType<?>, AttachmentType<DefaultEnergy>> DEFAULT_ENERGY = ATTACHMENT_TYPES
                        .register("default_energy", () -> AttachmentType.builder(() -> new DefaultEnergy())
                                        .serialize(DefaultEnergy.CODEC).sync(DefaultEnergy.STREAM_CODEC).build());

        public static final DeferredHolder<AttachmentType<?>, AttachmentType<BasicData>> BASIC_DATA = ATTACHMENT_TYPES
                        .register("basic_data",
                                  () -> AttachmentType.builder(() -> BasicData.initData()).serialize(BasicData.CODEC)
                                                        .sync(BasicData.STREAM_CODEC)
                                                        .build());

        public static final DeferredHolder<AttachmentType<?>, AttachmentType<DefaultData>> DEFAULT_DATA = ATTACHMENT_TYPES
                        .register("default_data", () -> AttachmentType.builder(() -> new DefaultData())
                                        .serialize(DefaultData.CODEC).sync(DefaultData.STREAM_CODEC).build());

        public static DefaultData getDefaultData(Entity entity) {
                return entity.getData(DEFAULT_DATA);
        }

        public static BasicData getBasicData(Entity entity) {
                return entity.getData(BASIC_DATA);
        }

        public static DefaultEnergy getDefaultEnergy(Entity entity) {
                return entity.getData(DEFAULT_ENERGY);
        }

        public static void register(IEventBus modBus) {
                ATTACHMENT_TYPES.register(modBus);
        }
}
