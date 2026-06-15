package PortLib.extensions.com.mojang.datafixers.util.Either;

import com.mojang.datafixers.util.Either;
import org.mesdag.portlib.util.Static;

import java.util.function.Function;

public class PortEitherExtension {
    @Static
    public static <L extends U, R extends U, U> U unwrap(Either<L, R> either) {
        return either.map(Function.identity(), Function.identity());
    }
}
