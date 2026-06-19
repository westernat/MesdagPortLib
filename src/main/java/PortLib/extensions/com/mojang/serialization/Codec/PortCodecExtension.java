package PortLib.extensions.com.mojang.serialization.Codec;

import PortLib.extensions.com.mojang.datafixers.util.Either.PortEitherExtension;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
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

public class PortCodecExtension {
    private static final Codec<Vector4f> VECTOR4F = Codec.FLOAT.listOf().comapFlatMap(
            list1 -> Util.fixedSize(list1, 4).map(list2 -> new Vector4f(list2.get(0), list2.get(1), list2.get(2), list2.get(3))),
            vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w())
    );

    public static Codec<Vector4f> vector4f() {
        return VECTOR4F;
    }

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

    public static <A> Codec<List<A>> listWithOptionalElements(Codec<Optional<A>> elementCodec) {
        return listWithoutEmpty(elementCodec.listOf());
    }

    public static <A> Codec<List<A>> listWithoutEmpty(Codec<List<Optional<A>>> codec) {
        return codec.xmap(
                list -> list.stream().filter(Optional::isPresent).map(Optional::get).toList(),
                list -> list.stream().map(Optional::of).toList()
        );
    }

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

    public static <A> Codec<A> json(Function<A, ? extends JsonElement> encoder, Function<? super JsonElement, A> decoder) {
        return new Codec<>() {
            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
                return DataResult.success(new Pair<>(decoder.apply(ops.convertTo(JsonOps.INSTANCE, input)), input), Lifecycle.stable());
            }

            @Override
            public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
                return DataResult.success(JsonOps.INSTANCE.convertTo(ops, encoder.apply(input)), Lifecycle.stable());
            }
        };
    }
}
