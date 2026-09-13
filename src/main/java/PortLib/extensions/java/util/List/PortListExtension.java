package PortLib.extensions.java.util.List;

import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

@SuppressWarnings("all")
public class PortListExtension {
    public static <E> E getFirst(List<E> thiz) {
        if (thiz instanceof Deque<?> deque) {
            return (E) deque.getFirst();
        }
        if (thiz.isEmpty()) {
            throw new NoSuchElementException();
        }
        return thiz.get(0);
    }

    public static <E> E getLast(List<E> thiz) {
        if (thiz instanceof Deque<?> deque) {
            return (E) deque.getLast();
        }
        if (thiz.isEmpty()) {
            throw new NoSuchElementException();
        }
        return thiz.get(thiz.size() - 1);
    }

    public static <E> void addFirst(List<E> thiz, E e) {
        if (thiz instanceof Deque deque) {
            deque.addFirst(e);
        } else {
            thiz.add(0, e);
        }
    }

    public static <E> void addLast(List<E> thiz, E e) {
        if (thiz instanceof Deque deque) {
            deque.addLast(e);
        } else {
            thiz.add(e);
        }
    }

    public static <E> E removeFirst(List<E> thiz) {
        if (thiz instanceof Deque<?> deque) {
            return (E) deque.removeFirst();
        }
        if (thiz.isEmpty()) {
            throw new NoSuchElementException();
        }
        return thiz.remove(0);
    }

    public static <E> E removeLast(List<E> thiz) {
        if (thiz instanceof Deque<?> deque) {
            return (E) deque.removeLast();
        }
        if (thiz.isEmpty()) {
            throw new NoSuchElementException();
        }
        return thiz.remove(thiz.size() - 1);
    }
}
