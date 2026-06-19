package PortLib.extensions.com.mojang.serialization.Codec;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.BaseMapCodec;

import java.util.Map;
import java.util.Optional;

public record StrictUnboundedMapCodec<K, V>(
        Codec<K> keyCodec,
        Codec<V> elementCodec
) implements Codec<Map<K, V>>, BaseMapCodec<K, V> {
    @Override
    public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        for (Pair<T, T> pair : input.entries().toList()) {
            DataResult<K> resultK = keyCodec().parse(ops, pair.getFirst());
            DataResult<V> resultV = elementCodec().parse(ops, pair.getSecond());
            DataResult<Pair<K, V>> resultKV = resultK.apply2stable(Pair::of, resultV);
            Optional<DataResult.PartialResult<Pair<K, V>>> optional = resultKV.error();
            if (optional.isPresent()) {
                String s = optional.get().message();
                return DataResult.error(() -> resultK.result().isPresent() ? "Map entry '" + resultK.result().get() + "' : " + s : s);
            }
            if (resultKV.result().isEmpty()) {
                return DataResult.error(() -> "Empty or invalid map contents are not allowed");
            }
            Pair<K, V> kv = resultKV.result().get();
            builder.put(kv.getFirst(), kv.getSecond());
        }
        return DataResult.success(builder.build());
    }

    @Override
    public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getMap(input)
                .setLifecycle(Lifecycle.stable())
                .flatMap(mapLike -> decode(ops, mapLike))
                .map(map -> Pair.of(map, input));
    }

    public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T value) {
        return encode(input, ops, ops.mapBuilder()).build(value);
    }

    @Override
    public String toString() {
        return "StrictUnboundedMapCodec[" + keyCodec + " -> " + elementCodec + "]";
    }
}
