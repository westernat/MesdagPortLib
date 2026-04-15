package org.mesdag.portlib.wrapper.serialization;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector4f;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.datafixers.util.PortEither;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PortCodec {
    Codec<Vector4f> VECTOR4F = Codec.FLOAT.listOf().comapFlatMap(
            list1 -> Util.fixedSize(list1, 4).map(list2 -> new Vector4f(list2.get(0), list2.get(1), list2.get(2), list2.get(3))),
            vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w())
    );

    static <A> Codec<A> lazyInitialized(Supplier<Codec<A>> delegate) {
        return new MemoizeCodec<>(delegate::get);
    }

    static <A> Codec<A> validate(Codec<A> codec, Function<A, DataResult<A>> checker) {
        return codec.flatXmap(checker, checker);
    }

    static <A> MapCodec<Optional<A>> lenientOptionalFieldOf(Codec<A> codec, String name) {
        return Codec.optionalField(name, codec);
    }

    static <A> MapCodec<A> lenientOptionalFieldOf(Codec<A> codec, String name, A defaultValue) {
        return Codec.optionalField(name, codec).xmap(
                o -> o.orElse(defaultValue),
                a -> Objects.equals(a, defaultValue) ? Optional.empty() : Optional.of(a)
        );
    }    static <T> Codec<T> withAlternative(final Codec<T> primary, final Codec<? extends T> alternative) {
        return Codec.either(primary, alternative).xmap(PortEither::unwrap, Either::left);
    }

    static <T, U> Codec<T> withAlternative(final Codec<T> primary, final Codec<U> alternative, final Function<U, T> converter) {
        return Codec.either(primary, alternative).xmap(either -> either.map(v -> v, converter), Either::left);
    }

    @Diff
    record MemoizeCodec<A>(com.google.common.base.Supplier<Codec<A>> delegate) implements Codec<A> {
        public MemoizeCodec {
            delegate = Suppliers.memoize(delegate);
        }

        @Override
        public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
            return delegate.get().decode(ops, input);
        }

        @Override
        public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
            return delegate.get().encode(input, ops, prefix);
        }

        @Override
        public @NotNull String toString() {
            return "MemoizeCodec[" + delegate.toString() + ']';
        }
    }
}
