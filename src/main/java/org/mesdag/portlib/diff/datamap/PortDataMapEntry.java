package org.mesdag.portlib.diff.datamap;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.mesdag.portlib.datamap.PortAdvancedDataMapType;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.datamap.PortDataMapValueRemover;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;
import java.util.function.Function;

@Diff
public record PortDataMapEntry<T>(T value, boolean replace) {
    private PortDataMapEntry(T value) {
        this(value, false);
    }

    public static <T> Codec<PortDataMapEntry<T>> codec(PortDataMapType<?, T> type) {
        return Codec.either(
                RecordCodecBuilder.<PortDataMapEntry<T>>create(instance -> instance.group(
                        type.codec().fieldOf("value").forGetter(PortDataMapEntry::value),
                        Codec.BOOL.optionalFieldOf("replace", false).forGetter(PortDataMapEntry::replace)
                ).apply(instance, PortDataMapEntry::new)),
                type.codec()
        ).xmap(
                either -> either.map(Function.identity(), PortDataMapEntry::new),
                entry -> entry.replace() ? Either.left(entry) : Either.right(entry.value())
        );
    }

    public record Removal<T, R>(
            Either<TagKey<R>, ResourceKey<R>> key,
            Optional<PortDataMapValueRemover<R, T>> remover
    ) {
        private Removal(Either<TagKey<R>, ResourceKey<R>> key) {
            this(key, Optional.empty());
        }

        @SuppressWarnings("unchecked")
        public static <T, R> Codec<Removal<T, R>> codec(Codec<Either<TagKey<R>, ResourceKey<R>>> tagOrValue, PortDataMapType<R, T> attachment) {
            if (attachment instanceof PortAdvancedDataMapType<R, T, ?> advanced) {
                return RecordCodecBuilder.create(instance -> instance.group(
                        tagOrValue.fieldOf("key").forGetter(Removal::key),
                        ((Codec<PortDataMapValueRemover<R, T>>) advanced.remover()).optionalFieldOf("remover").forGetter(Removal::remover)
                ).apply(instance, Removal::new));
            }
            return RecordCodecBuilder.create(instance -> instance.group(
                    tagOrValue.fieldOf("key").forGetter(Removal::key)
            ).apply(instance, Removal::new));
        }
    }
}
