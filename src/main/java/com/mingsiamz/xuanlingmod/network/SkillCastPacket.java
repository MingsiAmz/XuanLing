package com.mingsiamz.xuanlingmod.network;

import java.util.ArrayList;
import java.util.List;

import com.mingsiamz.xuanlingmod.XuanLing;
import com.mingsiamz.xuanlingmod.data.SpellComponent;
import com.mingsiamz.xuanlingmod.type.XuanLingTypeEnum;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;


public class SkillCastPacket extends CustomPackageImp {

        public static final CustomPacketPayload.Type<SkillCastPacket> TYPE = reType("skill_cast_packet");
        public static final StreamCodec<RegistryFriendlyByteBuf, SkillCastPacket> CODEC = StreamCodec
                        .ofMember(SkillCastPacket::encode, SkillCastPacket::new);

        private String combo = "";

        public SkillCastPacket() {
        }

        public SkillCastPacket(String combo) {
                this.combo = combo;
        }

        public SkillCastPacket(RegistryFriendlyByteBuf buf) {
                this.combo = buf.readUtf(64);
        }

        public String getCombo() {
                return combo;
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
                return TYPE;
        }

        @Override
        void serverRun(MinecraftServer server, LivingEntity entity) {
                super.serverRun(server, entity);
                if (entity == null || this.combo.isEmpty())
                        return;

                CompoundTag root = entity.getPersistentData();
                CompoundTag spells = root.getCompound(XuanLing.MODID + XuanLingTypeEnum.SPELL.type);
                CompoundTag binds = root.getCompound(BindSkillPacket.BIND_KEY);

                List<String> matched = new ArrayList<>();
                for (String name : binds.getAllKeys()) {
                        if (spells.contains(name) && this.combo.equals(binds.getString(name)))
                                matched.add(name);
                }

                if (matched.isEmpty()) {
                        XuanLing.LOGGER.info("[battle] {} combo {} matched no spell",
                                        entity.getName().getString(), this.combo);
                        return;
                }

                if (matched.size() == 1) {
                        cast(entity, matched.get(0));
                        return;
                }

                if (entity instanceof ServerPlayer player)
                        Networking.sendToClient(player, new SpellChoicePacket(matched, this.combo));
        }

        
        public static void cast(LivingEntity entity, String name) {
                SpellComponent spell = SpellComponent.load(entity.getStringUUID(), name);
                if (spell == null) {
                        XuanLing.LOGGER.warn("[battle] {} failed to load spell '{}'",
                                        entity.getName().getString(), name);
                        return;
                }
                XuanLing.LOGGER.info("[battle] {} cast spell {}", entity.getName().getString(), name);
                spell.run();
        }

        @Override
        void encode(RegistryFriendlyByteBuf buf) {
                buf.writeUtf(this.combo, 64);
        }

}
