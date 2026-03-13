package org.mesdag.portlib.network;

public interface PortStreamCodec<B, V> {
    void encode(B buffer, V value);

    V decode(B buffer);
}
