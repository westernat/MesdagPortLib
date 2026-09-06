package org.mesdag.portlib.diff.datamap;

import com.google.common.collect.Maps;
import io.netty.util.AttributeKey;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.network.config.PortConfigurationManager;
import org.mesdag.portlib.wrapper.common.extensions.IPortResourceLocationExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Diff
public record PortKnownRegistryDataMapsReplyPayload(
        Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>> dataMaps
) implements IPortPacket.C2S {
    public static final ResourceLocation IDENTIFIER = PortLib.asResource("known_registry_data_maps_reply");
    public static final PortStreamCodec<FriendlyByteBuf, PortKnownRegistryDataMapsReplyPayload> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.map(
                    Maps::newHashMapWithExpectedSize,
                    PortByteBufCodecs.registryKey(),
                    IPortResourceLocationExtension.STREAM_CODEC.apply(PortByteBufCodecs.collection(ArrayList::new))
            ), PortKnownRegistryDataMapsReplyPayload::dataMaps,
            PortKnownRegistryDataMapsReplyPayload::new
    );

    public static final AttributeKey<Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>>> ATTRIBUTE_KNOWN_DATA_MAPS = AttributeKey.valueOf("portlib:known_data_maps");

    @Override
    public void handle(Context context) {
        // 先把客户端的“已知 data map”清单写入本连接属性（内容同步依赖它），
        context.channelHandlerContext().attr(ATTRIBUTE_KNOWN_DATA_MAPS).set(dataMaps);
        // 再声明协商任务完成，让配置阶段推进到下一个任务。
        PortConfigurationManager.finishCurrentTask(context.connection(), PortRegistryDataMapNegotiation.KNOWN_TASK_TYPE);
    }

    @Override
    public void work(ServerPlayer player) {}

    @Override
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }
}
