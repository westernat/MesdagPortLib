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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.Identifier;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.event.EventHandler;
import org.mesdag.portlib.event.Priority;

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class NetworkHandler {
    private static final List<NetworkHandler> handlers = new ArrayList<>();
    private static final Map<Identifier, CustomPacketPayload.Type<?>> types = new HashMap<>();

    private final String namespace;
    private final String version;
    private EnumMap<PacketDirection, List<Payload<?, ?>>> payloads = new EnumMap<>(PacketDirection.class);

    public NetworkHandler(String namespace, String version) {
        this.namespace = namespace;
        this.version = version;
        handlers.add(this);
    }

    public <P extends IPacket.S2C, B extends ByteBuf> void registerInGameS2C(String path, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        register(path, codec, handler, PacketDirection.PLAY_TO_CLIENT);
    }

    public <P extends IPacket.C2S, B extends ByteBuf> void registerInGameC2S(String path, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        register(path, codec, handler, PacketDirection.PLAY_TO_SERVER);
    }

    public <P extends IPacket.S2C, B extends ByteBuf> void registerLoginS2C(String path, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        register(path, codec, handler, PacketDirection.LOGIN_TO_CLIENT);
    }

    public <P extends IPacket.C2S, B extends ByteBuf> void registerLoginC2S(String path, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        register(path, codec, handler, PacketDirection.LOGIN_TO_SERVER);
    }

    private <P extends IPacket, B extends ByteBuf> void register(String path, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler, PacketDirection direction) {
        payloads.computeIfAbsent(direction, d -> new ArrayList<>()).add(new Payload<>(PortLib.identifier(namespace, path), codec, handler));
    }

    @SuppressWarnings("unchecked")
    static <P extends IPacket> CustomPacketPayload.Type<P> createType(Identifier identifier) {
        return (CustomPacketPayload.Type<P>) types.computeIfAbsent(identifier, CustomPacketPayload.Type::new);
    }

    public void sendToServer(IPacket.C2S packet, IPacket.C2S... packets) {
        PacketDistributor.sendToServer(packet, packets);
    }

    public void sendToPlayer(ServerPlayer player, IPacket.S2C packet, IPacket.S2C... packets) {
        PacketDistributor.sendToPlayer(player, packet, packets);
    }

    public void sendToPlayersInDimension(ResourceKey<Level> dimension, IPacket.S2C packet, IPacket.S2C... packets) {
        PacketDistributor.sendToPlayersInDimension(getLevel(dimension), packet, packets);
    }

    public void sendToPlayersNear(ResourceKey<Level> dimension, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, IPacket.S2C packet, IPacket.S2C... packets) {
        PacketDistributor.sendToPlayersNear(getLevel(dimension), excluded, x, y, z, radius, packet, packets);
    }

    public void sendToAllPlayers(IPacket.S2C packet, IPacket.S2C... packets) {
        PacketDistributor.sendToAllPlayers(packet, packets);
    }

    public void sendToPlayersTrackingEntity(Entity entity, IPacket.S2C packet, IPacket.S2C... packets) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, packet, packets);
    }

    public void sendToPlayersTrackingEntityAndSelf(Entity entity, IPacket.S2C packet, IPacket.S2C... packets) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, packet, packets);
    }

    public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, IPacket.S2C packet, IPacket.S2C... packets) {
        PacketDistributor.sendToPlayersTrackingChunk(level, pos, packet, packets);
    }

    private static ServerLevel getLevel(ResourceKey<Level> dimension) {
        return Objects.requireNonNull(Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client").getLevel(dimension), "Cannot get level '" + dimension.location() + "'");
    }

    @ApiStatus.Internal
    public static void init() {
        EventHandler.addListener(Priority.LOWEST, (RegisterPayloadHandlersEvent event) -> {
            for (NetworkHandler handler : handlers) {
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

    public static <P extends IPacket, B extends ByteBuf> PacketCodec<P, B> toPacketCodec(StreamCodec<B, P> codec) {
        return new PacketStreamCodec<>(codec);
    }

    public interface PacketCodec<P extends IPacket, B extends ByteBuf> {
        void encode(P packet, B buffer);

        P decode(B buffer);
    }

    public enum PacketDirection {
        PLAY_TO_SERVER,
        PLAY_TO_CLIENT,
        LOGIN_TO_SERVER,
        LOGIN_TO_CLIENT
    }

    @SuppressWarnings("unchecked")
    record Payload<P extends IPacket, B extends ByteBuf>(Identifier identifier, PacketCodec<P, ? super B> codec, BiConsumer<P, IPacket.Context> handler) {
        void accept(TriConsumer<CustomPacketPayload.Type<P>, StreamCodec<? super FriendlyByteBuf, P>, IPayloadHandler<P>> consumer) {
            consumer.accept(createType(identifier), (StreamCodec<? super FriendlyByteBuf, P>) toStreamCodec(codec), (p, c) -> handler.accept(p, new IPacket.Context(c.player())));
        }

        static <P extends IPacket, B extends ByteBuf> StreamCodec<B, P> toStreamCodec(PacketCodec<P, B> codec) {
            if (codec instanceof PacketStreamCodec<P, B>(StreamCodec<B, P> streamCodec)) {
                return streamCodec;
            }
            return new StreamCodec<>() {
                @Override
                public @NotNull P decode(@NotNull B buffer) {
                    return codec.decode(buffer);
                }

                @Override
                public void encode(@NotNull B buffer, @NotNull P value) {
                    codec.encode(value, buffer);
                }
            };
        }
    }

    record PacketStreamCodec<P extends IPacket, B extends ByteBuf>(StreamCodec<B, P> codec) implements PacketCodec<P, B> {
        @Override
        public void encode(P packet, B buffer) {
            codec.encode(buffer, packet);
        }

        @Override
        public P decode(B buffer) {
            return codec.decode(buffer);
        }
    }
}
