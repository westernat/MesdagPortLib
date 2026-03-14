package org.mesdag.portlib.network;

import net.jodah.typetools.TypeResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortBundledPacket;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("all")
public class PortNetworkHandler {
    private final SimpleChannel channel;
    private static final Map<PortIdentifier, PortStreamCodec<?, ?>> codecMap = new HashMap<>();
    private int packetId;

    public PortNetworkHandler(String namespace, String version) {
        this.channel = NetworkRegistry.newSimpleChannel(
                PortIdentifier.fromNamespaceAndPath(namespace, "main"),
                () -> version,
                version::equals,
                version::equals
        );

        register(PortBundledPacket.IDENTIFIER, PortBundledPacket.PACKET_CODEC, (p, s) -> {
            switch (s.get().getDirection()) {
                case PLAY_TO_CLIENT, LOGIN_TO_CLIENT -> s2c(p, s, PortBundledPacket::handle);
                case PLAY_TO_SERVER, LOGIN_TO_SERVER -> c2s(p, s, PortBundledPacket::handle);
            }
        }, null);
    }

    public <P extends IPortPacket.S2C> void registerInGameS2C(PortIdentifier identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> s2c(p, s, handler), PortNetworkDirection.PLAY_TO_CLIENT);
    }

    public <P extends IPortPacket.C2S> void registerInGameC2S(PortIdentifier identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> c2s(p, s, handler), PortNetworkDirection.PLAY_TO_SERVER);
    }

    public <P extends IPortPacket.S2C> void registerLoginS2C(PortIdentifier identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, (p, s) -> s2c(p, s, handler), PortNetworkDirection.LOGIN_TO_CLIENT);
    }

    public <P extends IPortPacket.C2S> void registerLoginC2S(PortIdentifier identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, (p, s) -> c2s(p, s, handler), PortNetworkDirection.LOGIN_TO_SERVER);
    }

    private static <P extends IPortPacket> void s2c(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            handler.accept(p, new IPortPacket.Context(Minecraft.getInstance().player));
            s.get().setPacketHandled(true);
        });
    }

    private static <P extends IPortPacket> void c2s(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler) {
        handler.accept(p, new IPortPacket.Context(s.get().getSender()));
        s.get().setPacketHandled(true);
    }

    private <P extends IPortPacket> void register(PortIdentifier identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, Supplier<NetworkEvent.Context>> handler, @Nullable PortNetworkDirection direction) {
        Class<?>[] classes = TypeResolver.resolveRawArguments(BiConsumer.class, handler.getClass());
        Class<?> packetClass = classes[0];
        if (packetClass != TypeResolver.Unknown.class) {
            channel.registerMessage(
                    packetId++, (Class<P>) packetClass,
                    (p, b) -> codec.encode((FriendlyByteBuf) b, p), b -> codec.decode((FriendlyByteBuf) b),
                    handler, direction == null ? Optional.empty() : Optional.of(direction.unwrap()));
            codecMap.put(identifier, codec);
        }
    }

    public void sendToServer(IPortPacket.C2S packet, IPortPacket.C2S... packets) {
        channel.sendToServer(PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayer(ServerPlayer player, IPortPacket packet, IPortPacket... packets) {
        channel.send(PacketDistributor.PLAYER.with(() -> player), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersInDimension(ResourceKey<Level> dimension, IPortPacket packet, IPortPacket... packets) {
        channel.send(PacketDistributor.DIMENSION.with(() -> dimension), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersNear(ResourceKey<Level> dimension, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, IPortPacket packet, IPortPacket... packets) {
        channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(excluded, x, y, z, radius, dimension)), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToAllPlayers(IPortPacket packet, IPortPacket... packets) {
        channel.send(PacketDistributor.ALL.noArg(), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersTrackingEntity(Entity entity, IPortPacket packet, IPortPacket... packets) {
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersTrackingEntityAndSelf(Entity entity, IPortPacket packet, IPortPacket... packets) {
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, IPortPacket packet, IPortPacket... packets) {
        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunk(pos.x, pos.z)), PortBundledPacket.makePacket(packet, packets));
    }

    @Diff
    public static <P extends IPortPacket> PortStreamCodec<? super FriendlyByteBuf, P> getPacketCodec(PortIdentifier identifier) {
        PortStreamCodec codec = codecMap.get(identifier);
        if (codec == null) {
            throw new IllegalStateException("Packet not registered: " + identifier);
        }
        return codec;
    }
}
