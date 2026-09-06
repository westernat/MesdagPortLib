package org.mesdag.portlib.diff.datamap;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.config.IPortCustomConfigurationTask;
import org.mesdag.portlib.network.config.PortConfigurationContext;
import org.mesdag.portlib.network.config.PortConfigurationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// 注册表 data map 的“进服前协商”，从 post-join 挪进配置阶段（方案 C，详见
/// `docs/backport-configuration-phase.md`）。
///
/// 此前 Known/Reply 在 `OnDatapackSyncEvent`（玩家已加入世界）才发送，而内容同步
/// （`PortDataMapLoader#handleSync`）又依赖客户端 Reply 后写入的频道属性，导致
/// **新玩家首次加入时内容同步必然被跳过**。现在协商作为一条 [IPortCustomConfigurationTask]
/// 在“进世界前”完成：
///
/// - [PortKnownRegistryDataMapsPayload] / [PortKnownRegistryDataMapsReplyPayload]
///   注册为 LOGIN 方向消息（配置阶段内收发）；
/// - 服务端以任务 [KNOWN_TASK_TYPE] 注册到 [PortConfigurationManager]：任务 start 发送 Known
///   （[#runKnownTask]），收到 Reply 后由本类注册的 C2S 处理器调用
///   [PortConfigurationManager#finishCurrentTask] 声明任务完成；
/// - join 时原有的内容同步（`PortDataMapLoader` 的 `OnDatapackSyncEvent` 监听）保持不变——
///   此时 Known 已协商、属性已写入，首次加入即能下发内容。
@Diff
public class PortRegistryDataMapNegotiation {
    /// 该协商任务在配置阶段队列中的类型 id。
    public static final ResourceLocation KNOWN_TASK_TYPE = PortLib.asResource("known_registry_data_maps");

    public static void init() {
        PortLib.NETWORK_HANDLER.registerLoginS2C(
                PortKnownRegistryDataMapsPayload.class,
                PortKnownRegistryDataMapsPayload.IDENTIFIER,
                PortKnownRegistryDataMapsPayload.STREAM_CODEC
        );
        PortLib.NETWORK_HANDLER.registerLoginC2S(
                PortKnownRegistryDataMapsReplyPayload.class,
                PortKnownRegistryDataMapsReplyPayload.IDENTIFIER,
                PortKnownRegistryDataMapsReplyPayload.STREAM_CODEC,
                (p, ctx) -> {
                    // 先把客户端的“已知 data map”清单写入本连接属性（内容同步依赖它），
                    // 再声明协商任务完成，让配置阶段推进到下一个任务。
                    p.handle(ctx);
                    PortConfigurationManager.finishCurrentTask(ctx.connection(), KNOWN_TASK_TYPE);
                }
        );
        PortConfigurationManager.registerConfigurationTask(new IPortCustomConfigurationTask() {
            @Override
            public ResourceLocation type() {
                return KNOWN_TASK_TYPE;
            }

            @Override
            public void start(PortConfigurationContext context) {
                if (!hasAnyNetworkedDataMaps()) {
                    // 服务器没有任何带网络编解码的 data map，无需协商，直接完成。
                    context.finish();
                    return;
                }
                runKnownTask(context::send);
                // 完成时机：客户端回执（Reply 处理器 → finishCurrentTask）。
            }
        });
    }

    /// 是否存在任何带网络编解码的 data map（两端由同一套静态注册，结果一致）。
    private static boolean hasAnyNetworkedDataMaps() {
        return !PortDataMapLoader.getDataMaps().isEmpty();
    }

    /// 配置阶段的服务端任务体：把服务器已知 data map 清单发给客户端。
    private static void runKnownTask(java.util.function.Consumer<IPortPacket.S2C> sender) {
        final Map<ResourceKey<? extends Registry<?>>, List<PortKnownRegistryDataMapsPayload.KnownDataMap>> dataMaps = new HashMap<>();
        PortDataMapLoader.getDataMaps().forEach((key, attach) -> {
            final List<PortKnownRegistryDataMapsPayload.KnownDataMap> list = new ArrayList<>();
            attach.forEach((id, val) -> {
                if (val.networkCodec() != null) {
                    list.add(new PortKnownRegistryDataMapsPayload.KnownDataMap(id, val.mandatorySync()));
                }
            });
            dataMaps.put(key, list);
        });
        sender.accept(new PortKnownRegistryDataMapsPayload(dataMaps));
    }

    private PortRegistryDataMapNegotiation() {}
}
