package org.mesdag.portlib.network.codec;

@FunctionalInterface
public interface PortStreamMemberEncoder<O, T> {
    void encode(T value, O output);
}
