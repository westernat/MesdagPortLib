package PortLib.extensions.com.mojang.datafixers.util.Either;

import com.mojang.datafixers.util.Either;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.function.Function;

@Extension
public class PortEitherExtension {
    public static <L extends U, R extends U, U> U unwrap(@This Either<L, R> thiz) {
        return thiz.map(Function.identity(), Function.identity());
    }
}
