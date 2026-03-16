package org.mesdag.portlib.wrapper.serialization;

import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public interface PortCodec {
    static <A> Codec<A> lazyInitialized(Supplier<Codec<A>> delegate) {
        return Codec.lazyInitialized(delegate);
    }
}
