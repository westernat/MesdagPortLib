package org.mesdag.portlib.diff.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.mesdag.portlib.network.config.PortConfigurationManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/// 方案 C 的服务端锚点（详见 `docs/backport-configuration-phase.md`）。
///
/// 1.20.1 中玩家“进世界”的唯一收口是 `ServerLoginPacketListenerImpl#placeNewPlayer(ServerPlayer)`
/// （它直接调用 `PlayerList#placeNewPlayer`）。本 mixin：
///
///     - 在第一次 `placeNewPlayer` 被调用时（此时已发完 `ClientboundGameProfilePacket`、
///     玩家对象已创建），若对端是 PortLib 客户端则把这次进世界推迟，
///     交给 [PortConfigurationManager] 先跑配置阶段；
///     - 每个 tick 驱动配置阶段；阶段完成后再通过 `placeNewPlayer` 恢复原版进世界。
///
/// 原版/未装 PortLib 的客户端不受影响（[PortConfigurationManager#shouldUseConfigurationStage]
/// 判定为 false 时完全不拦截）；内存连接（单机）同样跳过。
///
/// 断线防护：阶段只在连接存活时推进；`onDisconnect` 时清除阶段（防止对已关闭连接继续
/// 超时/恢复进世界，进而引发重复的 `handleDisconnection()`）；配置阶段完成过一次进世界后
/// 用 `portlib$placed` 阻止 vanilla 因 DELAY_ACCEPT/READY_TO_ACCEPT 再走一次
/// `handleAcceptedLogin`（顶号等场景下原版会把状态留在 READY_TO_ACCEPT）。
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin {
    @Final
    @Shadow
    Connection connection;

    @Shadow
    protected abstract void placeNewPlayer(ServerPlayer p_143700_);

    @Unique
    private boolean portlib$resuming;

    @Unique
    private boolean portlib$placed;

    @Inject(method = "tick", at = @At("HEAD"))
    private void portlib$tick(CallbackInfo ci) {
        if (portlib$resuming || portlib$placed) return;
        PortConfigurationManager.tickServerStage(this.connection);
    }

    /// 防止配置阶段完成（玩家已放行进世界）后，原版因残留的 READY_TO_ACCEPT/DELAY_ACCEPT
    /// 状态再次进入 `handleAcceptedLogin`（会在断开清理阶段造成重复/悬挂动作）。
    @Inject(method = "handleAcceptedLogin", at = @At("HEAD"), cancellable = true)
    private void portlib$preventReaccept(CallbackInfo ci) {
        if (portlib$placed) {
            ci.cancel();
        }
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void portlib$clearConfigOnDisconnect(CallbackInfo ci) {
        PortConfigurationManager.clearServerStage(this.connection);
    }

    @Inject(method = "placeNewPlayer", at = @At("HEAD"), cancellable = true)
    private void portlib$delayPlaceNewPlayer(ServerPlayer p_143700_, CallbackInfo ci) {
        if (portlib$resuming) return;
        if (!PortConfigurationManager.shouldUseConfigurationStage(this.connection)) return;
        PortConfigurationManager.startServerStage(this.connection, () -> {
            portlib$resuming = true;
            try {
                placeNewPlayer(p_143700_);
                portlib$placed = true;
            } finally {
                portlib$resuming = false;
            }
        });
        ci.cancel();
    }
}
