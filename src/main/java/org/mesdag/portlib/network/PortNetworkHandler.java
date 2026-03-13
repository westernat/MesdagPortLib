package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortPriority;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("all")
public class PortNetworkHandler {
    private static final List<PortNetworkHandler> handlers = new ArrayList<>();

    private final String namespace;
    private final String version;
    private EnumMap<PacketDirection, List<Payload<?, ?>>> payloads = new EnumMap<>(PacketDirection.class);

    public PortNetworkHandler(String namespace, String version) {
        this.namespace = namespace;
        this.version = version;
        handlers.add(this);
    }

    public <B extends ByteBuf, P extends IPortPacket.S2C> void registerInGameS2C(String path, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(path, codec, handler, PacketDirection.PLAY_TO_CLIENT);
    }

    public <B extends ByteBuf, P extends IPortPacket.C2S> void registerInGameC2S(String path, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(path, codec, handler, PacketDirection.PLAY_TO_SERVER);
    }

    public <B extends ByteBuf, P extends IPortPacket.S2C> void registerLoginS2C(String path, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(path, codec, handler, PacketDirection.LOGIN_TO_CLIENT);
    }

    public <B extends ByteBuf, P extends IPortPacket.C2S> void registerLoginC2S(String path, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(path, codec, handler, PacketDirection.LOGIN_TO_SERVER);
    }

    private <B extends ByteBuf, P extends IPortPacket> void register(String path, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler, PacketDirection direction) {
        payloads.computeIfAbsent(direction, d -> new ArrayList<>()).add(new Payload<>(PortIdentifier.fromNamespaceAndPath(namespace, path), codec, handler));
    }

    public void sendToServer(IPortPacket packet, IPortPacket... packets) {
        PacketDistributor.sendToServer(packet, packets);
    }

    public void sendToPlayer(ServerPlayer player, IPortPacket packet, IPortPacket... packets) {
        PacketDistributor.sendToPlayer(player, packet, packets);
    }

    public void sendToPlayersInDimension(ResourceKey<Level> dimension, IPortPacket packet, IPortPacket... packets) {
        PacketDistributor.sendToPlayersInDimension(getLevel(dimension), packet, packets);
    }

    public void sendToPlayersNear(ResourceKey<Level> dimension, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, IPortPacket packet, IPortPacket... packets) {
        PacketDistributor.sendToPlayersNear(getLevel(dimension), excluded, x, y, z, radius, packet, packets);
    }

    public void sendToAllPlayers(IPortPacket packet, IPortPacket... packets) {
        PacketDistributor.sendToAllPlayers(packet, packets);
    }

    public void sendToPlayersTrackingEntity(Entity entity, IPortPacket packet, IPortPacket... packets) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, packet, packets);
    }

    public void sendToPlayersTrackingEntityAndSelf(Entity entity, IPortPacket packet, IPortPacket... packets) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, packet, packets);
    }

    public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, IPortPacket packet, IPortPacket... packets) {
        PacketDistributor.sendToPlayersTrackingChunk(level, pos, packet, packets);
    }

    private static ServerLevel getLevel(ResourceKey<Level> dimension) {
        return Objects.requireNonNull(Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client").getLevel(dimension), "Cannot get level '" + dimension.location() + "'");
    }

    @Diff
    public static void init() {
        PortEventHandler.addListener(PortPriority.LOWEST, (RegisterPayloadHandlersEvent event) -> {
            for (PortNetworkHandler handler : handlers) {
                PayloadRegistrar registrar = event.registrar(handler.version);
                for (Map.Entry<PacketDirection, List<Payload<?, ?>>> entry : handler.payloads.entrySet()) {
                    for (Payload<?, ?> payload : entry.getValue()) {
                        switch (entry.getKey()) {
                            case PLAY_TO_SERVER -> payload.accept(registrar::playToServer);
                            case PLAY_TO_CLIENT -> payload.accept(registrar::playToClient);
                            case LOGIN_TO_SERVER -> payload.accept(registrar::configurationToServer);
                            case LOGIN_TO_CLIENT -> payload.accept(registrar::configurationToClient);
                        }
                    }
                }
                handler.payloads = null;
            }
        });
    }

    private record Payload<B extends ByteBuf, P extends IPortPacket>(PortIdentifier identifier, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        private void accept(TriConsumer<CustomPacketPayload.Type<P>, StreamCodec<? super FriendlyByteBuf, P>, IPayloadHandler<P>> consumer) {
            consumer.accept(identifier.getType(), (StreamCodec<? super FriendlyByteBuf, P>) codec.unwrap(), (p, c) -> handler.accept(p, new IPortPacket.Context(c.player())));
        }
    }
}
