package org.mesdag.portlib.network.config;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortStreamCodec;

/// 配置阶段“大负载分片”消息（S2C，LOGIN 方向）。
///
/// 登录期信封有单包大小上限，datamap 清单等超大负载需要切片传输：
/// 服务端把原消息按字节切成多段，逐段包在 [PortConfigurationFragmentPayload] 里发送；
/// 客户端收到 `last` 段后按 [PortConfigurationManager] 记录的原始目标类型重组并解码，
/// 再以与正常收包一致的方式调用原消息的 `handle(...)`。
public class PortConfigurationFragmentPayload implements IPortPacket.S2C {
    public static final ResourceLocation IDENTIFIER = PortLib.asResource("configuration_fragment");
    public static final PortStreamCodec<FriendlyByteBuf, PortConfigurationFragmentPayload> STREAM_CODEC = PortStreamCodec.ofMember(
            PortConfigurationFragmentPayload::write,
            PortConfigurationFragmentPayload::decode
    );

    private final boolean first;
    private final boolean last;
    private final ResourceLocation target;
    private final byte[] data;

    public PortConfigurationFragmentPayload(boolean first, boolean last, ResourceLocation target, byte[] data) {
        this.first = first;
        this.last = last;
        this.target = target;
        this.data = data;
    }

    private static void write(FriendlyByteBuf buf, PortConfigurationFragmentPayload value) {
        buf.writeBoolean(value.first);
        buf.writeBoolean(value.last);
        if (value.first) {
            buf.writeResourceLocation(value.target);
        }
        buf.writeByteArray(value.data);
    }

    private static PortConfigurationFragmentPayload decode(FriendlyByteBuf buf) {
        boolean first = buf.readBoolean();
        boolean last = buf.readBoolean();
        ResourceLocation target = first ? buf.readResourceLocation() : null;
        byte[] data = buf.readByteArray();
        return new PortConfigurationFragmentPayload(first, last, target, data);
    }

    public boolean first() {
        return first;
    }

    public boolean last() {
        return last;
    }

    public ResourceLocation target() {
        return target;
    }

    public byte[] data() {
        return data;
    }

    @Override
    public void handle(Context context) {
        PortConfigurationManager.handleFragment(context, this);
    }

    @Override
    public void work(Player player) {}

    @Override
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }
}
