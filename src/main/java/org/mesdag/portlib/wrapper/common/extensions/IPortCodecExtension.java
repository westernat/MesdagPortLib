package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("all")
public interface IPortCodecExtension<A> {

    private Codec<A> self() {
        return (Codec<A>) this;
    }

    default Codec<A> validate(Function<A, DataResult<A>> checker) {
        return PortCodecExtension.validate(self(), checker);
    }

    default MapCodec<Optional<A>> lenientOptionalFieldOf(String name) {
        return PortCodecExtension.lenientOptionalFieldOf(self(), name);
    }

    default MapCodec<A> lenientOptionalFieldOf(String name, A defaultValue) {
        return PortCodecExtension.lenientOptionalFieldOf(self(), name, defaultValue);
    }

    static <A> IPortCodecExtension<A> of(Codec<A> codec) {
        return (IPortCodecExtension<A>) codec;
    }
}
