package org.mesdag.portlib.network.config;

import net.minecraft.resources.ResourceLocation;

/// 一个 PortLib“配置阶段任务”（1.20.1 CONFIGURATION 仿真的任务抽象，
/// 语义对应 1.21.1 的 `ICustomConfigurationTask` / vanilla `ConfigurationTask`）。
///
/// 任务只关心两件事：
/// - [#type()] —— 任务唯一 id（用于回执匹配与错误定位，等价 vanilla `ConfigurationTask.Type`）；
/// - [#start(PortConfigurationContext)] —— 任务执行体：通过
///   [PortConfigurationContext#send] 向客户端发送 S2C 消息。
///
/// 任务何时“算完成”由任务自身决定（与 NeoForge 一致）：
/// - 发完即完（fire-and-forget，如配置文件同步）：在 `start` 末尾调用
///   [PortConfigurationContext#finish]；
/// - 等客户端回执：不在 `start` 里 finish，等该任务注册的 C2S 登录消息处理器
///   收到客户端应答后调用 [PortConfigurationManager#finishCurrentTask]。
///
/// 管理器（[PortConfigurationManager.ServerStage] 队列）按“一个任务结束才启动下一个”的
/// 顺序执行，全部结束后发出“配置结束”信号。
public interface IPortCustomConfigurationTask {
    /// 任务唯一类型 id。
    ResourceLocation type();

    /// 执行本任务。
    ///
    /// @param context 执行上下文：[PortConfigurationContext#send] 发送消息；
    ///                [PortConfigurationContext#finish] 声明任务完成（可同步调用）。
    void start(PortConfigurationContext context);
}
