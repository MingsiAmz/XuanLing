package com.mingsiamz.xuanlingmod.network;

import com.mingsiamz.xuanlingmod.XuanLing;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class CustomPackageImp implements CustomPacketPayload {
        void clientRun(Minecraft minecraft) {

        }

        void serverRun(MinecraftServer server, LivingEntity entity) {

        }

        abstract void encode(RegistryFriendlyByteBuf buf);

        static <T extends CustomPackageImp> CustomPacketPayload.Type<T> reType(String id) {
                return new CustomPacketPayload.Type<>(XuanLing.prefix(id));
        }

}
