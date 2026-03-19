package org.mesdag.portlib.datamap;

import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record DataMapFile<T, R>(
        boolean replace,
        Map<Either<TagKey<R>, ResourceKey<R>>, Optional<WithConditions<DataMapFile<T, R>>>> values,
        List<DataMapEntry.Removal<T, R>> removals
) {
}
