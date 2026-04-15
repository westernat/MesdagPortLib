package org.mesdag.portlib.wrapper.serialization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector4f;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PortCodec {
    Codec<Vector4f> VECTOR4F = ExtraCodecs.VECTOR4F;

    static <A> Codec<A> lazyInitialized(Supplier<Codec<A>> delegate) {
        return Codec.lazyInitialized(delegate);
    }

    static <A> Codec<A> validate(Codec<A> codec, Function<A, DataResult<A>> checker) {
        return codec.validate(checker);
    }

    static <A> MapCodec<Optional<A>> lenientOptionalFieldOf(Codec<A> codec, String name) {
        return codec.lenientOptionalFieldOf(name);
    }

    static <A> MapCodec<A> lenientOptionalFieldOf(Codec<A> codec, String name, A defaultValue) {
        return codec.lenientOptionalFieldOf(name, defaultValue);
    }

    static <T> Codec<T> withAlternative(Codec<T> primary, Codec<? extends T> alternative) {
        return Codec.withAlternative(primary, alternative);
    }

    static <T, U> Codec<T> withAlternative(Codec<T> primary, Codec<U> alternative, Function<U, T> converter) {
        return Codec.withAlternative(primary, alternative, converter);
    }
}
