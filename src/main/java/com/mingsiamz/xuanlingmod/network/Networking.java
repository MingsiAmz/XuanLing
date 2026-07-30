package com.mingsiamz.xuanlingmod.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class Networking {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
	PayloadRegistrar reg = event.registrar("1");
	// reg.commonToServer(
	// 		   CreateDataPacket.TYPE,
	// 		   CreateDataPacket.CODEC, handler);
    }

    public static<T extends CustomPackageImp> void handler() {
	
    }
}
