package org.mesdag.portlib.diff;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * 将多个 PortLib 业务包按发送顺序合并为一次 Forge 消息。
 *
 * <p>合包使用列表保存成员，同一种消息可以在一次发送中出现多次。编码和解码阶段都会限制
 * 消息总数与标识长度，执行阶段则通过网络注册表调用对应处理器并复核消息方向。</p>
 */
@Diff
public record PortBundledPacket(List<IPortPacket> packets) implements IPortPacket {
    private static final int MAX_PACKETS = 256;
    private static final int MAX_IDENTIFIER_PART_LENGTH = 64;
    public static final ResourceLocation IDENTIFIER =
            ResourceLocation.fromNamespaceAndPath("portlib", "bundled");
    public static final PortStreamCodec<FriendlyByteBuf, PortBundledPacket> PACKET_CODEC =
            new PortStreamCodec<>() {
                @Override
                public void encode(FriendlyByteBuf buffer, PortBundledPacket packet) {
                    validatePacketCount(packet.packets.size());
                    buffer.writeVarInt(packet.packets.size());
                    for (IPortPacket member : packet.packets) {
                        validateMember(member);
                        ResourceLocation identifier = member.identifier();
                        buffer.writeUtf(
                                identifier.getNamespace(),
                                MAX_IDENTIFIER_PART_LENGTH);
                        buffer.writeUtf(
                                identifier.getPath(),
                                MAX_IDENTIFIER_PART_LENGTH);
                        PortNetworkHandler.getPacketCodec(identifier)
                                .encode(buffer, member);
                    }
                }

                @Override
                public PortBundledPacket decode(FriendlyByteBuf buffer) {
                    int packetCount = buffer.readVarInt();
                    validatePacketCount(packetCount);
                    List<IPortPacket> packets =
                            new ArrayList<>(packetCount);
                    for (int index = 0; index < packetCount; index++) {
                        String namespace =
                                buffer.readUtf(MAX_IDENTIFIER_PART_LENGTH);
                        String path =
                                buffer.readUtf(MAX_IDENTIFIER_PART_LENGTH);
                        ResourceLocation identifier =
                                ResourceLocation.fromNamespaceAndPath(
                                        namespace, path);
                        if (IDENTIFIER.equals(identifier)) {
                            throw new IllegalArgumentException(
                                    "Nested bundled packets are not allowed");
                        }
                        packets.add(PortNetworkHandler
                                .getPacketCodec(identifier)
                                .decode(buffer));
                    }
                    return new PortBundledPacket(packets);
                }
            };

    public PortBundledPacket {
        packets = List.copyOf(packets);
    }

    @Override
    public void handle(Context context) {
        for (IPortPacket packet : packets) {
            if (!PortNetworkHandler.isPacketAllowed(
                    packet.identifier(), context.isServerbound())) {
                context.disconnect(Component.literal(
                        "PortLib rejected a bundled packet with an invalid direction: "
                                + packet.identifier()));
                return;
            }
            PortNetworkHandler.handleBundledPacket(packet, context);
        }
    }

    @Override
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }

    public static IPortPacket makePacket(
            IPortPacket packet,
            IPortPacket... packets
    ) {
        if (packets.length == 0) {
            return packet;
        }
        List<IPortPacket> bundled =
                new ArrayList<>(packets.length + 1);
        appendFlattened(bundled, packet);
        for (IPortPacket member : packets) {
            appendFlattened(bundled, member);
        }
        return bundled.size() == 1
                ? bundled.get(0)
                : new PortBundledPacket(bundled);
    }

    public static IPortPacket makePacket(List<IPortPacket> packets) {
        List<IPortPacket> bundled = new ArrayList<>(packets.size());
        for (IPortPacket packet : packets) {
            appendFlattened(bundled, packet);
        }
        return bundled.size() == 1
                ? bundled.get(0)
                : new PortBundledPacket(bundled);
    }

    private static void appendFlattened(
            List<IPortPacket> destination,
            IPortPacket packet
    ) {
        if (packet instanceof PortBundledPacket bundle) {
            destination.addAll(bundle.packets);
        } else {
            destination.add(packet);
        }
        validatePacketCount(destination.size());
    }

    private static void validatePacketCount(int count) {
        if (count < 0 || count > MAX_PACKETS) {
            throw new IllegalArgumentException(
                    "Invalid bundled packet count: " + count);
        }
    }

    private static void validateMember(IPortPacket packet) {
        if (packet instanceof PortBundledPacket
                || IDENTIFIER.equals(packet.identifier())) {
            throw new IllegalArgumentException(
                    "Nested bundled packets are not allowed");
        }
        ResourceLocation identifier = packet.identifier();
        if (identifier.getNamespace().length()
                        > MAX_IDENTIFIER_PART_LENGTH
                || identifier.getPath().length()
                        > MAX_IDENTIFIER_PART_LENGTH) {
            throw new IllegalArgumentException(
                    "Bundled packet identifier is too long: "
                            + identifier);
        }
    }
}
