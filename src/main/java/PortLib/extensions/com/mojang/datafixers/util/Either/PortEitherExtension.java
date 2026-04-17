package PortLib.extensions.com.mojang.datafixers.util.Either;

import com.mojang.datafixers.util.Either;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.function.Function;

@Extension
public class PortEitherExtension {
    public static <U> U unwrap(@This Either<? extends U, ? extends U> either) {
        return either.map(Function.identity(), Function.identity());
    }
}
