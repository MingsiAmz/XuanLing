package com.mingsiamz.xuanlingmod.network;

import net.minecraft.client.Minecraft;
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
                // reg.playToClient(
                //                    SyncDataPacket.TYPE,
                //                    SyncDataPacket.CODEC,
                //                    Networking::handler);

                reg.playToServer(
                                 MeditationPacket.TYPE,
                                 MeditationPacket.CODEC,
                                 Networking::handler);
        }

        public static <T extends CustomPackageImp> void handler(T t, IPayloadContext ctx) {
                if (ctx.flow().isClientbound()) {
                        t.clientRun(Minecraft.getInstance());
                } else {
                        t.serverRun(ctx.player().getServer(), ctx.player());
                }
        }

        public static <T extends CustomPackageImp> void sendToClient(ServerPlayer player, T t) {
                PacketDistributor.sendToPlayer(player, t);
        }

        public static <T extends CustomPackageImp> void sendToServer(T t) {
                PacketDistributor.sendToServer(t);
        }
}
