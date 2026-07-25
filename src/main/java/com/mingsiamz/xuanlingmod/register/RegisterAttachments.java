package com.mingsiamz.xuanlingmod.register;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.DefaultData;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RegisterAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, XuanLing.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<DefaultData>> DEFAULT_DATA = ATTACHMENT_TYPES.register("default_data", () -> AttachmentType.builder(DefaultData::new).serialize(DefaultData.CODEC).build());

    public static void register(IEventBus modBus) {
	ATTACHMENT_TYPES.register(modBus);
    }
}
