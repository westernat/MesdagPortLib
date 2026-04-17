package PortLib.extensions.java.util.List;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import java.util.List;
import java.util.NoSuchElementException;

@Extension
public class PortListExtension {
    public static <E> E getFirst(@This List<E> thiz) {
        if (thiz.isEmpty()) {
            throw new NoSuchElementException();
        }
        return thiz.get(0);
    }
}
