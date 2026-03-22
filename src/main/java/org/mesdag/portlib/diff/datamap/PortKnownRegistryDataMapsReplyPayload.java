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
import org.mesdag.portlib.network.login.PortLoginPacket;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Diff
public class PortKnownRegistryDataMapsReplyPayload extends PortLoginPacket implements IPortPacket.C2S {
    public static final PortIdentifier IDENTIFIER = PortLib.asResource("known_registry_data_maps_reply");
    public static final PortStreamCodec<FriendlyByteBuf, PortKnownRegistryDataMapsReplyPayload> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.map(
                    Maps::newHashMapWithExpectedSize,
                    PortByteBufCodecs.registryKey(),
                    PortByteBufCodecs.RESOURCE_LOCATION.apply(PortByteBufCodecs.collection(ArrayList::new))
            ), PortKnownRegistryDataMapsReplyPayload::dataMaps,
            PortKnownRegistryDataMapsReplyPayload::new
    );
    private final Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>> dataMaps;

    public PortKnownRegistryDataMapsReplyPayload(Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>> dataMaps) {
        this.dataMaps = dataMaps;
    }

    public Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>> dataMaps() {
        return dataMaps;
    }

    public static final AttributeKey<Map<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>>> ATTRIBUTE_KNOWN_DATA_MAPS = AttributeKey.valueOf("portlib:known_data_maps");

    @Override
    public void handle(Context context) {
        context.channelHandlerContext().attr(ATTRIBUTE_KNOWN_DATA_MAPS).set(dataMaps);
    }

    @Override
    public void work(ServerPlayer player) {}

    @Override
    public PortIdentifier identifier() {
        return IDENTIFIER;
    }
}
