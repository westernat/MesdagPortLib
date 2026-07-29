package org.mesdag.portlib.network;

import PortLib.extensions.net.minecraft.network.FriendlyByteBuf.PortFriendlyByteBufExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Forge 1.20 网络 API 的轻量桥接层。
 *
 * <p>该类只负责不同加载器版本之间的频道注册、编解码、收发目标、方向校验和主线程调度，
 * 不承载 Confluence 的物品、Boss、难度或其他玩法规则。业务包仍由各自模块定义，通用玩法
 * 契约则归 MagicLib 所有。</p>
 *
 * <p>所有公开发送方法都会在发送端验证方向；接收端由 SimpleChannel 验证普通消息，并由
 * {@link PortBundledPacket} 逐项验证合包消息。这样即使客户端手工构造数据，也不能通过
 * 双向外层包执行只注册为 S2C 的业务逻辑。</p>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PortNetworkHandler {
    private static final String BRIDGE_PROTOCOL_VERSION = "2";
    //    private static final List<PortNetworkHandler> handlers = new ArrayList<>();
    private static final Object REGISTRATION_LOCK = new Object();
    private final SimpleChannel channel;
    private static final Map<ResourceLocation, PortStreamCodec<?, ?>> codecMap = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, BiConsumer<IPortPacket, IPortPacket.Context>> handlerMap =
            new ConcurrentHashMap<>();
    /**
     * 合包协议只会由 Forge 校验外层消息的方向，因此还要记录每个内部消息的注册方向。
     * 空 Optional 表示该消息明确注册为双向；键不存在则表示消息从未注册。
     * Forge 可能并行构造多个模组，相关全局索引都必须使用并发容器。
     */
    private static final Map<ResourceLocation, Optional<PortNetworkDirection>> directionMap = new ConcurrentHashMap<>();
    private int packetId;
//    private final List<Consumer<IPortCustomLoginTask>> tasks = new ArrayList<>();

    /// 务必在类初始化时创建
    public PortNetworkHandler(String namespace, String version) {
        String protocolVersion =
                BRIDGE_PROTOCOL_VERSION + ":" + version;
        this.channel = NetworkRegistry.newSimpleChannel(
                ResourceLocation.fromNamespaceAndPath(namespace, "main"),
                () -> protocolVersion,
                protocolVersion::equals,
                protocolVersion::equals
        );

        register(PortBundledPacket.IDENTIFIER, PortBundledPacket.PACKET_CODEC, (p, s) -> {
            switch (s.get().getDirection()) {
                case PLAY_TO_CLIENT, LOGIN_TO_CLIENT -> s2c(p, s, PortBundledPacket::handle);
                case PLAY_TO_SERVER, LOGIN_TO_SERVER -> c2s(p, s, PortBundledPacket::handle);
            }
        }, PortBundledPacket::handle, PortBundledPacket.class, null);

//        handlers.add(this);
    }

    public <P extends IPortPacket.S2C> void registerInGameS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> s2c(p, s, handler), handler, clazz, PortNetworkDirection.PLAY_TO_CLIENT);
    }

    public <P extends IPortPacket.S2C> void registerInGameS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameS2C(clazz, identifier, codec, IPortPacket.S2C::handle);
    }

    public <P extends IPortPacket.C2S> void registerInGameC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> c2s(p, s, handler), handler, clazz, PortNetworkDirection.PLAY_TO_SERVER);
    }

    public <P extends IPortPacket.C2S> void registerInGameC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameC2S(clazz, identifier, codec, IPortPacket.C2S::handle);
    }

//    public <P extends IPortPacket.S2C> void registerLoginS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
//        register(identifier, codec, (p, s) -> s2c(p, s, handler), clazz, PortNetworkDirection.LOGIN_TO_CLIENT);
//    }
//
//    public <P extends IPortPacket.C2S> void registerLoginC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
//        register(identifier, codec, (p, s) -> c2s(p, s, handler), clazz, PortNetworkDirection.LOGIN_TO_SERVER);
//    }

    public <P extends IPortPacket> void registerInGameBidirectional(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> {
            if (s.get().getDirection().getOriginationSide().isServer()) {
                s2c(p, s, handler);
            } else {
                c2s(p, s, handler);
            }
        }, handler, clazz, null);
    }

    public <P extends IPortPacket> void registerInGameBidirectional(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameBidirectional(clazz, identifier, codec, IPortPacket::handle);
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
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> C.handle(p, s, handler, channel));
    }

    private static class C {
        private static <P extends IPortPacket> void handle(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler, SimpleChannel channel) {
            NetworkEvent.Context context = s.get();
            IPortPacket.Context portContext = IPortPacket.Context.wrap(Minecraft.getInstance().player, context, channel);
            // Forge 1.20 在网络线程调用消息消费者。PortLib 在桥接层统一切回游戏主线程，
            // 使业务模块与 NeoForge 新版负载处理器保持相同的线程语义。
            context.enqueueWork(() -> handleDecodedPacket(p, portContext, handler));
            context.setPacketHandled(true);
        }
    }

    private <P extends IPortPacket> void c2s(P p, Supplier<NetworkEvent.Context> s, BiConsumer<P, IPortPacket.Context> handler) {
        NetworkEvent.Context context = s.get();
        IPortPacket.Context portContext = IPortPacket.Context.wrap(context.getSender(), context, channel);
        // 世界、实体、容器和 SavedData 都只能在服务器主线程修改。
        context.enqueueWork(() -> handleDecodedPacket(p, portContext, handler));
        context.setPacketHandled(true);
    }

    static <P extends IPortPacket> void handleDecodedPacket(
            P packet,
            IPortPacket.Context context,
            BiConsumer<P, IPortPacket.Context> handler
    ) {
        if (packet instanceof RejectedDecode rejected) {
            rejected.handle(context);
            return;
        }
        handler.accept(packet, context);
    }

    /**
     * 使用注册时保存的业务处理器执行合包成员。
     *
     * <p>合包成员不会再次经过 Forge 的消息分发器，因此必须在这里恢复普通消息使用的处理器语义。
     * 显式注册的处理器和消息自身的默认处理方法不会再因是否合包而产生差异。</p>
     */
    @ApiStatus.Internal
    public static void handleBundledPacket(
            IPortPacket packet,
            IPortPacket.Context context
    ) {
        BiConsumer<IPortPacket, IPortPacket.Context> handler =
                handlerMap.get(packet.identifier());
        if (handler == null) {
            context.disconnect(Component.literal(
                    "PortLib rejected a bundled packet without a registered handler: "
                            + packet.identifier()));
            return;
        }
        handleDecodedPacket(packet, context, handler);
    }

    private <P extends IPortPacket> void register(
            ResourceLocation identifier,
            PortStreamCodec<? super FriendlyByteBuf, P> codec,
            BiConsumer<P, Supplier<NetworkEvent.Context>> networkHandler,
            BiConsumer<P, IPortPacket.Context> businessHandler,
            Class<?> packetClass,
            @Nullable PortNetworkDirection direction
    ) {
        synchronized (REGISTRATION_LOCK) {
            boolean reserved = reservePacketIdentifier(identifier, codec, direction);
            if (reserved) {
                handlerMap.put(
                        identifier,
                        (BiConsumer<IPortPacket, IPortPacket.Context>) (BiConsumer<?, ?>) businessHandler);
            }
            try {
                channel.registerMessage(
                        packetId, (Class<P>) packetClass,
                        (v, b) -> codec.encode(PortFriendlyByteBufExtension.wrap(b), v),
                        b -> decodeSafely(identifier, codec, PortFriendlyByteBufExtension.wrap(b)),
                        networkHandler, direction == null ? Optional.empty() : Optional.of(direction.unwrap()));
                packetId++;
            } catch (RuntimeException | Error failure) {
                if (reserved) {
                    codecMap.remove(identifier, codec);
                    directionMap.remove(identifier, Optional.ofNullable(direction));
                    handlerMap.remove(identifier);
                }
                throw failure;
            }
        }
    }

    /**
     * 将业务包解码异常转换为可在主线程处理的拒绝消息。
     *
     * <p>Forge 1.20 会把网络线程中逃逸的客户端解码异常交给原版连接处理，而原版处理器会构造
     * 客户端方向的断开包。若异常发生在客户端，该包会反向进入服务端 listener。这里保留诊断，
     * 但把关闭连接延迟到已有的方向感知处理流程中。</p>
     */
    @SuppressWarnings("unchecked")
    static <P extends IPortPacket> P decodeSafely(
            ResourceLocation identifier,
            PortStreamCodec<? super FriendlyByteBuf, P> codec,
            FriendlyByteBuf buffer
    ) {
        try {
            return codec.decode(buffer);
        } catch (RuntimeException exception) {
            String detail = exception.getMessage();
            if (detail == null || detail.isBlank()) {
                detail = exception.getClass().getSimpleName();
            } else if (detail.length() > 256) {
                detail = detail.substring(0, 256);
            }
            return (P) new RejectedDecode(
                    identifier,
                    "PortLib rejected an invalid packet payload: " + identifier + " (" + detail + ")");
        }
    }

    record RejectedDecode(ResourceLocation identifier, String reason) implements IPortPacket {
        @Override
        public void handle(Context context) {
            context.disconnect(Component.literal(reason));
        }
    }

    /**
     * 原子占用全局消息标识，避免后注册的频道悄悄替换旧 codec 或方向。
     *
     * @return 是否由本次调用新增了全局索引；合包 codec 可由多个频道共享
     */
    static boolean reservePacketIdentifier(
            ResourceLocation identifier,
            PortStreamCodec<?, ?> codec,
            @Nullable PortNetworkDirection direction
    ) {
        synchronized (REGISTRATION_LOCK) {
            PortStreamCodec<?, ?> registeredCodec = codecMap.get(identifier);
            boolean sharedBundleRegistration = PortBundledPacket.IDENTIFIER.equals(identifier)
                    && registeredCodec == codec
                    && Optional.empty().equals(directionMap.get(identifier));
            if (registeredCodec != null && !sharedBundleRegistration) {
                throw new IllegalStateException("Duplicate packet identifier: " + identifier);
            }
            if (!sharedBundleRegistration) {
                codecMap.put(identifier, codec);
                directionMap.put(identifier, Optional.ofNullable(direction));
            }
            return !sharedBundleRegistration;
        }
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
        validateOutgoingDirection(true, packet, packets);
        channel.sendToServer(PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayer(ServerPlayer player, IPortPacket packet, IPortPacket... packets) {
        validateOutgoingDirection(false, packet, packets);
        channel.send(PacketDistributor.PLAYER.with(() -> player), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersInDimension(ResourceKey<Level> dimension, IPortPacket packet, IPortPacket... packets) {
        validateOutgoingDirection(false, packet, packets);
        channel.send(PacketDistributor.DIMENSION.with(() -> dimension), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersNear(ResourceKey<Level> dimension, @Nullable ServerPlayer excluded, double x, double y, double z, double radius, IPortPacket packet, IPortPacket... packets) {
        validateOutgoingDirection(false, packet, packets);
        channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(excluded, x, y, z, radius, dimension)), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToAllPlayers(IPortPacket packet, IPortPacket... packets) {
        validateOutgoingDirection(false, packet, packets);
        channel.send(PacketDistributor.ALL.noArg(), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersTrackingEntity(Entity entity, IPortPacket packet, IPortPacket... packets) {
        validateOutgoingDirection(false, packet, packets);
        channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersTrackingEntityAndSelf(Entity entity, IPortPacket packet, IPortPacket... packets) {
        validateOutgoingDirection(false, packet, packets);
        channel.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), PortBundledPacket.makePacket(packet, packets));
    }

    public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, IPortPacket packet, IPortPacket... packets) {
        validateOutgoingDirection(false, packet, packets);
        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunk(pos.x, pos.z)), PortBundledPacket.makePacket(packet, packets));
    }

    /**
     * 在消息离开发送端前校验外层及所有合包成员的方向。
     * 这既能尽早暴露开发期误用，也与接收端的恶意消息防护形成双保险。
     */
    static void validateOutgoingDirection(boolean serverbound, IPortPacket packet, IPortPacket... packets) {
        validatePacketDirection(packet, serverbound, false);
        for (IPortPacket bundled : packets) {
            validatePacketDirection(bundled, serverbound, true);
        }
    }

    private static void validatePacketDirection(
            IPortPacket packet,
            boolean serverbound,
            boolean bundled
    ) {
        if (packet instanceof PortBundledPacket bundle) {
            for (IPortPacket member : bundle.packets()) {
                validatePacketDirection(member, serverbound, true);
            }
            return;
        }
        if (!isPacketAllowed(packet.identifier(), serverbound)) {
            String prefix = bundled ? "Bundled packet" : "Packet";
            throw new IllegalArgumentException(
                    prefix + " registered for the wrong direction: "
                            + packet.identifier());
        }
    }

    /**
     * 校验回复包是否注册为原消息的反方向。
     *
     * <p>{@link SimpleChannel#reply(Object, NetworkEvent.Context)} 会沿用上下文选择目标，
     * 但不会替 PortLib 检查业务消息的注册方向。这里在真正编码前拒绝误用，避免方向错误
     * 进入 Forge 的连接异常处理。</p>
     */
    static void validateReplyDirection(IPortPacket packet, boolean receivedServerbound) {
        try {
            validatePacketDirection(packet, !receivedServerbound, false);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Packet registered for the wrong reply direction: "
                            + packet.identifier(),
                    exception);
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

    /**
     * 判断一个业务包能否沿当前接收方向执行。
     *
     * <p>普通消息由 SimpleChannel 按注册方向过滤；合包内的消息不会再次经过该过滤，必须
     * 显式复核，防止客户端把只应由服务器发送的 S2C 包塞进 C2S 合包。</p>
     */
    @ApiStatus.Internal
    public static boolean isPacketAllowed(ResourceLocation identifier, boolean serverbound) {
        if (!directionMap.containsKey(identifier)) {
            return false;
        }
        Optional<PortNetworkDirection> direction = directionMap.get(identifier);
        return direction.isEmpty()
                || (serverbound ? direction.get().toServer() : direction.get().toClient());
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
