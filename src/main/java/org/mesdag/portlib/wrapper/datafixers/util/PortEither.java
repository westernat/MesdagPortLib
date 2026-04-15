package org.mesdag.portlib.wrapper.datafixers.util;

import com.mojang.datafixers.util.Either;

import java.util.function.Function;

public class PortEither {
    public static <U> U unwrap(Either<? extends U, ? extends U> either) {
        return either.map(Function.identity(), Function.identity());
    }
}
