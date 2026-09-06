package org.mesdag.portlib.network.config;

import io.netty.util.AttributeKey;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ConnectionData;
import net.minecraftforge.network.NetworkHooks;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortNetworkHandler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/// 1.20.1 上“配置阶段（CONFIGURATION）”的仿真实现（详见仓库 `docs/backport-configuration-phase.md`）。
///
/// 背景：1.20.1 没有 1.20.2+ 的 CONFIGURATION 协议状态，登录接受后会在同一 tick 内直接
/// `placeNewPlayer` 进世界（`ServerLoginPacketListenerImpl#handleAcceptedLogin`）。
/// 本类把“发完 GameProfile 之后、放行进世界之前”的一段窗口变成可跨 tick 等待回执的任务队列，
/// 语义对齐 1.21.1 的 `ConfigurationTask` 队列：
///
/// 1. 任务实现 [IPortCustomConfigurationTask]，经 [#registerConfigurationTask] 注册；
/// 2. 服务端 [ServerStage] 按注册顺序逐任务执行：一个任务结束（任务在 `start` 内同步
///    [PortConfigurationContext#finish]，或客户端回执触发 [#finishCurrentTask]）才启动下一个；
/// 3. 队列清空后发出“配置结束”信号（[PortConfigurationFinishedPayload]），
///    客户端才执行被推迟的“切 PLAY / 建 ClientPacketListener”收尾，服务端随后恢复进世界。
///
/// 传输层：1.20.1 登录协议只有 vanilla custom query（transactionId 回执、无频道回执），
/// Forge 用 `fml:loginwrapper` 信封承载内层频道消息；因此这里经
/// [PortNetworkHandler#sendLoginToClient] 以同样信封格式发送，收包侧由 Forge 的
/// [net.minecraftforge.network.LoginWrapper] 拆封后按 `portlib:main` 分发。
///
/// 能力协商：是否进入配置阶段，取决于“对端是否具备 PortLib” —— Forge 握手结束时
/// 双方已经把频道清单写入 [ConnectionData]（[#getConnectionData]），因此不新增任何探测包。
public final class PortConfigurationManager {
    /// PortLib 在 Forge 频道清单里登记的频道名（与 `PortNetworkHandler` 构造一致）。
    public static final ResourceLocation PORTLIB_CHANNEL =
            ResourceLocation.fromNamespaceAndPath(PortLib.MODID, "main");

    private static final AttributeKey<Runnable> CLIENT_CONTINUATION =
            AttributeKey.valueOf("portlib:client_configuration_continuation");
    private static final AttributeKey<ServerStage> SERVER_STAGE =
            AttributeKey.valueOf("portlib:server_configuration_stage");

    /// 单个任务最长存活 tick 数；超时按“慢登录”处理断开。
    public static final int MAX_TASK_TICKS = 200;

    /// 全部已注册的配置阶段任务（服务端按注册顺序执行）。任务实例应无状态、可跨连接复用。
    private static final List<IPortCustomConfigurationTask> CONFIGURATION_TASKS = new ArrayList<>();

    private static boolean registered = false;

    private PortConfigurationManager() {}

    /// 注册配置阶段消息（目前只有“完成”标记）与框架自身初始化。
    /// 须在 mod 构造阶段调用一次（`PortLib` 构造器、`PortNetworkHandler.init()` 之后）。
    public static void init() {
        if (registered) return;
        registered = true;
        PortNetworkHandler networkHandler = PortLib.NETWORK_HANDLER;
        networkHandler.registerLoginS2C(
                PortConfigurationFinishedPayload.class,
                PortConfigurationFinishedPayload.IDENTIFIER,
                PortConfigurationFinishedPayload.STREAM_CODEC
        );
    }

    /// 注册一个配置阶段任务。模块在自身 init 时调用（例如 datamap 模块注册 Known 协商任务）。
    /// 注册顺序即执行顺序。
    public static void registerConfigurationTask(IPortCustomConfigurationTask task) {
        CONFIGURATION_TASKS.add(task);
    }

    /// 对端是否是 PortLib（两边都登记了 `portlib:main` 频道）。
    ///
    /// 内存连接（单机 / 局域网本机）跳过：同一进程无需网络同步，走原版路径。
    public static boolean isPortLibPeer(Connection connection) {
        if (connection.isMemoryConnection()) return false;
        ConnectionData data = NetworkHooks.getConnectionData(connection);
        return data != null && data.getChannels().containsKey(PORTLIB_CHANNEL);
    }

    /// 该连接是否要进入 PortLib 配置阶段。两端（服务端 mixin 与客户端 mixin）用同一判定，
    /// 保证“服务端推迟进世界 ⇔ 客户端推迟切 PLAY”严格一致。
    public static boolean shouldUseConfigurationStage(Connection connection) {
        return isPortLibPeer(connection);
    }

    // ============================ 服务端（登录监听器侧） ============================

    /// 在“原版将放行玩家进世界”的瞬间创建配置阶段并开始执行任务队列。
    /// 由 `ServerLoginPacketListenerImplMixin#placeNewPlayer` 拦截时调用；
    /// 队列完成后执行 `resumePlacement` 恢复原版的进世界流程。
    public static void startServerStage(Connection connection, Runnable resumePlacement) {
        connection.channel().attr(SERVER_STAGE).set(new ServerStage(connection, resumePlacement));
    }

    /// 每个服务端 tick 驱动配置阶段；由 `ServerLoginPacketListenerImplMixin#tick` 调用。
    ///
    /// 若连接已关闭（客户端在配置期间掉线等），不再驱动阶段并清理之，避免后续
    /// tick / 超时 / 恢复进世界在失效连接上继续动作（防止重复断线清理）。
    public static void tickServerStage(Connection connection) {
        ServerStage stage = connection.channel().attr(SERVER_STAGE).get();
        if (stage == null) {
            return;
        }
        if (!connection.isConnected()) {
            connection.channel().attr(SERVER_STAGE).set(null);
            return;
        }
        stage.tick();
    }

    /// 立即清除该连接上的服务端配置阶段（例如登录监听器 `onDisconnect` 时）。
    public static void clearServerStage(Connection connection) {
        connection.channel().attr(SERVER_STAGE).set(null);
    }

    /// 声明“类型为 `type` 的配置任务已完成”。可由两类调用方触发：
    ///
    /// - 服务端主线程：任务在 `start` 内同步调用 [PortConfigurationContext#finish]；
    /// - 客户端回执处理器（网络线程）：例如 datamap Known 的 Reply 到达时。
    ///
    /// 实现上只投递一个请求，实际的队列推进发生在下一个服务端 tick，保证线程安全与顺序性。
    public static void finishCurrentTask(Connection connection, ResourceLocation type) {
        ServerStage stage = connection.channel().attr(SERVER_STAGE).get();
        if (stage != null) {
            stage.requestFinish(type);
        }
    }

    static final class ServerStage {
        private final Connection connection;
        private final Runnable resumePlacement;
        private final ArrayDeque<IPortCustomConfigurationTask> pending;
        private final Queue<ResourceLocation> finishRequests = new ConcurrentLinkedQueue<>();

        private IPortCustomConfigurationTask current;
        private boolean finished = false;
        private int stuckTicks = 0;
        private int seq = 0;

        ServerStage(Connection connection, Runnable resumePlacement) {
            this.connection = connection;
            this.resumePlacement = resumePlacement;
            this.pending = new ArrayDeque<>(CONFIGURATION_TASKS);
        }

        void tick() {
            if (finished) return;

            if (current == null) {
                if (pending.isEmpty()) {
                    finish();
                    return;
                }
                current = pending.removeFirst();
                stuckTicks = 0;
                PortConfigurationContext context = new PortConfigurationContext(
                        connection, current.type(),
                        this::sendLogin,
                        this::requestFinish,
                        connection::disconnect
                );
                current.start(context);
            }

            drainFinishRequests();

            if (current != null && ++stuckTicks > MAX_TASK_TICKS) {
                clear();
                connection.disconnect(Component.translatable("multiplayer.disconnect.slow_login"));
            }
        }

        void requestFinish(ResourceLocation type) {
            finishRequests.add(type);
        }

        private void drainFinishRequests() {
            ResourceLocation request;
            while ((request = finishRequests.poll()) != null) {
                if (current != null && request.equals(current.type())) {
                    current = null;
                } else {
                    PortLib.LOGGER.warn("Ignoring configuration task finish request for unknown/stale task '{}'", request);
                }
            }
        }

        private void sendLogin(IPortPacket.S2C payload) {
            PortLib.NETWORK_HANDLER.sendLoginToClient(connection, seq++, payload);
        }

        private void finish() {
            finished = true;
            if (!connection.isConnected()) {
                clear();
                return;
            }
            // 先让客户端收到“配置结束”，再恢复原版 placeNewPlayer（此时才会发 PLAY 的 LoginPacket）。
            sendLogin(PortConfigurationFinishedPayload.INSTANCE);
            clear();
            resumePlacement.run();
        }

        private void clear() {
            if (connection.channel() != null) {
                connection.channel().attr(SERVER_STAGE).set(null);
            }
        }
    }

    // ============================ 客户端（登录监听器侧） ============================

    /// 客户端 mixin 推迟原版收尾时，把“恢复收尾”的延续存到连接上。
    public static void storeClientContinuation(Connection connection, Runnable continuation) {
        connection.channel().attr(CLIENT_CONTINUATION).set(continuation);
    }

    /// 清除客户端延期收尾（连接断开时调用，避免在失效连接上执行）。
    public static void clearClientContinuation(Connection connection) {
        connection.channel().attr(CLIENT_CONTINUATION).set(null);
    }

    /// 收到“配置结束”标记后（网络线程）执行被推迟的原版收尾：
    /// setProtocol(PLAY) + handleClientLoginSuccess + new ClientPacketListener。
    public static void clientFinish(Connection connection) {
        Runnable continuation = connection.channel().attr(CLIENT_CONTINUATION).getAndSet(null);
        if (continuation != null) {
            continuation.run();
        }
    }
}
