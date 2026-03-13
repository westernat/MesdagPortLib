package org.mesdag.portlib.network.codec;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import com.mojang.datafixers.util.Function6;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface PortStreamCodec<B, V> {
    void encode(B buffer, V value);

    V decode(B buffer);

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

    static <B, C, T1> PortStreamCodec<B, C> composite(final PortStreamCodec<? super B, T1> codec, final Function<C, T1> getter, final Function<T1, C> factory) {
        return new PortStreamCodec<B, C>() {
            @Override
            public C decode(B p_320924_) {
                T1 t1 = codec.decode(p_320924_);
                return factory.apply(t1);
            }

            @Override
            public void encode(B p_320798_, C p_320749_) {
                codec.encode(p_320798_, getter.apply(p_320749_));
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
        return new PortStreamCodec<B, C>() {
            @Override
            public C decode(B p_320168_) {
                T1 t1 = codec1.decode(p_320168_);
                T2 t2 = codec2.decode(p_320168_);
                return factory.apply(t1, t2);
            }

            @Override
            public void encode(B p_320592_, C p_320163_) {
                codec1.encode(p_320592_, getter1.apply(p_320163_));
                codec2.encode(p_320592_, getter2.apply(p_320163_));
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
        return new PortStreamCodec<B, C>() {
            @Override
            public C decode(B p_320842_) {
                T1 t1 = codec1.decode(p_320842_);
                T2 t2 = codec2.decode(p_320842_);
                T3 t3 = codec3.decode(p_320842_);
                return factory.apply(t1, t2, t3);
            }

            @Override
            public void encode(B p_320737_, C p_320439_) {
                codec1.encode(p_320737_, getter1.apply(p_320439_));
                codec2.encode(p_320737_, getter2.apply(p_320439_));
                codec3.encode(p_320737_, getter3.apply(p_320439_));
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
        return new PortStreamCodec<B, C>() {
            @Override
            public C decode(B p_323859_) {
                T1 t1 = codec1.decode(p_323859_);
                T2 t2 = codec2.decode(p_323859_);
                T3 t3 = codec3.decode(p_323859_);
                T4 t4 = codec4.decode(p_323859_);
                return factory.apply(t1, t2, t3, t4);
            }

            @Override
            public void encode(B p_323667_, C p_323469_) {
                codec1.encode(p_323667_, getter1.apply(p_323469_));
                codec2.encode(p_323667_, getter2.apply(p_323469_));
                codec3.encode(p_323667_, getter3.apply(p_323469_));
                codec4.encode(p_323667_, getter4.apply(p_323469_));
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
        return new PortStreamCodec<B, C>() {
            @Override
            public C decode(B p_324610_) {
                T1 t1 = codec1.decode(p_324610_);
                T2 t2 = codec2.decode(p_324610_);
                T3 t3 = codec3.decode(p_324610_);
                T4 t4 = codec4.decode(p_324610_);
                T5 t5 = codec5.decode(p_324610_);
                return factory.apply(t1, t2, t3, t4, t5);
            }

            @Override
            public void encode(B p_323786_, C p_323619_) {
                codec1.encode(p_323786_, getter1.apply(p_323619_));
                codec2.encode(p_323786_, getter2.apply(p_323619_));
                codec3.encode(p_323786_, getter3.apply(p_323619_));
                codec4.encode(p_323786_, getter4.apply(p_323619_));
                codec5.encode(p_323786_, getter5.apply(p_323619_));
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
        return new PortStreamCodec<B, C>() {
            @Override
            public C decode(B p_330310_) {
                T1 t1 = codec1.decode(p_330310_);
                T2 t2 = codec2.decode(p_330310_);
                T3 t3 = codec3.decode(p_330310_);
                T4 t4 = codec4.decode(p_330310_);
                T5 t5 = codec5.decode(p_330310_);
                T6 t6 = codec6.decode(p_330310_);
                return factory.apply(t1, t2, t3, t4, t5, t6);
            }

            @Override
            public void encode(B p_332052_, C p_331912_) {
                codec1.encode(p_332052_, getter1.apply(p_331912_));
                codec2.encode(p_332052_, getter2.apply(p_331912_));
                codec3.encode(p_332052_, getter3.apply(p_331912_));
                codec4.encode(p_332052_, getter4.apply(p_331912_));
                codec5.encode(p_332052_, getter5.apply(p_331912_));
                codec6.encode(p_332052_, getter6.apply(p_331912_));
            }
        };
    }

    @FunctionalInterface
    interface PortCodecOperation<B, S, T> {
        PortStreamCodec<B, T> apply(PortStreamCodec<B, S> codec);
    }
}
