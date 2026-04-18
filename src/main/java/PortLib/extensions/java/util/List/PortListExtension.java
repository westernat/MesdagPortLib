package PortLib.extensions.java.util.List;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Extension
public class PortListExtension {
    public static <E> E getFirst(@This List<E> thiz) {
        if (thiz instanceof LinkedList<E> linkedList) {
            return linkedList.getFirst();
        }
        if (thiz.isEmpty()) {
            throw new NoSuchElementException();
        }
        return thiz.get(0);
    }

    public static <E> E getLast(@This List<E> thiz) {
        if (thiz instanceof LinkedList<E> linkedList) {
            return linkedList.getLast();
        }
        if (thiz.isEmpty()) {
            throw new NoSuchElementException();
        }
        return thiz.get(thiz.size() - 1);
    }

    public static <E> void addFirst(@This List<E> thiz, E e) {
        if (thiz instanceof LinkedList<E> linkedList) {
            linkedList.addFirst(e);
        } else {
            thiz.add(0, e);
        }
    }

    public static <E> void addLast(@This List<E> thiz, E e) {
        if (thiz instanceof LinkedList<E> linkedList) {
            linkedList.addLast(e);
        } else {
            thiz.add(e);
        }
    }
}
