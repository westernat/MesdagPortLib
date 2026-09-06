package org.mesdag.portlib.network.config;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.IPortPacket;

import java.util.function.Consumer;

/// 单个配置阶段任务的执行上下文：向任务暴露“发消息 / 声明完成 / 断线 / 拿连接”四件事。
///
/// 由 [PortConfigurationManager] 的服务器端阶段（`ServerStage`）在启动任务时创建并注入，
/// 任务实现本身不应与 `ServerStage` 的调度细节耦合。
public final class PortConfigurationContext {
    private final Connection connection;
    private final ResourceLocation taskType;
    private final Consumer<IPortPacket.S2C> sender;
    private final Consumer<ResourceLocation> finisher;
    private final Consumer<Component> disconnector;

    PortConfigurationContext(
            Connection connection,
            ResourceLocation taskType,
            Consumer<IPortPacket.S2C> sender,
            Consumer<ResourceLocation> finisher,
            Consumer<Component> disconnector
    ) {
        this.connection = connection;
        this.taskType = taskType;
        this.sender = sender;
        this.finisher = finisher;
        this.disconnector = disconnector;
    }

    /// 把一条 S2C 消息发给正在登录的客户端（经 `fml:loginwrapper` 信封，见
    /// [org.mesdag.portlib.network.PortNetworkHandler#sendLoginToClient]）。
    public void send(IPortPacket.S2C payload) {
        sender.accept(payload);
    }

    /// 声明“本任务已完成”。
    ///
    /// 发完即完的任务在 `start` 末尾同步调用；等回执的任务不调用本方法，而是等
    /// 客户端应答处理器调用 [PortConfigurationManager#finishCurrentTask]。
    public void finish() {
        finisher.accept(taskType);
    }

    /// 当前连接。
    public Connection connection() {
        return connection;
    }

    /// 直接断开连接（例如协商出不可接受的差异时）。
    public void disconnect(Component reason) {
        disconnector.accept(reason);
    }

    /// 当前正在执行的任务类型。
    public ResourceLocation type() {
        return taskType;
    }
}
