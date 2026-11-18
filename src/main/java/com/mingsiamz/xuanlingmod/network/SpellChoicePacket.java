package com.mingsiamz.xuanlingmod.network;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.gui.SpellSelectWheelGui;


public class SpellChoicePacket extends CustomPackageImp {

        private static final String SEP = "\u0001";

        public static final CustomPacketPayload.Type<SpellChoicePacket> TYPE = reType("spell_choice_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, SpellChoicePacket> CODEC = StreamCodec
                        .ofMember(SpellChoicePacket::encode, SpellChoicePacket::new);

        private final List<String> names;
        private final String combo;

        public SpellChoicePacket(List<String> names, String combo) {
                this.names = names;
                this.combo = combo;
        }

        public SpellChoicePacket(RegistryFriendlyByteBuf buf) {
                this.combo = buf.readUtf(16);
                String joined = buf.readUtf(1024);
                this.names = new ArrayList<>();
                for (String s : joined.split(SEP))
                        if (!s.isEmpty())
                                this.names.add(s);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        void clientRun(Minecraft minecraft) {
                super.clientRun(minecraft);
                minecraft.execute(() -> {
                        if (minecraft.player == null)
                                return;
                        minecraft.setScreen(new SpellSelectWheelGui(this.names, this.combo));
                });
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                buf.writeUtf(this.combo == null ? "" : this.combo, 16);
                buf.writeUtf(String.join(SEP, this.names), 1024);
        }

        public List<String> getNames() {
                return names;
        }

        public String getCombo() {
                return combo;
        }
}
