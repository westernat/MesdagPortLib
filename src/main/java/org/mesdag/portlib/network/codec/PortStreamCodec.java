package org.mesdag.portlib.network.codec;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;
import org.mesdag.portlib.diff.Diff;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface PortStreamCodec<B, V> {
    void encode(B buffer, V value);

    V decode(B buffer);

    @Diff
    default void reversedEncode(V value, B buffer) {
        encode(buffer, value);
    }

    default <O> PortStreamCodec<B, O> apply(PortCodecOperation<B, V, O> operation) {
        return operation.apply(this);
    }

    default <O> PortStreamCodec<B, O> map(Function<? super V, ? extends O> factory, Function<? super O, ? extends V> getter) {
        return new PortStreamCodec<>() {
            @Override
            public O decode(B buffer) {
                return factory.apply(PortStreamCodec.this.decode(buffer));
            }

            @Override
            public void encode(B buffer, O value) {
                PortStreamCodec.this.encode(buffer, getter.apply(value));
            }
        };
    }

    static <B, V> PortStreamCodec<B, V> ofMember(final PortStreamMemberEncoder<B, V> encoder, final PortStreamDecoder<B, V> decoder) {
        return new PortStreamCodec<>() {
            @Override
            public V decode(B buffer) {
                return decoder.decode(buffer);
            }

            @Override
            public void encode(B buffer, V value) {
                encoder.encode(value, buffer);
            }
        };
    }

    static <B, C, T1> PortStreamCodec<B, C> composite(final PortStreamCodec<? super B, T1> codec, final Function<C, T1> getter, final Function<T1, C> factory) {
        return new PortStreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec.decode(buffer);
                return factory.apply(t1);
            }

            @Override
            public void encode(B buffer, C value) {
                codec.encode(buffer, getter.apply(value));
            }
        };
    }

    static <B, C, T1, T2> PortStreamCodec<B, C> composite(
            final PortStreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final PortStreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final BiFunction<T1, T2, C> factory
    ) {
        return new PortStreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                return factory.apply(t1, t2);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
            }
        };
    }

    static <B, C, T1, T2, T3> PortStreamCodec<B, C> composite(
            final PortStreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final PortStreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final PortStreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final Function3<T1, T2, T3, C> factory
    ) {
        return new PortStreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                return factory.apply(t1, t2, t3);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
            }
        };
    }

    static <B, C, T1, T2, T3, T4> PortStreamCodec<B, C> composite(
            final PortStreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final PortStreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final PortStreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final PortStreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final Function4<T1, T2, T3, T4, C> factory
    ) {
        return new PortStreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                return factory.apply(t1, t2, t3, t4);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
            }
        };
    }

    static <B, C, T1, T2, T3, T4, T5> PortStreamCodec<B, C> composite(
            final PortStreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final PortStreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final PortStreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final PortStreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final PortStreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final Function5<T1, T2, T3, T4, T5, C> factory
    ) {
        return new PortStreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
            }
        };
    }

    static <B, C, T1, T2, T3, T4, T5, T6> PortStreamCodec<B, C> composite(
            final PortStreamCodec<? super B, T1> codec1,
            final Function<C, T1> getter1,
            final PortStreamCodec<? super B, T2> codec2,
            final Function<C, T2> getter2,
            final PortStreamCodec<? super B, T3> codec3,
            final Function<C, T3> getter3,
            final PortStreamCodec<? super B, T4> codec4,
            final Function<C, T4> getter4,
            final PortStreamCodec<? super B, T5> codec5,
            final Function<C, T5> getter5,
            final PortStreamCodec<? super B, T6> codec6,
            final Function<C, T6> getter6,
            final Function6<T1, T2, T3, T4, T5, T6, C> factory
    ) {
        return new PortStreamCodec<>() {
            @Override
            public C decode(B buffer) {
                T1 t1 = codec1.decode(buffer);
                T2 t2 = codec2.decode(buffer);
                T3 t3 = codec3.decode(buffer);
                T4 t4 = codec4.decode(buffer);
                T5 t5 = codec5.decode(buffer);
                T6 t6 = codec6.decode(buffer);
                return factory.apply(t1, t2, t3, t4, t5, t6);
            }

            @Override
            public void encode(B buffer, C value) {
                codec1.encode(buffer, getter1.apply(value));
                codec2.encode(buffer, getter2.apply(value));
                codec3.encode(buffer, getter3.apply(value));
                codec4.encode(buffer, getter4.apply(value));
                codec5.encode(buffer, getter5.apply(value));
                codec6.encode(buffer, getter6.apply(value));
            }
        };
    }

    @FunctionalInterface
    interface PortCodecOperation<B, S, T> {
        PortStreamCodec<B, T> apply(PortStreamCodec<B, S> codec);
    }
}
