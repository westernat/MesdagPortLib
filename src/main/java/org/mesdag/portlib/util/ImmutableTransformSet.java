package org.mesdag.portlib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

@UnmodifiableView
public class ImmutableTransformSet<F, T> extends AbstractSet<T> {
    private final Set<F> fromSet;
    private final Function<? super F, ? extends T> function;

    public ImmutableTransformSet(Set<F> fromSet, Function<? super F, ? extends T> function) {
        this.fromSet = fromSet;
        this.function = function;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        Iterator<F> iterator = fromSet.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return function.apply(iterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove is not supported in ImmutableTransformSet");
            }
        };
    }

    @Override
    public int size() {
        return fromSet.size();
    }
}