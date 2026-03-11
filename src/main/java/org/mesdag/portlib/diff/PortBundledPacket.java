package org.mesdag.portlib.diff;

import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.wrapper.PortIdentifier;

import java.util.LinkedHashMap;
import java.util.Map;

@Diff
@SuppressWarnings("all")
public record PortBundledPacket(LinkedHashMap<String, LinkedHashMap<String, IPortPacket>> map) implements IPortPacket {
    public static final PortIdentifier IDENTIFIER = PortLib.asResource("bundled");
    public static final PortNetworkHandler.PacketCodec<PortBundledPacket, FriendlyByteBuf> PACKET_CODEC = new PortNetworkHandler.PacketCodec<>() {
        @Override
        public void encode(PortBundledPacket packet, FriendlyByteBuf buffer) {
            buffer.writeVarInt(packet.map.size());
            for (Map.Entry<String, LinkedHashMap<String, IPortPacket>> namespace : packet.map.entrySet()) {
                buffer.writeUtf(namespace.getKey());
                buffer.writeVarInt(namespace.getValue().size());
                for (Map.Entry<String, IPortPacket> path : namespace.getValue().entrySet()) {
                    PortIdentifier identifier = PortIdentifier.fromNamespaceAndPath(namespace.getKey(), path.getKey());
                    buffer.writeUtf(path.getKey());
                    PortNetworkHandler.getPacketCodec(identifier).encode(path.getValue(), buffer);
                }
            }
        }

        @Override
        public PortBundledPacket decode(FriendlyByteBuf buffer) {
            LinkedHashMap<String, LinkedHashMap<String, IPortPacket>> map = new LinkedHashMap<>();
            int i = buffer.readVarInt();
            for (int j = 0; j < i; j++) {
                String namespace = buffer.readUtf();
                int k = buffer.readVarInt();
                for (int l = 0; l < k; l++) {
                    String path = buffer.readUtf();
                    PortIdentifier identifier = PortIdentifier.fromNamespaceAndPath(namespace, path);
                    IPortPacket packet = PortNetworkHandler.getPacketCodec(identifier).decode(buffer);
                    map.computeIfAbsent(namespace, s -> new LinkedHashMap<>()).put(path, packet);
                }
            }
            return new PortBundledPacket(map);
        }
    };

    @Override
    public void handle(Context context) {
        map.values().stream().flatMap(map -> map.values().stream()).forEachOrdered(packet -> packet.handle(context));
    }

    @Override
    public PortIdentifier identifier() {
        return IDENTIFIER;
    }
}
