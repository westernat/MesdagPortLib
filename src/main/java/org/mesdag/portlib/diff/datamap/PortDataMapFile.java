package org.mesdag.portlib.diff.datamap;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import org.mesdag.portlib.datamap.PortAdvancedDataMapType;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.datamap.PortDataMapValueRemover;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.conditions.PortConditionalOps;
import org.mesdag.portlib.wrapper.common.conditions.PortWithConditions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Diff
public record PortDataMapFile<T, R>(
        boolean replace,
        Map<Either<TagKey<R>, ResourceKey<R>>, Optional<PortWithConditions<PortDataMapEntry<T>>>> values,
        List<PortDataMapEntry.Removal<T, R>> removals
) {
    public static <T, R> Codec<PortDataMapFile<T, R>> codec(ResourceKey<Registry<R>> registryKey, PortDataMapType<R, T> dataMap) {
        Codec<Either<TagKey<R>, ResourceKey<R>>> tagOrValue = ExtraCodecs.TAG_OR_ELEMENT_ID.xmap(
                l -> l.tag() ? Either.left(TagKey.create(registryKey, l.id())) : Either.right(ResourceKey.create(registryKey, l.id())),
                e -> e.map(t -> new ExtraCodecs.TagOrElementLocation(t.location(), true), r -> new ExtraCodecs.TagOrElementLocation(r.location(), false)));

        Codec<List<PortDataMapEntry.Removal<T, R>>> removalsCodec;
        if (dataMap instanceof PortAdvancedDataMapType<?, ?, ?>) {
            var removalCodec = PortDataMapEntry.Removal.codec(tagOrValue, dataMap);
            PortAdvancedDataMapType<R, T, PortDataMapValueRemover<R, T>> advanced = (PortAdvancedDataMapType<R, T, PortDataMapValueRemover<R, T>>) dataMap;
            removalsCodec = PortCodecExtension.withAlternative(
                    PortCodecExtension.withAlternative(removalCodec.listOf(), PortCodecExtension.decodeOnly(tagOrValue.listOf()
                            .map(l -> l.stream().map(k -> new PortDataMapEntry.Removal<T, R>(k, Optional.empty())).toList()))),
                    PortCodecExtension.decodeOnly(PortCodecExtension.strictUnboundedMap(tagOrValue, advanced.remover())
                            .map(map -> map.entrySet().stream()
                                    .map(entry -> new PortDataMapEntry.Removal<>(entry.getKey(), Optional.of(entry.getValue()))).toList())));
        } else {
            removalsCodec = tagOrValue.listOf()
                    .xmap(l -> l.stream().map(k -> new PortDataMapEntry.Removal<T, R>(k, Optional.empty())).toList(),
                            rm -> rm.stream().map(PortDataMapEntry.Removal::key).toList());
        }

        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("replace", false).forGetter(PortDataMapFile::replace),
                PortCodecExtension.strictUnboundedMap(tagOrValue, PortConditionalOps.createConditionalCodecWithConditions(PortDataMapEntry.codec(dataMap))).fieldOf("values").forGetter(PortDataMapFile::values),
                removalsCodec.optionalFieldOf("remove", List.of()).forGetter(PortDataMapFile::removals)
        ).apply(instance, PortDataMapFile::new));
    }
}
