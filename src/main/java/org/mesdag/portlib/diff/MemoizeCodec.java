package org.mesdag.portlib.diff;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.NotNull;

@Diff
public
record MemoizeCodec<A>(com.google.common.base.Supplier<Codec<A>> delegate) implements Codec<A> {
    public MemoizeCodec {
        delegate = Suppliers.memoize(delegate);
    }

    @Override
    public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
        return delegate.get().decode(ops, input);
    }

    @Override
    public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
        return delegate.get().encode(input, ops, prefix);
    }

    @Override
    public @NotNull String toString() {
        return "MemoizeCodec[" + delegate.toString() + ']';
    }
}
