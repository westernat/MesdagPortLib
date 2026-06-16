package PortLib.extensions.com.mojang.serialization.Codec;

import PortLib.extensions.com.mojang.datafixers.util.Either.PortEitherExtension;
import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.BaseMapCodec;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import org.joml.Vector4f;
import org.mesdag.portlib.diff.MemoizeCodec;
import org.mesdag.portlib.util.Static;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PortCodecExtension {
    private static final Codec<Vector4f> VECTOR4F = Codec.FLOAT.listOf().comapFlatMap(
            list1 -> Util.fixedSize(list1, 4).map(list2 -> new Vector4f(list2.get(0), list2.get(1), list2.get(2), list2.get(3))),
            vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w())
    );

    public static Codec<Vector4f> vector4f() {
        return VECTOR4F;
    }

    @Static
    public static <A> Codec<A> lazyInitialized(Supplier<Codec<A>> delegate) {
        return new MemoizeCodec<>(delegate::get);
    }

    public static <A> Codec<A> validate(Codec<A> thiz, Function<A, DataResult<A>> checker) {
        return thiz.flatXmap(checker, checker);
    }

    public static <A> MapCodec<Optional<A>> lenientOptionalFieldOf(Codec<A> thiz, String name) {
        return Codec.optionalField(name, thiz);
    }

    public static <A> MapCodec<A> lenientOptionalFieldOf(Codec<A> thiz, String name, A defaultValue) {
        return Codec.optionalField(name, thiz).xmap(
                o -> o.orElse(defaultValue),
                a -> Objects.equals(a, defaultValue) ? Optional.empty() : Optional.of(a)
        );
    }

    @Static
    public static <T> Codec<T> withAlternative(Codec<T> primary, Codec<? extends T> alternative) {
        return Codec.either(primary, alternative).xmap(PortEitherExtension::unwrap, Either::left);
    }

    @Static
    public static <T, U> Codec<T> withAlternative(Codec<T> primary, Codec<U> alternative, Function<U, T> converter) {
        return Codec.either(primary, alternative).xmap(either -> either.map(v -> v, converter), Either::left);
    }

    @Static
    public static <K, V> StrictUnboundedMapCodec<K, V> strictUnboundedMap(Codec<K> key, Codec<V> value) {
        return new StrictUnboundedMapCodec<>(key, value);
    }

    @Static
    public static <K, V> Codec<Map<K, V>> dispatchedMap(Codec<K> keyCodec, Function<K, Codec<? extends V>> valueCodecFunction) {
        return new DispatchedMapCodec<>(keyCodec, valueCodecFunction);
    }

    @Static
    public static <A> Codec<Optional<A>> optionalEmptyMap(Codec<A> codec) {
        return new Codec<>() {
            @Override
            public <T> DataResult<Pair<Optional<A>, T>> decode(DynamicOps<T> ops, T input) {
                Optional<MapLike<T>> optional = ops.getMap(input).result();
                return optional.isPresent() && optional.get().entries().findAny().isEmpty()
                        ? DataResult.success(Pair.of(Optional.empty(), input))
                        : codec.decode(ops, input).map(pair -> pair.mapFirst(Optional::of));
            }

            public <T> DataResult<T> encode(Optional<A> input, DynamicOps<T> ops, T prefix) {
                return input.isEmpty() ? DataResult.success(ops.emptyMap()) : codec.encode(input.get(), ops, prefix);
            }
        };
    }

    @Static
    public static <A> Codec<List<A>> listWithOptionalElements(Codec<Optional<A>> elementCodec) {
        return listWithoutEmpty(elementCodec.listOf());
    }

    @Static
    public static <A> Codec<List<A>> listWithoutEmpty(Codec<List<Optional<A>>> codec) {
        return codec.xmap(
                list -> list.stream().filter(Optional::isPresent).map(Optional::get).toList(),
                list -> list.stream().map(Optional::of).toList()
        );
    }

    @Static
    public static <A> Codec<A> decodeOnly(Decoder<A> decoder) {
        return Codec.of(Codec.unit(() -> {
            throw new UnsupportedOperationException("Cannot encode with decode-only codec! Decoder:" + decoder);
        }), decoder, "DecodeOnly[" + decoder + "]");
    }

    public static Codec<TemporalAccessor> temporalCodec(DateTimeFormatter dateTimeFormatter) {
        return Codec.STRING.comapFlatMap(s -> {
            try {
                return DataResult.success(dateTimeFormatter.parse(s));
            } catch (Exception exception) {
                return DataResult.error(exception::getMessage);
            }
        }, dateTimeFormatter::format);
    }

    @Static
    public static <A> Codec<Object2BooleanMap<A>> object2BooleanMap(Codec<A> codec) {
        return Codec.unboundedMap(codec, Codec.BOOL).xmap(Object2BooleanOpenHashMap::new, Object2ObjectOpenHashMap::new);
    }

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
}
