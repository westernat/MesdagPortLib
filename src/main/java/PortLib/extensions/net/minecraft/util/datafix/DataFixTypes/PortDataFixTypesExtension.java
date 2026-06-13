package PortLib.extensions.net.minecraft.util.datafix.DataFixTypes;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.datafix.DataFixTypes;

public class PortDataFixTypesExtension {
    public static <A> Codec<A> wrapCodec(DataFixTypes thiz, Codec<A> codec, DataFixer dataFixer, int dataVersion) {
        return new Codec<>() {
            @Override
            public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T value) {
                return codec.encode(input, ops, value).flatMap(t -> ops.mergeToMap(t, ops.createString("DataVersion"), ops.createInt(DataFixTypes.currentVersion())));
            }

            @Override
            public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T value) {
                int version = ops.get(value, "DataVersion").flatMap(ops::getNumberValue).map(Number::intValue).result().orElse(dataVersion);
                Dynamic<T> input = new Dynamic<>(ops, ops.remove(value, "DataVersion"));
                Dynamic<T> outpu = thiz.updateToCurrentVersion(dataFixer, input, version);
                return codec.decode(outpu);
            }
        };
    }
}
