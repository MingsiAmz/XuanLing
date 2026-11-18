package com.mingsiamz.xuanlingmod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {
        @SubscribeEvent
        public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
                PayloadRegistrar reg = event.registrar("1");

                reg.playToServer(
                                MeditationPacket.TYPE,
                                MeditationPacket.CODEC,
                                Networking::handler);

                reg.playToServer(
                                ComprehensionPacket.TYPE,
                                ComprehensionPacket.CODEC,
                                Networking::handler);

                reg.playToServer(
                                CreateDataPacket.TYPE,
                                CreateDataPacket.CODEC,
                                Networking::handler);

                reg.playToServer(
                                SkillCastPacket.TYPE,
                                SkillCastPacket.CODEC,
                                Networking::handler);

                reg.playToServer(
                                BindSkillPacket.TYPE,
                                BindSkillPacket.CODEC,
                                Networking::handler);

                reg.playToServer(
                                CastSpellPacket.TYPE,
                                CastSpellPacket.CODEC,
                                Networking::handler);

                reg.playToServer(
                                ManageDataPacket.TYPE,
                                ManageDataPacket.CODEC,
                                Networking::handler);

                reg.playToClient(
                                SpellChoicePacket.TYPE,
                                SpellChoicePacket.CODEC,
                                Networking::handler);

                reg.playToClient(
                                SyncDataPacket.TYPE,
                                SyncDataPacket.CODEC,
                                Networking::handler);

                reg.playToClient(
                                SkillFxPacket.TYPE,
                                SkillFxPacket.CODEC,
                                Networking::handler);
        }

        public static <T extends CustomPackageImp> void handler(T t, IPayloadContext ctx) {
                ctx.enqueueWork(() -> {
                        if (ctx.flow().isClientbound()) {
                                t.clientRun(Minecraft.getInstance());
                        } else {
                                t.serverRun(ctx.player().getServer(), ctx.player());
                        }
                });
        }

        public static <T extends CustomPackageImp> void sendToClient(ServerPlayer player, T t) {
                PacketDistributor.sendToPlayer(player, t);
        }

        public static <T extends CustomPackageImp> void sendToServer(T t) {
                PacketDistributor.sendToServer(t);
        }

        public static void syncPersistentData(ServerPlayer player, String key) {
                if (player == null || key == null || key.isEmpty())
                        return;
                CompoundTag data = player.getPersistentData().getCompound(key);
                sendToClient(player, new SyncDataPacket(key, data));
        }
}
