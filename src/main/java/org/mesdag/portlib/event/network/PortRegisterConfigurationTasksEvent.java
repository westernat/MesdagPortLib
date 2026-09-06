package org.mesdag.portlib.event.network;

import net.minecraft.network.Connection;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.network.config.IPortCustomConfigurationTask;
import org.mesdag.portlib.network.config.PortConfigurationManager;

import java.util.ArrayList;
import java.util.List;

/// 每个连接、服务端配置阶段启动前触发（对齐 1.21.1 的 `RegisterConfigurationTasksEvent`）。
///
/// 监听器拿到当前连接后可按需注册任务（例如用 [#getConnection] 判断对端能力后
/// 决定是否注册某个任务）。任务按注册顺序执行，全部结束后客户端才进世界。
public class PortRegisterConfigurationTasksEvent extends Event {
    private final Connection connection;
    private final List<IPortCustomConfigurationTask> tasks = new ArrayList<>();

    public PortRegisterConfigurationTasksEvent(Connection connection) {
        this.connection = connection;
    }

    /// 注册一个配置阶段任务（注册顺序即执行顺序）。
    public void register(IPortCustomConfigurationTask task) {
        tasks.add(task);
    }

    /// 当前连接。
    public Connection getConnection() {
        return connection;
    }

    /// 本连接需要执行的配置阶段任务（快照，由 [PortConfigurationManager] 消费）。
    public List<IPortCustomConfigurationTask> getTasks() {
        return new ArrayList<>(tasks);
    }
}
