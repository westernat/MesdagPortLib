package PortLib.extensions.com.mojang.datafixers.util.Either;

import com.mojang.datafixers.util.Either;
import manifold.ext.rt.api.Extension;

import java.util.function.Function;

@Extension
public class PortEitherExtension {
    @Extension
    public static <L extends U, R extends U, U> U unwrap(Either<L, R> either) {
        return either.map(Function.identity(), Function.identity());
    }
}
