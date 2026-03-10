package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import net.jodah.typetools.TypeResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.Identifier;
import org.mesdag.portlib.PortLib;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class NetworkHandler {
    private final SimpleChannel channel;
    private static final Map<Identifier, PacketCodec<?, ?>> codecMap = new HashMap<>();
    private int packetId;

    public NetworkHandler(String namespace, String version) {
        this.channel = NetworkRegistry.newSimpleChannel(
                PortLib.identifier(namespace, "main"),
                () -> version,
                version::equals,
                version::equals
        );

        register(BundledPacket.IDENTIFIER, BundledPacket.PACKET_CODEC, (p, s) -> {
            switch (s.get().getDirection()) {
                case PLAY_TO_CLIENT, LOGIN_TO_CLIENT -> s2c(p, s, BundledPacket::handle);
                case PLAY_TO_SERVER, LOGIN_TO_SERVER -> c2s(p, s, BundledPacket::handle);
            }
        }, null);
    }

    public <P extends IPacket.S2C, B extends ByteBuf> void registerInGameS2C(Identifier identifier, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        register(identifier, codec, (p, s) -> s2c(p, s, handler), PacketDirection.PLAY_TO_CLIENT);
    }

    public <P extends IPacket.C2S, B extends ByteBuf> void registerInGameC2S(Identifier identifier, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        register(identifier, codec, (p, s) -> c2s(p, s, handler), PacketDirection.PLAY_TO_SERVER);
    }

    public <P extends IPacket.S2C, B extends ByteBuf> void registerLoginS2C(Identifier identifier, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        register(identifier, codec, (p, s) -> s2c(p, s, handler), PacketDirection.LOGIN_TO_CLIENT);
    }

    public <P extends IPacket.C2S, B extends ByteBuf> void registerLoginC2S(Identifier identifier, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        register(identifier, codec, (p, s) -> c2s(p, s, handler), PacketDirection.LOGIN_TO_SERVER);
    }

    private static <P extends IPacket> void s2c(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPacket.Context> handler) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            handler.accept(p, new IPacket.Context(Minecraft.getInstance().player));
            s.get().setPacketHandled(true);
        });
    }

    private static <P extends IPacket> void c2s(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPacket.Context> handler) {
        handler.accept(p, new IPacket.Context(s.get().getSender()));
        s.get().setPacketHandled(true);
    }

    @SuppressWarnings("unchecked")
    private <P extends IPacket, B extends ByteBuf> void register(Identifier identifier, PacketCodec<P, ? super B> codec, BiConsumer<P, Supplier<NetworkEvent.Context>> handler, @Nullable PacketDirection direction) {
        Class<?>[] classes = TypeResolver.resolveRawArguments(BiConsumer.class, handler.getClass());
        Class<?> packetClass = classes[0];
        if (packetClass != TypeResolver.Unknown.class) {
            channel.registerMessage(
                    packetId++, (Class<P>) packetClass,
                    (p, b) -> codec.encode(p, (B) b), b -> codec.decode((B) b),
                    handler, direction == null ? Optional.empty() : Optional.of(direction.unwrap()));
            codecMap.put(identifier, codec);
        }
    }

    public void sendToServer(IPacket.C2S packet, IPacket.C2S... packets) {
        channel.sendToServer(makePacket(packet, packets));
    }

    public void sendToPlayer(ServerPlayer player, IPacket.S2C packet, IPacket.S2C... packets) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), makePacket(packet, packets));
    }

    public void sendToPlayersInDimension(ResourceKey<Level> dimension, IPacket.S2C packet, IPacket.S2C... packets) {
        channel.send(PacketDistributor.DIMENSION.with(() -> dimension), makePacket(packet, packets));
    }

    public void sendToPlayersNear(ResourceKey<Level> dimension, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, IPacket.S2C packet, IPacket.S2C... packets) {
        channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(excluded, x, y, z, radius, dimension)), makePacket(packet, packets));
    }

    public void sendToAllPlayers(IPacket.S2C packet, IPacket.S2C... packets) {
        channel.send(PacketDistributor.ALL.noArg(), makePacket(packet, packets));
    }

    public void sendToPlayersTrackingEntity(Entity entity, IPacket.S2C packet, IPacket.S2C... packets) {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), makePacket(packet, packets));
    }

    public void sendToPlayersTrackingEntityAndSelf(Entity entity, IPacket.S2C packet, IPacket.S2C... packets) {
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), makePacket(packet, packets));
    }

    public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, IPacket.S2C packet, IPacket.S2C... packets) {
        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunk(pos.x, pos.z)), makePacket(packet, packets));
    }

    private static IPacket makePacket(IPacket packet, IPacket... packets) {
        if (packets.length > 0) {
            LinkedHashMap<String, LinkedHashMap<String, IPacket>> map = new LinkedHashMap<>();
            LinkedHashMap<String, IPacket> value = new LinkedHashMap<>();
            value.put(packet.identifier().getPath(), packet);
            map.put(packet.identifier().getNamespace(), value);
            for (IPacket iPacket : packets) {
                map.computeIfAbsent(iPacket.identifier().getNamespace(), s -> new LinkedHashMap<>()).put(iPacket.identifier().getPath(), iPacket);
            }
            return new BundledPacket(map);
        }
        return packet;
    }

    @SuppressWarnings("rawtypes")
    static PacketCodec getPacketCodec(Identifier identifier) {
        PacketCodec codec = codecMap.get(identifier);
        if (codec == null) {
            throw new IllegalStateException("Packet not registered: " + identifier);
        }
        return codec;
    }

    public interface PacketCodec<P extends IPacket, B extends ByteBuf> {
        void encode(P packet, B buffer);

        P decode(B buffer);
    }

    public enum PacketDirection {
        PLAY_TO_SERVER,
        PLAY_TO_CLIENT,
        LOGIN_TO_SERVER,
        LOGIN_TO_CLIENT;

        NetworkDirection unwrap() {
            return switch (this) {
                case PLAY_TO_SERVER -> NetworkDirection.PLAY_TO_SERVER;
                case PLAY_TO_CLIENT -> NetworkDirection.PLAY_TO_CLIENT;
                case LOGIN_TO_SERVER -> NetworkDirection.LOGIN_TO_SERVER;
                case LOGIN_TO_CLIENT -> NetworkDirection.LOGIN_TO_CLIENT;
            };
        }
    }
}
