package org.mesdag.portlib.network.codec;

@FunctionalInterface
public interface PortStreamEncoder<O, T> {
    void encode(O buffer, T value);
}
