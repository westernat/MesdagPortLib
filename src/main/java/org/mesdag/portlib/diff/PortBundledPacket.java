package org.mesdag.portlib.diff;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Diff
@SuppressWarnings("all")
public record PortBundledPacket(
        LinkedHashMap<String, LinkedHashMap<String, IPortPacket>> map
) implements IPortPacket {
    public static final ResourceLocation IDENTIFIER = ResourceLocation.fromNamespaceAndPath("portlib", "bundled");
    public static final PortStreamCodec<FriendlyByteBuf, PortBundledPacket> PACKET_CODEC = new PortStreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf buffer, PortBundledPacket packet) {
            buffer.writeVarInt(packet.map.size());
            for (Map.Entry<String, LinkedHashMap<String, IPortPacket>> namespace : packet.map.entrySet()) {
                buffer.writeUtf(namespace.getKey());
                buffer.writeVarInt(namespace.getValue().size());
                for (Map.Entry<String, IPortPacket> path : namespace.getValue().entrySet()) {
                    ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(namespace.getKey(), path.getKey());
                    buffer.writeUtf(path.getKey());
                    PortNetworkHandler.getPacketCodec(identifier).encode(buffer, path.getValue());
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
                    ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(namespace, path);
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
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }

    public static IPortPacket makePacket(IPortPacket packet, IPortPacket... packets) {
        if (packets.length > 0) {
            LinkedHashMap<String, LinkedHashMap<String, IPortPacket>> map = new LinkedHashMap<>();
            LinkedHashMap<String, IPortPacket> value = new LinkedHashMap<>();
            value.put(packet.identifier().getPath(), packet);
            map.put(packet.identifier().getNamespace(), value);
            for (IPortPacket iPacket : packets) {
                map.computeIfAbsent(iPacket.identifier().getNamespace(), s -> new LinkedHashMap<>()).put(iPacket.identifier().getPath(), iPacket);
            }
            return new PortBundledPacket(map);
        }
        return packet;
    }

    public static IPortPacket makePacket(List<IPortPacket> packets) {
        LinkedHashMap<String, LinkedHashMap<String, IPortPacket>> map = new LinkedHashMap<>();
        for (IPortPacket iPacket : packets) {
            map.computeIfAbsent(iPacket.identifier().getNamespace(), s -> new LinkedHashMap<>()).put(iPacket.identifier().getPath(), iPacket);
        }
        return new PortBundledPacket(map);
    }
}
