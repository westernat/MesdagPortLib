package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public interface IPortStringRepresentableExtension {
    static <T extends StringRepresentable> Codec<T> fromValues(Supplier<T[]> valuesSupplier) {
        T[] at = valuesSupplier.get();
        Function<String, T> function = createNameLookup(at, p_304333_ -> p_304333_);
        ToIntFunction<T> tointfunction = Util.createIndexLookup(Arrays.asList(at));
        return new StringRepresentableCodec<>(at, function, tointfunction);
    }

    static <T extends StringRepresentable> Function<String, T> createNameLookup(T[] values, Function<String, String> keyFunction) {
        if (values.length > 16) {
            Map<String, T> map = Arrays.<StringRepresentable>stream(values).collect(Collectors.toMap(sr -> keyFunction.apply(sr.getSerializedName()), sr1 -> (T) sr1));
            return s -> s == null ? null : map.get(s);
        }
        return s -> {
            for (T t : values) {
                if (keyFunction.apply(t.getSerializedName()).equals(s)) {
                    return t;
                }
            }
            return null;
        };
    }

    class StringRepresentableCodec<S extends StringRepresentable> implements Codec<S> {
        private final Codec<S> codec;

        public StringRepresentableCodec(S[] values, Function<String, S> nameLookup, ToIntFunction<S> indexLookup) {
            this.codec = ExtraCodecs.orCompressed(
                    PortCodecExtension.stringResolver(StringRepresentable::getSerializedName, nameLookup),
                    ExtraCodecs.idResolverCodec(indexLookup, p_304986_ -> p_304986_ >= 0 && p_304986_ < values.length ? values[p_304986_] : null, -1)
            );
        }

        @Override
        public <T> DataResult<Pair<S, T>> decode(DynamicOps<T> ops, T value) {
            return this.codec.decode(ops, value);
        }

        public <T> DataResult<T> encode(S input, DynamicOps<T> ops, T prefix) {
            return this.codec.encode(input, ops, prefix);
        }
    }
}
