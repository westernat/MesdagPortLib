package org.mesdag.portlib.diff.datamap;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.OnDatapackSyncEvent;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Diff
public class PortRegistryDataMapNegotiation {
//    public static final PortIdentifier IDENTIFIER = PortLib.asResource("registry_data_map_negotiation");
//
//    public static void handle(IPortCustomLoginTask task) {
//        final Map<ResourceKey<? extends Registry<?>>, List<PortKnownRegistryDataMapsPayload.KnownDataMap>> dataMaps = new HashMap<>();
//        PortDataMapLoader.getDataMaps().forEach((key, attach) -> {
//            final List<PortKnownRegistryDataMapsPayload.KnownDataMap> list = new ArrayList<>();
//            attach.forEach((id, val) -> {
//                if (val.networkCodec() != null) {
//                    list.add(new PortKnownRegistryDataMapsPayload.KnownDataMap(id, val.mandatorySync()));
//                }
//            });
//            dataMaps.put(key, list);
//        });
//        task.sender().accept(new PortKnownRegistryDataMapsPayload(dataMaps));
//    }

    public static void init() {
//        NETWORK_HANDLER.addLoginTask(
//                PortRegistryDataMapNegotiation.IDENTIFIER,
//                PortRegistryDataMapNegotiation::handle,
//                PortKnownRegistryDataMapsPayload.IDENTIFIER,
//                PortKnownRegistryDataMapsPayload.STREAM_CODEC,
//                PortKnownRegistryDataMapsPayload::handle,
//                PortKnownRegistryDataMapsReplyPayload.IDENTIFIER,
//                PortKnownRegistryDataMapsReplyPayload.STREAM_CODEC,
//                PortKnownRegistryDataMapsReplyPayload::handle
//        );
        PortLib.NETWORK_HANDLER.registerInGameS2C(
                PortKnownRegistryDataMapsPayload.class,
                PortKnownRegistryDataMapsPayload.IDENTIFIER,
                PortKnownRegistryDataMapsPayload.STREAM_CODEC,
                PortKnownRegistryDataMapsPayload::handle
        );
        PortLib.NETWORK_HANDLER.registerInGameC2S(
                PortKnownRegistryDataMapsReplyPayload.class,
                PortKnownRegistryDataMapsReplyPayload.IDENTIFIER,
                PortKnownRegistryDataMapsReplyPayload.STREAM_CODEC,
                PortKnownRegistryDataMapsReplyPayload::handle
        );
        PortEventHandler.addListener((OnDatapackSyncEvent event) -> {
            ServerPlayer player = event.getPlayer();
            if (player == null) return;
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
            PortLib.NETWORK_HANDLER.sendToPlayer(player, new PortKnownRegistryDataMapsPayload(dataMaps));
        });
    }
}
