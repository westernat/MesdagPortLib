package org.mesdag.portlib.network.config;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortStreamCodec;

/// “配置阶段结束”标记（S2C，LOGIN 方向）。
///
/// 对应 1.21.1 中服务端任务队列末尾 `JoinWorldTask` 发出的“配置结束”语义：
/// 客户端收到本消息后，才执行在 `ClientHandshakePacketListenerImpl#handleGameProfile`
/// 里被推迟的原版“切到 PLAY / 创建 ClientPacketListener”收尾动作（见
/// `diff/mixin/ClientHandshakePacketListenerImplMixin`）。
///
/// 本消息没有任何负载：仅作为窗口关闭的信号，由 [#clientFinish] 处理。
public class PortConfigurationFinishedPayload implements IPortPacket.S2C {
    public static final ResourceLocation IDENTIFIER = PortLib.asResource("configuration_finished");
    public static final PortConfigurationFinishedPayload INSTANCE = new PortConfigurationFinishedPayload();
    public static final PortStreamCodec<FriendlyByteBuf, PortConfigurationFinishedPayload> STREAM_CODEC = PortStreamCodec.unit(INSTANCE);

    private PortConfigurationFinishedPayload() {}

    @Override
    public void handle(Context context) {
        PortConfigurationManager.clientFinish(context.connection());
    }

    @Override
    public void work(Player player) {}

    @Override
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }
}
