package PortLib.extensions.com.mojang.serialization.DataResult;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;

import java.util.Optional;
import java.util.function.Function;

public class PortDataResultExtension {
    public static <R, E extends Throwable> R getOrThrow(DataResult<R> thiz, Function<String, E> exceptionSupplier) throws E {
        Either<R, DataResult.PartialResult<R>> either = thiz.get();
        if (either.left().isPresent()) {
            return either.left().get();
        }
        throw exceptionSupplier.apply(either.right().map(DataResult.PartialResult::message).orElse("Unknown problem"));
    }

    public static <R> R getOrThrow(DataResult<R> thiz) {
        return getOrThrow(thiz, IllegalStateException::new);
    }

    public static <V, K> Optional<Pair<K, V>> resultOrPartial(DataResult<Pair<K, V>> thiz) {
        return thiz.get().map(Optional::of, r -> Optional.empty());
    }
}
