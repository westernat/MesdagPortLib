package org.mesdag.portlib.util;

import org.jetbrains.annotations.Unmodifiable;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

@Unmodifiable
public class TransformSet<F, T> extends AbstractSet<T> {
    private final Set<F> fromSet;
    private final Function<? super F, ? extends T> function;

    public TransformSet(Set<F> fromSet, Function<? super F, ? extends T> function) {
        this.fromSet = fromSet;
        this.function = function;
    }

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
        };
    }

    @Override
    public int size() {
        return fromSet.size();
    }
}
