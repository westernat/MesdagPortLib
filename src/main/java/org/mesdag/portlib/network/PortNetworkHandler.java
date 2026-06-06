package org.mesdag.portlib.network;

import PortLib.extensions.net.minecraft.network.FriendlyByteBuf.PortFriendlyByteBufExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortBundledPacket;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PortNetworkHandler {
    //    private static final List<PortNetworkHandler> handlers = new ArrayList<>();
    private final SimpleChannel channel;
    private static final Map<ResourceLocation, PortStreamCodec<?, ?>> codecMap = new HashMap<>();
    private int packetId;
//    private final List<Consumer<IPortCustomLoginTask>> tasks = new ArrayList<>();

    /// 务必在类初始化时创建
    public PortNetworkHandler(String namespace, String version) {
        this.channel = NetworkRegistry.newSimpleChannel(
                ResourceLocation.fromNamespaceAndPath(namespace, "main"),
                () -> version,
                version::equals,
                version::equals
        );

        register(PortBundledPacket.IDENTIFIER, PortBundledPacket.PACKET_CODEC, (p, s) -> {
            switch (s.get().getDirection()) {
                case PLAY_TO_CLIENT, LOGIN_TO_CLIENT -> s2c(p, s, PortBundledPacket::handle);
                case PLAY_TO_SERVER, LOGIN_TO_SERVER -> c2s(p, s, PortBundledPacket::handle);
            }
        }, PortBundledPacket.class, null);

//        handlers.add(this);
    }

    public <P extends IPortPacket.S2C> void registerInGameS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> s2c(p, s, handler), clazz, PortNetworkDirection.PLAY_TO_CLIENT);
    }

    public <P extends IPortPacket.S2C> void registerInGameS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> s2c(p, s, IPortPacket.S2C::handle), clazz, PortNetworkDirection.PLAY_TO_CLIENT);
    }

    public <P extends IPortPacket.C2S> void registerInGameC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> c2s(p, s, handler), clazz, PortNetworkDirection.PLAY_TO_SERVER);
    }

    public <P extends IPortPacket.C2S> void registerInGameC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> c2s(p, s, IPortPacket.C2S::handle), clazz, PortNetworkDirection.PLAY_TO_SERVER);
    }

//    public <P extends IPortPacket.S2C> void registerLoginS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
//        register(identifier, codec, (p, s) -> s2c(p, s, handler), clazz, PortNetworkDirection.LOGIN_TO_CLIENT);
//    }
//
//    public <P extends IPortPacket.C2S> void registerLoginC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
//        register(identifier, codec, (p, s) -> c2s(p, s, handler), clazz, PortNetworkDirection.LOGIN_TO_SERVER);
//    }

    public <P extends IPortPacket> void registerInGameBidirectional(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, (p, s) -> s2c(p, s, handler), clazz, PortNetworkDirection.PLAY_TO_CLIENT);
        register(identifier, codec, (p, s) -> c2s(p, s, handler), clazz, PortNetworkDirection.PLAY_TO_SERVER);
    }

    public <P extends IPortPacket> void registerInGameBidirectional(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec) {
        register(identifier, codec, (p, s) -> s2c(p, s, IPortPacket::handle), clazz, PortNetworkDirection.PLAY_TO_CLIENT);
        register(identifier, codec, (p, s) -> c2s(p, s, IPortPacket::handle), clazz, PortNetworkDirection.PLAY_TO_SERVER);
    }

//    public <S2C extends PortLoginPacket & IPortPacket, C2S extends PortLoginPacket & IPortPacket> void addLoginTask(
//            ResourceLocation identifier,
//            Consumer<IPortCustomLoginTask> consumer,
//            ResourceLocation s2cIdentifier,
//            PortStreamCodec<? super FriendlyByteBuf, S2C> s2cCodec,
//            BiConsumer<S2C, IPortPacket.Context> s2cHandler,
//            @Nullable ResourceLocation c2sIdentifier,
//            @Nullable PortStreamCodec<? super FriendlyByteBuf, C2S> c2sCodec,
//            @Nullable BiConsumer<C2S, IPortPacket.Context> c2sHandler
//    ) {
//        boolean noC2S = c2sIdentifier == null;
//        if (noC2S != (c2sHandler == null) || noC2S != (c2sCodec == null)) {
//            throw new IllegalArgumentException("c2sIdentifier, c2sCodec and c2sHandler must be either all null or all non-null");
//        }
//        Class<?> s2cClazz = TypeResolver.resolveRawArguments(BiConsumer.class, s2cHandler.getClass())[0];
//        if (s2cClazz == TypeResolver.Unknown.class) {
//            throw new IllegalStateException("Cannot get class from s2cCodec");
//        } else {
//            SimpleChannel.MessageBuilder<S2C> s2cBuilder = channel.messageBuilder((Class<S2C>) s2cClazz, packetId++, PortNetworkDirection.LOGIN_TO_CLIENT.unwrap());
//            if (noC2S) {
//                s2cBuilder.noResponse();
//            } else {
//                Class<?> c2sClazz = TypeResolver.resolveRawArguments(BiConsumer.class, c2sHandler.getClass())[0];
//                if (c2sClazz == TypeResolver.Unknown.class) {
//                    throw new IllegalStateException("Cannot get class from c2sCodec");
//                } else {
//                    channel.messageBuilder((Class<C2S>) c2sClazz, packetId++, PortNetworkDirection.LOGIN_TO_SERVER.unwrap())
//                            .loginIndex(PortLoginPacket::getLoginIndex, PortLoginPacket::setLoginIndex)
//                            .decoder(c2sCodec::decode).encoder(c2sCodec::reversedEncode)
//                            .consumerNetworkThread((p, s) -> {c2s(p, s, c2sHandler);})
//                            .add();
//                    codecMap.put(c2sIdentifier, c2sCodec);
//                }
//            }
//            s2cBuilder.loginIndex(PortLoginPacket::getLoginIndex, PortLoginPacket::setLoginIndex)
//                    .decoder(s2cCodec::decode).encoder(s2cCodec::reversedEncode)
//                    .consumerNetworkThread((p, s) -> {s2c(p, s, s2cHandler);})
//                    .add();
//            codecMap.put(s2cIdentifier, s2cCodec);
//            tasks.add(consumer);
//        }
//    }
//
//    public <S2C extends PortLoginPacket & IPortPacket.S2C> void addLoginTask(
//            ResourceLocation identifier,
//            Consumer<IPortCustomLoginTask> consumer,
//            ResourceLocation s2cIdentifier,
//            PortStreamCodec<? super FriendlyByteBuf, S2C> s2cCodec,
//            BiConsumer<S2C, IPortPacket.Context> s2cHandler
//    ) {
//        addLoginTask(identifier, consumer, s2cIdentifier, s2cCodec, s2cHandler, null, null, null);
//    }

    private <P extends IPortPacket> void s2c(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            NetworkEvent.Context context = s.get();
            handler.accept(p, IPortPacket.Context.wrap(Minecraft.getInstance().player, context, channel));
            context.setPacketHandled(true);
        });
    }

    private <P extends IPortPacket> void c2s(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler) {
        NetworkEvent.Context context = s.get();
        handler.accept(p, IPortPacket.Context.wrap(context.getSender(), context, channel));
        context.setPacketHandled(true);
    }

    private <P extends IPortPacket> void register(ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, Supplier<NetworkEvent.Context>> handler, Class<?> packetClass, @Nullable PortNetworkDirection direction) {
        channel.registerMessage(
                packetId++, (Class<P>) packetClass,
                (v, b) -> codec.encode(PortFriendlyByteBufExtension.wrap(b), v), b -> codec.decode(PortFriendlyByteBufExtension.wrap(b)),
                handler, direction == null ? Optional.empty() : Optional.of(direction.unwrap()));
        codecMap.put(identifier, codec);
    }

    @Diff
    public Packet<ClientGamePacketListener> toVanillaClientbound(IPortPacket.S2C packet) {
        return (Packet<ClientGamePacketListener>) channel.toVanillaPacket(packet, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Diff
    public Packet<ServerGamePacketListener> toVanillaServerbound(IPortPacket.C2S packet) {
        return (Packet<ServerGamePacketListener>) channel.toVanillaPacket(packet, NetworkDirection.PLAY_TO_SERVER);
    }

    public void sendToServer(IPortPacket packet, IPortPacket... packets) {
        if (packet instanceof IPortPacket.S2C) {
            throw new UnsupportedOperationException();
        }
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
    public static <P extends IPortPacket> PortStreamCodec<? super FriendlyByteBuf, P> getPacketCodec(ResourceLocation identifier) {
        PortStreamCodec codec = codecMap.get(identifier);
        if (codec == null) {
            throw new IllegalStateException("Packet not registered: " + identifier);
        }
        return codec;
    }

    @ApiStatus.Internal
    public static void init() {
//        PortEventHandler.addListener((PlayerNegotiationEvent event) -> {
//            for (PortNetworkHandler handler : handlers) {
//                if (handler.tasks.isEmpty()) continue;
//                IPortCustomLoginTask loginTask = new IPortCustomLoginTask() {
//                    @Override
//                    public Consumer<IPortPacket> sender() {
//                        return p2 -> handler.channel.sendTo(p2, event.getConnection(), NetworkDirection.LOGIN_TO_CLIENT);
//                    }
//
//                    @Override
//                    public void disconnect(Component reason) {
//                        event.getConnection().disconnect(reason);
//                    }
//                };
//                for (Consumer<IPortCustomLoginTask> task : handler.tasks) {
//                    task.accept(loginTask);
//                }
//            }
//        });
    }
}
