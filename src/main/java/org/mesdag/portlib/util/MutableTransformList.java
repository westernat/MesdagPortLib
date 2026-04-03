package org.mesdag.portlib.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

public class MutableTransformList<F, T> extends AbstractList<T> {
    private final List<F> fromList;
    private final Function<? super F, ? extends T> toFunction;
    private final Function<? super T, ? extends F> fromFunction;
    private final Runnable onDirty;

    MutableTransformList(
            List<F> fromList,
            Function<? super F, ? extends T> toFunction,
            Function<? super T, ? extends F> fromFunction,
            Runnable onDirty
    ) {
        this.fromList = fromList;
        this.toFunction = toFunction;
        this.fromFunction = fromFunction;
        this.onDirty = onDirty;
    }

    @Override
    public T get(int index) {
        return toFunction.apply(fromList.get(index));
    }

    @Override
    public int size() {
        return fromList.size();
    }

    @Override
    public T set(int index, T element) {
        F original = fromFunction.apply(element);
        F oldElement = fromList.set(index, original);
        onDirty.run();
        return toFunction.apply(oldElement);
    }

    @Override
    public void add(int index, T element) {
        F original = fromFunction.apply(element);
        fromList.add(index, original);
        onDirty.run();
    }

    @Override
    public T remove(int index) {
        F removed = fromList.remove(index);
        onDirty.run();
        return toFunction.apply(removed);
    }

    @Override
    public Iterator<T> iterator() {
        Iterator<F> it = fromList.iterator();
        return new Iterator<T>() {
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
    public ListIterator<T> listIterator(int index) {
        ListIterator<F> it = fromList.listIterator(index);
        return new ListIterator<T>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public T next() {
                return toFunction.apply(it.next());
            }

            @Override
            public boolean hasPrevious() {
                return it.hasPrevious();
            }

            @Override
            public T previous() {
                return toFunction.apply(it.previous());
            }

            @Override
            public int nextIndex() {
                return it.nextIndex();
            }

            @Override
            public int previousIndex() {
                return it.previousIndex();
            }

            @Override
            public void remove() {
                it.remove();
                onDirty.run();
            }

            @Override
            public void set(T t) {
                it.set(fromFunction.apply(t));
                onDirty.run();
            }

            @Override
            public void add(T t) {
                it.add(fromFunction.apply(t));
                onDirty.run();
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return fromList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;

        try {
            @SuppressWarnings("unchecked")
            T casted = (T) o;
            return fromList.contains(fromFunction.apply(casted));
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) return -1;

        try {
            @SuppressWarnings("unchecked")
            T casted = (T) o;
            F original = fromFunction.apply(casted);
            return fromList.indexOf(original);
        } catch (ClassCastException | NullPointerException e) {
            return -1;
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) return -1;

        try {
            @SuppressWarnings("unchecked")
            T casted = (T) o;
            F original = fromFunction.apply(casted);
            return fromList.lastIndexOf(original);
        } catch (ClassCastException | NullPointerException e) {
            return -1;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) return false;

        try {
            @SuppressWarnings("unchecked")
            T casted = (T) o;
            F original = fromFunction.apply(casted);
            if (fromList.remove(original)) {
                onDirty.run();
                return true;
            }
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
        return false;
    }

    @Override
    public void clear() {
        if (!fromList.isEmpty()) {
            fromList.clear();
            onDirty.run();
        }
    }
}
