package PortLib.extensions.com.mojang.datafixers.util.Either;

import com.mojang.datafixers.util.Either;

import java.util.function.Function;

public class PortEitherExtension {
    public static <L extends U, R extends U, U> U unwrap(Either<L, R> either) {
        return either.map(Function.identity(), Function.identity());
    }
}
