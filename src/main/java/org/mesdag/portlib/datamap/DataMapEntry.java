package org.mesdag.portlib.datamap;

import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.Optional;

public record DataMapEntry<T>(T value, boolean replace) {
    public record Removal<T, R>(
            Either<TagKey<R>, ResourceKey<R>> key,
            Optional<DataMapValueRemover<R, T>> remover
    ) {
    }
}
