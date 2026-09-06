package org.mesdag.portlib.diff.datamap;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.extensions.IPortResourceLocationExtension;

import java.util.*;
import java.util.stream.Collectors;

public record PortKnownRegistryDataMapsPayload(
        Map<ResourceKey<? extends Registry<?>>, List<KnownDataMap>> dataMaps
) implements IPortPacket.S2C {
    public static final ResourceLocation IDENTIFIER = PortLib.asResource("known_registry_data_maps");
    public static final PortStreamCodec<FriendlyByteBuf, PortKnownRegistryDataMapsPayload> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.map(
                    Maps::newHashMapWithExpectedSize, PortByteBufCodecs.registryKey(),
                    KnownDataMap.STREAM_CODEC.apply(PortByteBufCodecs.list())
            ), PortKnownRegistryDataMapsPayload::dataMaps,
            PortKnownRegistryDataMapsPayload::new
    );

    @Override
    public void handle(Context context) {
        record MandatoryEntry(ResourceKey<? extends Registry<?>> registry, ResourceLocation id) {}
        Set<MandatoryEntry> ourMandatory = new HashSet<>();
        PortDataMapLoader.getDataMaps().forEach((reg, values) -> values.values().forEach(attach -> {
            if (attach.mandatorySync()) {
                ourMandatory.add(new MandatoryEntry(reg, attach.id()));
            }
        }));

        Set<MandatoryEntry> theirMandatory = new HashSet<>();
        dataMaps.forEach((reg, values) -> values.forEach(attach -> {
            if (attach.mandatory()) {
                theirMandatory.add(new MandatoryEntry(reg, attach.id()));
            }
        }));

        List<Component> messages = new ArrayList<>();
        var missingOur = Sets.difference(ourMandatory, theirMandatory);
        if (!missingOur.isEmpty()) {
            messages.add(Component.translatable("portlib.network.data_maps.missing_our", Component.literal(missingOur.stream()
                    .map(e -> e.id() + " (" + e.registry().location() + ")")
                    .collect(Collectors.joining(", "))).withStyle(ChatFormatting.GOLD)));
        }

        var missingTheir = Sets.difference(theirMandatory, ourMandatory);
        if (!missingTheir.isEmpty()) {
            messages.add(Component.translatable("portlib.network.data_maps.missing_their", Component.literal(missingTheir.stream()
                    .map(e -> e.id() + " (" + e.registry().location() + ")")
                    .collect(Collectors.joining(", "))).withStyle(ChatFormatting.GOLD)));
        }

        if (!messages.isEmpty()) {
            MutableComponent message = Component.empty();
            final var itr = messages.iterator();
            while (itr.hasNext()) {
                message = message.append(itr.next());
                if (itr.hasNext()) {
                    message = message.append("\n");
                }
            }

            context.disconnect(message);
            return;
        }

        var known = new HashMap<ResourceKey<? extends Registry<?>>, Collection<ResourceLocation>>();
        PortDataMapLoader.getDataMaps().forEach((key, vals) -> known.put(key, vals.keySet()));
        context.reply(new PortKnownRegistryDataMapsReplyPayload(known));
    }

    @Override
    public void work(Player player) {}

    @Override
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }

    public record KnownDataMap(ResourceLocation id, boolean mandatory) {
        public static final PortStreamCodec<FriendlyByteBuf, KnownDataMap> STREAM_CODEC = PortStreamCodec.composite(
                IPortResourceLocationExtension.STREAM_CODEC, KnownDataMap::id,
                PortByteBufCodecs.BOOL, KnownDataMap::mandatory,
                KnownDataMap::new
        );
    }
}
