package org.mesdag.portlib.wrapper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("all")
public class PortUtil {
    public static <T> List<T> copyAndAdd(List<T> list, T value) {
        return ImmutableList.<T>builderWithExpectedSize(list.size() + 1).addAll(list).add(value).build();
    }

    public static <T> List<T> copyAndAdd(T value, List<T> list) {
        return ImmutableList.<T>builderWithExpectedSize(list.size() + 1).add(value).addAll(list).build();
    }

    public static <K, V> Map<K, V> copyAndPut(Map<K, V> map, K key, V value) {
        return ImmutableMap.<K, V>builderWithExpectedSize(map.size() + 1).putAll(map).put(key, value).buildKeepingLast();
    }

    public static <T> boolean isSymmetrical(int width, int height, List<T> list) {
        if (width == 1) return true;
        int i = width / 2;
        for (int j = 0; j < height; j++) {
            for (int k = 0; k < i; k++) {
                int l = width - 1 - k;
                T t = list.get(k + j * width);
                T t1 = list.get(l + j * width);
                if (t.equals(t1)) continue;
                return false;
            }
        }
        return true;
    }

    public static ResourceLocation asGuiSprite(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, "textures/gui/sprites/" + path + ".png");
    }

    public static <A, T> DataResult<T> encode(A input, Function<A, JsonElement> serializer, DynamicOps<T> ops, T prefix) {
        return ops.getMap(prefix).flatMap(preMap -> {
            T converted = JsonOps.INSTANCE.convertTo(ops, serializer.apply(input));
            return ops.getMap(converted).flatMap(jsonMap -> ops.mergeToMap(prefix, jsonMap));
        });
    }
}
