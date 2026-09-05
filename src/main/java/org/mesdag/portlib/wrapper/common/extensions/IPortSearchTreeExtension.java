package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.client.searchtree.SuffixArray;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;

public interface IPortSearchTreeExtension {
    static <T> SearchTree<T> empty() {
        return s -> List.of();
    }

    static <T> SearchTree<T> plainText(List<T> contents, Function<T, Stream<String>> filter) {
        if (contents.isEmpty()) {
            return empty();
        }
        SuffixArray<T> suffixarray = new SuffixArray<>();
        for (T t : contents) {
            filter.apply(t).forEach(p_344960_ -> suffixarray.add(t, p_344960_.toLowerCase(Locale.ROOT)));
        }
        suffixarray.generate();
        return suffixarray::search;
    }
}
