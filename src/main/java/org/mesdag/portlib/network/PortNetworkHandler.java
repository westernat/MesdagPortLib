package org.mesdag.portlib.network;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
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
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortBundledPacket;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.extensions.IPortFriendlyByteBufExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PortNetworkHandler {
    private final ResourceLocation channelName;
    private final SimpleChannel channel;
    private static final Map<ResourceLocation, PortStreamCodec<?, ?>> codecMap = new HashMap<>();
    private int packetId;

    /// 务必在类初始化时创建
    public PortNetworkHandler(String namespace, String version) {
        this.channelName = ResourceLocation.fromNamespaceAndPath(namespace, "main");
        this.channel = NetworkRegistry.newSimpleChannel(
                channelName,
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
    }

    public <P extends IPortPacket.S2C> void registerInGameS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> s2c(p, s, handler), clazz, PortNetworkDirection.PLAY_TO_CLIENT);
    }

    public <P extends IPortPacket.S2C> void registerInGameS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameS2C(clazz, identifier, codec, IPortPacket.S2C::handle);
    }

    public <P extends IPortPacket.C2S> void registerInGameC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> c2s(p, s, handler), clazz, PortNetworkDirection.PLAY_TO_SERVER);
    }

    public <P extends IPortPacket.C2S> void registerInGameC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameC2S(clazz, identifier, codec, IPortPacket.C2S::handle);
    }

    /// 登录期（LOGIN 协议内、进世界之前）方向：与 registerLoginS2C/registerLoginC2S 配套的是
    /// {@link #sendLoginToClient(Connection, int, IPortPacket)} 走 fml:loginwrapper 信封发送。
    public <P extends IPortPacket.S2C> void registerLoginS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, (p, s) -> s2c(p, s, handler), clazz, PortNetworkDirection.LOGIN_TO_CLIENT);
    }

    public <P extends IPortPacket.S2C> void registerLoginS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec) {
        registerLoginS2C(clazz, identifier, codec, IPortPacket.S2C::handle);
    }

    public <P extends IPortPacket.C2S> void registerLoginC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, codec, (p, s) -> c2s(p, s, handler), clazz, PortNetworkDirection.LOGIN_TO_SERVER);
    }

    public <P extends IPortPacket.C2S> void registerLoginC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec) {
        registerLoginC2S(clazz, identifier, codec, IPortPacket.C2S::handle);
    }

    public ResourceLocation channelName() {
        return channelName;
    }

    /// 在 LOGIN 阶段向对端发送一个 PortLib 消息。
    ///
    /// 1.20.1 的登录协议只有 vanilla 的 custom query（transactionId 回执）而没有“频道回执”，
    /// Forge 的做法是把所有登录期消息装进 `fml:loginwrapper` 信封（内层 = \[目标频道\]\[长度\]\[载荷\]），
    /// 由 [net.minecraftforge.network.LoginWrapper] 在收包侧拆开并按内层频道分发。这里复刻同一格式。
    ///
    /// @param manager  目标连接（处于 LOGIN 协议状态）
    /// @param sequence 自增序号（写入 vanilla transactionId）
    /// @param packet   要发送的 PortLib 消息
    public void sendLoginToClient(Connection manager, int sequence, IPortPacket packet) {
        Packet<?> direct = channel.toVanillaPacket(packet, NetworkDirection.LOGIN_TO_CLIENT);
        if (!(direct instanceof ICustomPacket<?> custom)) {
            throw new IllegalStateException("Message is not wrapped into a login custom query: " + packet.getClass());
        }
        FriendlyByteBuf data = custom.getInternalData();
        if (data == null) {
            throw new IllegalStateException("Login packet has no data: " + packet.getClass());
        }
        FriendlyByteBuf envelope = new FriendlyByteBuf(Unpooled.buffer());
        envelope.writeResourceLocation(channelName);
        envelope.writeVarInt(data.readableBytes());
        envelope.writeBytes(data, data.readerIndex(), data.readableBytes());
        manager.send(NetworkDirection.LOGIN_TO_CLIENT.buildPacket(Pair.of(envelope, sequence), LoginWrapper.WRAPPER).getThis());
    }

    public <P extends IPortPacket> void registerInGameBidirectional(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> {
            if (s.get().getDirection().getOriginationSide().isServer()) {
                s2c(p, s, handler);
            } else {
                c2s(p, s, handler);
            }
        }, clazz, null);
    }

    public <P extends IPortPacket> void registerInGameBidirectional(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameBidirectional(clazz, identifier, codec, IPortPacket::handle);
    }

    private <P extends IPortPacket> void s2c(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> C.handle(p, s, handler, channel));
    }

    private static class C {
        private static <P extends IPortPacket> void handle(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler, SimpleChannel channel) {
            NetworkEvent.Context context = s.get();
            handler.accept(p, IPortPacket.Context.wrap(Minecraft.getInstance().player, context, channel));
            context.setPacketHandled(true);
        }
    }

    private <P extends IPortPacket> void c2s(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler) {
        NetworkEvent.Context context = s.get();
        handler.accept(p, IPortPacket.Context.wrap(context.getSender(), context, channel));
        context.setPacketHandled(true);
    }

    private <P extends IPortPacket> void register(ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, Supplier<NetworkEvent.Context>> handler, Class<?> packetClass, @Nullable PortNetworkDirection direction) {
        channel.registerMessage(
                packetId++, (Class<P>) packetClass,
                (v, b) -> codec.encode(IPortFriendlyByteBufExtension.of(b).wrap(), v), b -> codec.decode(IPortFriendlyByteBufExtension.of(b).wrap()),
                handler, direction == null ? Optional.empty() : Optional.of(direction.unwrap()));
        codecMap.put(identifier, codec);
    }

    /// s2c
    @Diff
    public Packet<ClientGamePacketListener> toVanillaClientbound(IPortPacket packet) {
        return (Packet<ClientGamePacketListener>) channel.toVanillaPacket(packet, NetworkDirection.PLAY_TO_CLIENT);
    }

    /// c2s
    @Diff
    public Packet<ServerGamePacketListener> toVanillaServerbound(IPortPacket packet) {
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
        // 世界生成线程可能在区块尚未完成时同步数据；这里若调用 getChunk 会等待当前生成任务，
        // 形成工作线程等待自身的死锁。直接读取跟踪者列表不会加载区块，也符合该方法的语义。
        for (ServerPlayer player : level.getChunkSource().chunkMap.getPlayers(pos, false)) {
            sendToPlayer(player, packet, packets);
        }
    }

    @Diff
    public static <P extends IPortPacket> PortStreamCodec<? super FriendlyByteBuf, P> getPacketCodec(ResourceLocation identifier) {
        PortStreamCodec codec = codecMap.get(identifier);
        if (codec == null) {
            throw new IllegalStateException("Packet not registered: " + identifier);
        }
        return codec;
    }
}
