package PortLib.extensions.com.mojang.serialization.Codec;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.Util;
import org.joml.Vector4f;
import org.mesdag.portlib.diff.MemoizeCodec;
import org.mesdag.portlib.wrapper.datafixers.util.PortEither;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Extension
public class PortCodecExtension {
    private static final Codec<Vector4f> VECTOR4F = Codec.FLOAT.listOf().comapFlatMap(
            list1 -> Util.fixedSize(list1, 4).map(list2 -> new Vector4f(list2.get(0), list2.get(1), list2.get(2), list2.get(3))),
            vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w())
    );

    @Extension
    public static Codec<Vector4f> vector4f() {
        return VECTOR4F;
    }

    @Extension
    public static <A> Codec<A> lazyInitialized(Supplier<Codec<A>> delegate) {
        return new MemoizeCodec<>(delegate::get);
    }

    public static <A> Codec<A> validate(@This Codec<A> thiz, Function<A, DataResult<A>> checker) {
        return thiz.flatXmap(checker, checker);
    }

    public static <A> MapCodec<Optional<A>> lenientOptionalFieldOf(@This Codec<A> thiz, String name) {
        return Codec.optionalField(name, thiz);
    }

    public static <A> MapCodec<A> lenientOptionalFieldOf(@This Codec<A> thiz, String name, A defaultValue) {
        return Codec.optionalField(name, thiz).xmap(
                o -> o.orElse(defaultValue),
                a -> Objects.equals(a, defaultValue) ? Optional.empty() : Optional.of(a)
        );
    }

    @Extension
    public static <T> Codec<T> withAlternative(Codec<T> primary, Codec<? extends T> alternative) {
        return Codec.either(primary, alternative).xmap(PortEither::unwrap, Either::left);
    }

    @Extension
    public static <T, U> Codec<T> withAlternative(Codec<T> primary, Codec<U> alternative, Function<U, T> converter) {
        return Codec.either(primary, alternative).xmap(either -> either.map(v -> v, converter), Either::left);
    }
}
