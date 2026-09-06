package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import org.mesdag.portlib.network.config.PortConfigurationManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/// 方案 C 的客户端锚点（详见 `docs/backport-configuration-phase.md`）。
///
/// 1.20.1 客户端收到 `ClientboundGameProfilePacket` 后会在
/// `handleGameProfile` 里直接 setProtocol(PLAY) 并创建 `ClientPacketListener`
/// （登录 → 游戏没有任何回执往返）。当服务端会进入 PortLib 配置阶段时，本 mixin 把这段
/// 原版收尾推迟到收到 `configuration_finished` 消息之后，再原样执行一次
/// （通过 `handleGameProfile` + `portlib$resuming` 防重入）。
/// 服务端不是 PortLib（或内存连接）时完全不拦截。
///
/// 断线防护：`onDisconnect` 时清除被推迟的收尾延续，避免在失效连接上执行原版收尾。
@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class ClientHandshakePacketListenerImplMixin {
    @Final
    @Shadow
    private Connection connection;

    @Shadow
    public abstract void handleGameProfile(ClientboundGameProfilePacket packet);

    @Unique
    private boolean portlib$resuming;

    @Inject(method = "handleGameProfile", at = @At("HEAD"), cancellable = true)
    private void portlib$deferPlayEntry(ClientboundGameProfilePacket packet, CallbackInfo ci) {
        if (portlib$resuming) return;
        if (!PortConfigurationManager.shouldUseConfigurationStage(this.connection)) return;
        PortConfigurationManager.storeClientContinuation(this.connection, () -> {
            portlib$resuming = true;
            try {
                handleGameProfile(packet);
            } finally {
                portlib$resuming = false;
            }
        });
        ci.cancel();
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void portlib$clearContinuationOnDisconnect(CallbackInfo ci) {
        PortConfigurationManager.clearClientContinuation(this.connection);
    }
}
