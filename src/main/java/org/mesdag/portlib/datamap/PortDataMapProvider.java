package org.mesdag.portlib.datamap;

import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import PortLib.extensions.net.minecraft.core.HolderLookup.PortHolderLookupExtension;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.mesdag.portlib.diff.datamap.PortDataMapEntry;
import org.mesdag.portlib.diff.datamap.PortDataMapFile;
import org.mesdag.portlib.diff.datamap.PortDataMapLoader;
import org.mesdag.portlib.wrapper.common.conditions.PortConditionalOps;
import org.mesdag.portlib.wrapper.common.conditions.PortWithConditions;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class PortDataMapProvider implements DataProvider {
    protected final CompletableFuture<HolderLookup.Provider> lookupProvider;
    protected final PackOutput.PathProvider pathProvider;
    protected final Map<PortDataMapType<?, ?>, Builder<?, ?>> builders = new HashMap<>();

    protected PortDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.lookupProvider = lookupProvider;
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, PortDataMapLoader.PATH);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return lookupProvider.thenCompose(provider -> {
            gather(provider);

            DynamicOps<JsonElement> dynamicOps = PortHolderLookupExtension.Provider.createSerializationContext(provider, JsonOps.INSTANCE);

            return CompletableFuture.allOf(this.builders.entrySet().stream().map(entry -> {
                PortDataMapType<?, ?> type = entry.getKey();
                Path path = this.pathProvider.json(type.id().withPrefix(PortDataMapLoader.getFolderLocation(type.registryKey().location()) + "/"));
                return generate(path, cache, entry.getValue(), dynamicOps);
            }).toArray(CompletableFuture[]::new));
        });
    }

    private <T, R> CompletableFuture<?> generate(Path out, CachedOutput cache, Builder<T, R> builder, DynamicOps<JsonElement> ops) {
        return CompletableFuture.supplyAsync(() -> {
            Codec<Optional<PortWithConditions<PortDataMapFile<T, R>>>> withConditionsCodec = PortConditionalOps.createConditionalCodecWithConditions(PortDataMapFile.codec(builder.registryKey, builder.type));
            return PortDataResultExtension.getOrThrow(withConditionsCodec.encodeStart(ops, Optional.of(builder.build())), msg -> new RuntimeException("Failed to encode %s: %s".formatted(out, msg)));
        }).thenComposeAsync(encoded -> DataProvider.saveStable(cache, encoded, out));
    }

    protected abstract void gather(HolderLookup.Provider provider);

    @SuppressWarnings("unchecked")
    public <T, R> Builder<T, R> builder(PortDataMapType<R, T> type) {
        if (type instanceof PortAdvancedDataMapType<R, T, ?> advanced) {
            return builder(advanced);
        }
        return (Builder<T, R>) builders.computeIfAbsent(type, k -> new Builder<>(type));
    }

    @SuppressWarnings("unchecked")
    public <T, R, VR extends PortDataMapValueRemover<R, T>> AdvancedBuilder<T, R, VR> builder(PortAdvancedDataMapType<R, T, VR> type) {
        return (AdvancedBuilder<T, R, VR>) builders.computeIfAbsent(type, k -> new AdvancedBuilder<>(type));
    }

    @Override
    public String getName() {
        return "Data Maps";
    }

    public static class Builder<T, R> {
        private final Map<Either<TagKey<R>, ResourceKey<R>>, Optional<PortWithConditions<PortDataMapEntry<T>>>> values = new LinkedHashMap<>();
        protected final List<PortDataMapEntry.Removal<T, R>> removals = new ArrayList<>();
        protected final ResourceKey<Registry<R>> registryKey;
        private final PortDataMapType<R, T> type;
        private final List<ICondition> conditions = new ArrayList<>();

        private boolean replace;

        public Builder(PortDataMapType<R, T> type) {
            this.type = type;
            this.registryKey = type.registryKey();
        }

        public Builder<T, R> add(ResourceKey<R> key, T value, boolean replace, ICondition... conditions) {
            this.values.put(Either.right(key), Optional.of(new PortWithConditions<>(new PortDataMapEntry<>(value, replace), conditions)));
            return this;
        }

        public Builder<T, R> add(ResourceLocation id, T value, boolean replace, ICondition... conditions) {
            return add(ResourceKey.create(registryKey, id), value, replace, conditions);
        }

        public Builder<T, R> add(Holder<R> object, T value, boolean replace, ICondition... conditions) {
            return add(object.unwrapKey().orElseThrow(), value, replace, conditions);
        }

        public Builder<T, R> add(TagKey<R> tag, T value, boolean replace, ICondition... conditions) {
            this.values.put(Either.left(tag), Optional.of(new PortWithConditions<>(new PortDataMapEntry<>(value, replace), conditions)));
            return this;
        }

        public Builder<T, R> remove(ResourceLocation id) {
            this.removals.add(new PortDataMapEntry.Removal<>(Either.right(ResourceKey.create(registryKey, id)), Optional.empty()));
            return this;
        }

        public Builder<T, R> remove(TagKey<R> tag) {
            this.removals.add(new PortDataMapEntry.Removal<>(Either.left(tag), Optional.empty()));
            return this;
        }

        public Builder<T, R> remove(Holder<R> value) {
            this.removals.add(new PortDataMapEntry.Removal<>(Either.right(value.unwrap().orThrow()), Optional.empty()));
            return this;
        }

        public Builder<T, R> replace(boolean replace) {
            this.replace = replace;
            return this;
        }

        public Builder<T, R> conditions(ICondition... conditions) {
            Collections.addAll(this.conditions, conditions);
            return this;
        }

        public PortWithConditions<PortDataMapFile<T, R>> build() {
            return new PortWithConditions<>(conditions, new PortDataMapFile<>(replace, values, removals));
        }
    }

    public static class AdvancedBuilder<T, R, VR extends PortDataMapValueRemover<R, T>> extends Builder<T, R> {
        public AdvancedBuilder(PortAdvancedDataMapType<R, T, VR> type) {
            super(type);
        }

        public AdvancedBuilder<T, R, VR> remove(TagKey<R> tag, VR remover) {
            this.removals.add(new PortDataMapEntry.Removal<>(Either.left(tag), Optional.of(remover)));
            return this;
        }

        public AdvancedBuilder<T, R, VR> remove(Holder<R> value, VR remover) {
            this.removals.add(new PortDataMapEntry.Removal<>(Either.right(value.unwrap().orThrow()), Optional.of(remover)));
            return this;
        }

        public AdvancedBuilder<T, R, VR> remove(ResourceLocation id, VR remover) {
            this.removals.add(new PortDataMapEntry.Removal<>(Either.right(ResourceKey.create(registryKey, id)), Optional.of(remover)));
            return this;
        }
    }
}
