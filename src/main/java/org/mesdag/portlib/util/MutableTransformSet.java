package org.mesdag.portlib.util;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public class MutableTransformSet<F, T> extends AbstractSet<T> {
    private final Set<F> fromSet;
    private final Function<? super F, ? extends T> toFunction;
    private final Function<? super T, ? extends F> fromFunction;
    private final Runnable onDirty;

    MutableTransformSet(
            Set<F> fromSet,
            Function<? super F, ? extends T> toFunction,
            Function<? super T, ? extends F> fromFunction,
            Runnable onDirty
    ) {
        this.fromSet = fromSet;
        this.toFunction = toFunction;
        this.fromFunction = fromFunction;
        this.onDirty = onDirty;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;

        try {
            @SuppressWarnings("unchecked")
            T casted = (T) o;
            return fromSet.contains(fromFunction.apply(casted));
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        try {
            @SuppressWarnings("unchecked")
            T casted = (T) o;
            F original = fromFunction.apply(casted);
            if (fromSet.remove(original)) {
                onDirty.run();
                return true;
            }
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
        return false;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        Iterator<F> it = fromSet.iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public T next() {
                return toFunction.apply(it.next());
            }

            @Override
            public void remove() {
                it.remove();
                onDirty.run();
            }
        };
    }

    @Override
    public int size() {
        return fromSet.size();
    }

    @Override
    public void clear() {
        if (!fromSet.isEmpty()) {
            fromSet.clear();
            onDirty.run();
        }
    }
}