package org.mesdag.portlib.network.codec;

@FunctionalInterface
public interface PortStreamDecoder<I, T> {
    T decode(I buffer);
}
