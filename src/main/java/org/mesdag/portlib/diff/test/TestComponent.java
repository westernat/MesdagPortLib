package org.mesdag.portlib.diff.test;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public record TestComponent(int value) {
    public static final Codec<TestComponent> CODEC = Codec.INT.xmap(TestComponent::new, TestComponent::value);
    public static final PortStreamCodec<ByteBuf, TestComponent> STREAM_CODEC = PortByteBufCodecs.VAR_INT.map(TestComponent::new, TestComponent::value);
}
