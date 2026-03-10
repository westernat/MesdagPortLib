package org.mesdag.portlib.network;

import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.Identifier;
import org.mesdag.portlib.PortLib;

import java.util.LinkedHashMap;
import java.util.Map;

record BundledPacket(LinkedHashMap<String, LinkedHashMap<String, IPacket>> map) implements IPacket {
    static final Identifier IDENTIFIER = PortLib.identifier(PortLib.MODID, "bundled");
    @SuppressWarnings("unchecked")
    static final NetworkHandler.PacketCodec<BundledPacket, FriendlyByteBuf> PACKET_CODEC = new NetworkHandler.PacketCodec<>() {
        @Override
        public void encode(BundledPacket packet, FriendlyByteBuf buffer) {
            buffer.writeVarInt(packet.map.size());
            for (Map.Entry<String, LinkedHashMap<String, IPacket>> namespace : packet.map.entrySet()) {
                buffer.writeUtf(namespace.getKey());
                buffer.writeVarInt(namespace.getValue().size());
                for (Map.Entry<String, IPacket> path : namespace.getValue().entrySet()) {
                    Identifier identifier = PortLib.identifier(namespace.getKey(), path.getKey());
                    buffer.writeUtf(path.getKey());
                    NetworkHandler.getPacketCodec(identifier).encode(path.getValue(), buffer);
                }
            }
        }

        @Override
        public BundledPacket decode(FriendlyByteBuf buffer) {
            LinkedHashMap<String, LinkedHashMap<String, IPacket>> map = new LinkedHashMap<>();
            int i = buffer.readVarInt();
            for (int j = 0; j < i; j++) {
                String namespace = buffer.readUtf();
                int k = buffer.readVarInt();
                for (int l = 0; l < k; l++) {
                    String path = buffer.readUtf();
                    Identifier identifier = PortLib.identifier(namespace, path);
                    IPacket packet = NetworkHandler.getPacketCodec(identifier).decode(buffer);
                    map.computeIfAbsent(namespace, s -> new LinkedHashMap<>()).put(path, packet);
                }
            }
            return new BundledPacket(map);
        }
    };

    @Override
    public void handle(Context context) {
        map.values().stream().flatMap(map -> map.values().stream()).forEachOrdered(packet -> packet.handle(context));
    }

    @Override
    public Identifier identifier() {
        return IDENTIFIER;
    }
}
