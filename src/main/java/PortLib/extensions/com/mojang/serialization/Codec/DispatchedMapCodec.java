package PortLib.extensions.com.mojang.serialization.Codec;

import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public record DispatchedMapCodec<K, V>(
        Codec<K> keyCodec,
        Function<K, Codec<? extends V>> valueCodecFunction
) implements Codec<Map<K, V>> {
    @Override
    public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) {
        RecordBuilder<T> mapBuilder = ops.mapBuilder();
        for (Map.Entry<K, V> entry : input.entrySet()) {
            mapBuilder.add(keyCodec.encodeStart(ops, entry.getKey()), encodeValue(valueCodecFunction.apply(entry.getKey()), entry.getValue(), ops));
        }
        return mapBuilder.build(prefix);
    }

    @SuppressWarnings("unchecked")
    private <T, V2 extends V> DataResult<T> encodeValue(Codec<V2> codec, V input, DynamicOps<T> ops) {
        return codec.encodeStart(ops, (V2) input);
    }

    @Override
    public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
        return ops.getMap(input).flatMap(map -> {
            Map<K, V> entries = new Object2ObjectArrayMap<>();
            Stream.Builder<Pair<T, T>> failed = Stream.builder();

            DataResult<Unit> finalResult = map.entries().reduce(
                    DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                    (result, entry) -> parseEntry(result, ops, entry, entries, failed),
                    (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
            );

            Pair<Map<K, V>, T> pair = Pair.of(ImmutableMap.copyOf(entries), input);
            T errors = ops.createMap(failed.build());

            return finalResult.map(ignored -> pair).setPartial(pair).mapError(error -> error + " missed input: " + errors);
        });
    }

    private <T> DataResult<Unit> parseEntry(DataResult<Unit> result, DynamicOps<T> ops, Pair<T, T> input, Map<K, V> entries, Stream.Builder<Pair<T, T>> failed) {
        DataResult<K> keyResult = keyCodec.parse(ops, input.getFirst());
        DataResult<V> valueResult = keyResult.map(valueCodecFunction).flatMap(valueCodec -> valueCodec.parse(ops, input.getSecond()).map(Function.identity()));
        DataResult<Pair<K, V>> entryResult = keyResult.apply2stable(Pair::of, valueResult);

        Optional<Pair<K, V>> entry = PortDataResultExtension.resultOrPartial(entryResult);
        if (entry.isPresent()) {
            K key = entry.get().getFirst();
            V value = entry.get().getSecond();
            if (entries.putIfAbsent(key, value) != null) {
                failed.add(input);
                return result.apply2stable((u, p) -> u, DataResult.error(() -> "Duplicate entry for key: '" + key + "'"));
            }
        }
        if (entryResult.error().isPresent()) {
            failed.add(input);
        }

        return result.apply2stable((u, p) -> u, entryResult);
    }
}
