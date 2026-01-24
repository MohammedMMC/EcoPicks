package dev.moma.ecopicks.screen.custom;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record LeafeeScreenData(int entityId) {
    public static final PacketCodec<RegistryByteBuf, LeafeeScreenData> PACKET_CODEC =
        PacketCodec.tuple(PacketCodecs.VAR_INT, LeafeeScreenData::entityId, LeafeeScreenData::new);
}