package org.mesdag.portlib.wrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Objects;

@SuppressWarnings("all")
public class PortEnvironment {
    public static boolean isPhysicalClient() {
        return FMLEnvironment.dist.isClient();
    }

    /// @return 单人模式中为false；客户端连接服务端时，客户端为true，服务端为false
    /// @apiNote 你应该在逻辑服务端启动后调用这个方法，且仅适用于在逻辑服务端调用
    public static boolean isLogicalClient() {
        return isPhysicalClient() && ServerLifecycleHooks.getCurrentServer() == null;
    }

    public static boolean isPhysicalServer() {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    /// @return 逻辑客户端为false, 逻辑服务端为true
    /// @apiNote 你应该在逻辑服务端启动后调用这个方法
    public static boolean isLogicalServer() {
        if (isPhysicalServer()) return true;
        return ServerLifecycleHooks.getCurrentServer() != null && ServerLifecycleHooks.getCurrentServer().isSameThread();
    }

    public static boolean isDeveloper() {
        return !FMLEnvironment.production;
    }

    public static RegistryAccess registryAccess() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null && isPhysicalClient()) {
            return Minecraft.getInstance().getConnection().registryAccess();
        }
        return Objects.requireNonNull(server, "No Server Found").registryAccess();
    }

    /// 可于游戏加载早期阶段判断
    public static boolean isModLoaded(String modid) {
        return LoadingModList.get().getModFileById(modid) != null;
    }
}
