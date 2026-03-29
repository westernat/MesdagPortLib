package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectBooleanPair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.configuration.ServerConfigurationPacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.configuration.ICustomConfigurationTask;
import net.neoforged.neoforge.network.event.RegisterConfigurationTasksEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.network.login.IPortCustomLoginTask;
import org.mesdag.portlib.network.login.PortLoginPacket;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class PortNetworkHandler {
    private static final List<PortNetworkHandler> handlers = new ArrayList<>();
    private final String version;
    private EnumMap<PortNetworkDirection, List<Payload<?, ?>>> payloads = new EnumMap<>(PortNetworkDirection.class);
    private Map<ConfigurationTask.Type, ObjectBooleanPair<Consumer<IPortCustomLoginTask>>> tasks = new HashMap<>();

    public PortNetworkHandler(String namespace, String version) {
        this.version = version;
        handlers.add(this);
    }

    public <B extends ByteBuf, P extends IPortPacket.S2C> void registerInGameS2C(PortIdentifier identifier, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, handler, PortNetworkDirection.PLAY_TO_CLIENT, null);
    }

    public <B extends ByteBuf, P extends IPortPacket.C2S> void registerInGameC2S(PortIdentifier identifier, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, handler, PortNetworkDirection.PLAY_TO_SERVER, null);
    }

    public <B extends ByteBuf, P extends IPortPacket.S2C> void registerLoginS2C(PortIdentifier identifier, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, handler, PortNetworkDirection.LOGIN_TO_CLIENT, null);
    }

    public <B extends ByteBuf, P extends IPortPacket.C2S> void registerLoginC2S(PortIdentifier identifier, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, handler, PortNetworkDirection.LOGIN_TO_SERVER, null);
    }

    public <S2C extends PortLoginPacket & IPortPacket, C2S extends PortLoginPacket & IPortPacket> void addLoginTask(
            ResourceLocation identifier,
            Consumer<IPortCustomLoginTask> consumer,
            PortIdentifier s2cIdentifier,
            PortStreamCodec<? super FriendlyByteBuf, S2C> s2cCodec,
            BiConsumer<S2C, IPortPacket.Context> s2cHandler,
            @Nullable PortIdentifier c2sIdentifier,
            @Nullable PortStreamCodec<? super FriendlyByteBuf, C2S> c2sCodec,
            @Nullable BiConsumer<C2S, IPortPacket.Context> c2sHandler
    ) {
        boolean noC2S = c2sIdentifier == null;
        if (noC2S != (c2sHandler == null) || noC2S != (c2sCodec == null)) {
            throw new IllegalArgumentException("c2sIdentifier, c2sCodec and c2sHandler must be either all null or all non-null");
        }
        ConfigurationTask.Type type = new ConfigurationTask.Type(identifier);
        register(s2cIdentifier, s2cCodec, s2cHandler, PortNetworkDirection.LOGIN_TO_CLIENT, null);
        if (!noC2S) {
            register(c2sIdentifier, c2sCodec, c2sHandler, PortNetworkDirection.LOGIN_TO_SERVER, type);
        }
        tasks.put(type, new ObjectBooleanImmutablePair<>(consumer, noC2S));
    }

    public <S2C extends PortLoginPacket & IPortPacket.S2C> void addLoginTask(
            PortIdentifier identifier,
            Consumer<IPortCustomLoginTask> consumer,
            PortIdentifier s2cIdentifier,
            PortStreamCodec<? super FriendlyByteBuf, S2C> s2cCodec,
            BiConsumer<S2C, IPortPacket.Context> s2cHandler
    ) {
        addLoginTask(identifier, consumer, s2cIdentifier, s2cCodec, s2cHandler, null, null, null);
    }

    private <B extends ByteBuf, P extends IPortPacket> void register(PortIdentifier identifier, PortStreamCodec<? super B, P> codec, BiConsumer<P, IPortPacket.Context> handler, PortNetworkDirection direction, @Nullable ConfigurationTask.Type type) {
        payloads.computeIfAbsent(direction, d -> new ArrayList<>()).add(new Payload<>(identifier, codec, handler, type));
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

    public static void init() {
        PortEventHandler.addListener((RegisterPayloadHandlersEvent event) -> {
            for (PortNetworkHandler handler : handlers) {
                PayloadRegistrar registrar = event.registrar(handler.version);
                for (Map.Entry<PortNetworkDirection, List<Payload<?, ?>>> entry : handler.payloads.entrySet()) {
                    for (Payload<?, ?> payload : entry.getValue()) {
                        switch (entry.getKey()) {
                            case PLAY_TO_SERVER -> payload.acceptPlay(registrar::playToServer);
                            case PLAY_TO_CLIENT -> payload.acceptPlay(registrar::playToClient);
                            case LOGIN_TO_SERVER -> payload.acceptLogin(registrar::configurationToServer);
                            case LOGIN_TO_CLIENT -> payload.acceptLogin(registrar::configurationToClient);
                        }
                    }
                }
                handler.payloads = null;
            }
        });
        PortEventHandler.addListener((RegisterConfigurationTasksEvent event) -> {
            ServerConfigurationPacketListener listener = event.getListener();
            IPortCustomLoginTask loginTask = new IPortCustomLoginTask() {
                @Override
                public Consumer<IPortPacket> sender() {
                    return listener::send;
                }

                @Override
                public void disconnect(Component reason) {
                    listener.disconnect(reason);
                }
            };
            for (PortNetworkHandler handler : handlers) {
                for (Map.Entry<ConfigurationTask.Type, ObjectBooleanPair<Consumer<IPortCustomLoginTask>>> entry : handler.tasks.entrySet()) {
                    event.register(new ICustomConfigurationTask() {
                        @Override
                        public void run(Consumer<CustomPacketPayload> sender) {
                            ObjectBooleanPair<Consumer<IPortCustomLoginTask>> pair = entry.getValue();
                            pair.left().accept(loginTask);
                            if (pair.rightBoolean()) {
                                listener.finishCurrentTask(entry.getKey());
                            }
                        }

                        @Override
                        public Type type() {
                            return entry.getKey();
                        }
                    });
                }
                handler.tasks = null;
            }
        });
    }

    private record Payload<B extends ByteBuf, P extends IPortPacket>(
            PortIdentifier identifier,
            PortStreamCodec<? super B, P> codec,
            BiConsumer<P, IPortPacket.Context> handler,
            @Nullable ConfigurationTask.Type type
    ) {
        private void acceptPlay(TriConsumer<CustomPacketPayload.Type<P>, StreamCodec<? super FriendlyByteBuf, P>, IPayloadHandler<P>> consumer) {
            consumer.accept(identifier.getType(), (StreamCodec<? super FriendlyByteBuf, P>) codec.unwrap(), (p, c) -> handler.accept(p, IPortPacket.Context.wrap(c.player(), c)));
        }

        private void acceptLogin(TriConsumer<CustomPacketPayload.Type<P>, StreamCodec<? super FriendlyByteBuf, P>, IPayloadHandler<P>> consumer) {
            consumer.accept(identifier.getType(), (StreamCodec<? super FriendlyByteBuf, P>) codec.unwrap(), (p, c) -> {
                handler.accept(p, IPortPacket.Context.wrap(null, c));
                if (type != null) {
                    c.finishCurrentTask(type);
                }
            });
        }
    }
}
