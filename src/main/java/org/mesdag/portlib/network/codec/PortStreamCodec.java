package org.mesdag.portlib.network.codec;

public interface PortStreamCodec<B, V> {
    void encode(B buffer, V value);

    V decode(B buffer);

    @FunctionalInterface
    interface CodecOperation<B, S, T> {
        PortStreamCodec<B, T> apply(PortStreamCodec<B, S> codec);
    }
}
