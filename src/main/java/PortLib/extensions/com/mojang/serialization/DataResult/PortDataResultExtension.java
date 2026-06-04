package PortLib.extensions.com.mojang.serialization.DataResult;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;

import java.util.function.Function;

public class PortDataResultExtension {
    public static <R, E extends Throwable> R getOrThrow(DataResult<R> thiz, Function<String, E> exceptionSupplier) throws E {
        Either<R, DataResult.PartialResult<R>> either = thiz.get();
        if (either.left().isPresent()) {
            return either.left().get();
        }
        throw exceptionSupplier.apply(either.right().map(DataResult.PartialResult::message).orElse("Unknown problem"));
    }

    public static <R, E extends Throwable> R getOrThrow(DataResult<R> thiz) {
        return getOrThrow(thiz, IllegalStateException::new);
    }
}
