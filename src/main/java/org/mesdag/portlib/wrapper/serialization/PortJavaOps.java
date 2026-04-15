package org.mesdag.portlib.wrapper.serialization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class PortJavaOps implements DynamicOps<Object> {
    public static final PortJavaOps INSTANCE = new PortJavaOps();

    private PortJavaOps() {}

    @Override
    public Object empty() {
        return null;
    }

    @Override
    public Object emptyMap() {
        return Map.of();
    }

    @Override
    public Object emptyList() {
        return List.of();
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, Object input) {
        if (input == null) {
            return outOps.empty();
        }
        if (input instanceof Map) {
            return convertMap(outOps, input);
        }
        if (input instanceof ByteList value) {
            return outOps.createByteList(ByteBuffer.wrap(value.toByteArray()));
        }
        if (input instanceof IntList value) {
            return outOps.createIntList(value.intStream());
        }
        if (input instanceof LongList value) {
            return outOps.createLongList(value.longStream());
        }
        if (input instanceof List) {
            return convertList(outOps, input);
        }
        if (input instanceof String value) {
            return outOps.createString(value);
        }
        if (input instanceof Boolean value) {
            return outOps.createBoolean(value);
        }
        if (input instanceof Byte value) {
            return outOps.createByte(value);
        }
        if (input instanceof Short value) {
            return outOps.createShort(value);
        }
        if (input instanceof Integer value) {
            return outOps.createInt(value);
        }
        if (input instanceof Long value) {
            return outOps.createLong(value);
        }
        if (input instanceof Float value) {
            return outOps.createFloat(value);
        }
        if (input instanceof Double value) {
            return outOps.createDouble(value);
        }
        if (input instanceof Number value) {
            return outOps.createNumeric(value);
        }
        throw new IllegalStateException("Don't know how to convert " + input);
    }

    @Override
    public DataResult<Number> getNumberValue(Object input) {
        if (input instanceof Number value) {
            return DataResult.success(value);
        }
        return DataResult.error(() -> "Not a number: " + input);
    }

    @Override
    public Object createNumeric(Number value) {
        return value;
    }

    @Override
    public Object createByte(byte value) {
        return value;
    }

    @Override
    public Object createShort(short value) {
        return value;
    }

    @Override
    public Object createInt(int value) {
        return value;
    }

    @Override
    public Object createLong(long value) {
        return value;
    }

    @Override
    public Object createFloat(float value) {
        return value;
    }

    @Override
    public Object createDouble(double value) {
        return value;
    }

    @Override
    public DataResult<Boolean> getBooleanValue(Object input) {
        if (input instanceof Boolean value) {
            return DataResult.success(value);
        }
        return DataResult.error(() -> "Not a boolean: " + input);
    }

    @Override
    public Object createBoolean(boolean value) {
        return value;
    }

    @Override
    public DataResult<String> getStringValue(Object input) {
        if (input instanceof String value) {
            return DataResult.success(value);
        }
        return DataResult.error(() -> "Not a string: " + input);
    }

    @Override
    public Object createString(String value) {
        return value;
    }

    @Override
    public DataResult<Object> mergeToList(Object input, Object value) {
        if (input == empty()) {
            return DataResult.success(List.of(value));
        }
        if (input instanceof List<?> list) {
            if (list.isEmpty()) {
                return DataResult.success(List.of(value));
            }
            return DataResult.success(ImmutableList.builder().addAll(list).add(value).build());
        }
        return DataResult.error(() -> "Not a list: " + input);
    }

    @Override
    public DataResult<Object> mergeToList(Object input, List<Object> values) {
        if (input == empty()) {
            return DataResult.success(values);
        }
        if (input instanceof List<?> list) {
            if (list.isEmpty()) {
                return DataResult.success(values);
            }
            return DataResult.success(ImmutableList.builder().addAll(list).addAll(values).build());
        }
        return DataResult.error(() -> "Not a list: " + input);
    }

    @Override
    public DataResult<Object> mergeToMap(Object input, Object key, Object value) {
        if (input == empty()) {
            return DataResult.success(Map.of(key, value));
        }
        if (input instanceof Map<?, ?> map) {
            if (map.isEmpty()) {
                return DataResult.success(Map.of(key, value));
            }
            ImmutableMap.Builder<Object, Object> result = ImmutableMap.builderWithExpectedSize(map.size() + 1);
            result.putAll(map);
            result.put(key, value);
            return DataResult.success(result.buildKeepingLast());
        }
        return DataResult.error(() -> "Not a map: " + input);
    }

    @Override
    public DataResult<Object> mergeToMap(Object input, Map<Object, Object> values) {
        if (input == empty()) {
            return DataResult.success(values);
        }
        if (input instanceof Map<?, ?> map) {
            if (map.isEmpty()) {
                return DataResult.success(values);
            }
            ImmutableMap.Builder<Object, Object> result = ImmutableMap.builderWithExpectedSize(map.size() + values.size());
            result.putAll(map);
            result.putAll(values);
            return DataResult.success(result.buildKeepingLast());
        }
        return DataResult.error(() -> "Not a map: " + input);
    }

    private static Map<Object, Object> mapLikeToMap(MapLike<Object> values) {
        return values.entries().collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
    }

    @Override
    public DataResult<Object> mergeToMap(Object input, MapLike<Object> values) {
        if (input == empty()) {
            return DataResult.success(mapLikeToMap(values));
        }
        if (input instanceof Map<?, ?> map) {
            if (map.isEmpty()) {
                return DataResult.success(mapLikeToMap(values));
            }

            ImmutableMap.Builder<Object, Object> result = ImmutableMap.builderWithExpectedSize(map.size());
            result.putAll(map);
            values.entries().forEach(e -> result.put(e.getFirst(), e.getSecond()));
            return DataResult.success(result.buildKeepingLast());
        }
        return DataResult.error(() -> "Not a map: " + input);
    }

    private static Stream<Pair<Object, Object>> getMapEntries(Map<?, ?> input) {
        return input.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue()));
    }

    @Override
    public DataResult<Stream<Pair<Object, Object>>> getMapValues(Object input) {
        if (input instanceof Map<?, ?> map) {
            return DataResult.success(getMapEntries(map));
        }
        return DataResult.error(() -> "Not a map: " + input);
    }

    @Override
    public DataResult<Consumer<BiConsumer<Object, Object>>> getMapEntries(Object input) {
        if (input instanceof Map<?, ?> map) {
            return DataResult.success(map::forEach);
        }
        return DataResult.error(() -> "Not a map: " + input);
    }

    @Override
    public Object createMap(Stream<Pair<Object, Object>> map) {
        return map.collect(ImmutableMap.toImmutableMap(Pair::getFirst, Pair::getSecond));
    }

    @Override
    public DataResult<MapLike<Object>> getMap(Object input) {
        if (input instanceof Map<?, ?> map) {
            return DataResult.success(
                    new MapLike<>() {
                        @Nullable
                        @Override
                        public Object get(Object key) {
                            return map.get(key);
                        }

                        @Nullable
                        @Override
                        public Object get(String key) {
                            return map.get(key);
                        }

                        @Override
                        public Stream<Pair<Object, Object>> entries() {
                            return getMapEntries(map);
                        }

                        @Override
                        public String toString() {
                            return "MapLike[" + map + "]";
                        }
                    }
            );
        }
        return DataResult.error(() -> "Not a map: " + input);
    }

    @Override
    public Object createMap(Map<Object, Object> map) {
        return map;
    }

    @Override
    public DataResult<Stream<Object>> getStream(Object input) {
        if (input instanceof List<?> list) {
            return DataResult.success(list.stream().map(o -> o));
        }
        return DataResult.error(() -> "Not an list: " + input);
    }

    @Override
    public DataResult<Consumer<Consumer<Object>>> getList(Object input) {
        if (input instanceof List<?> list) {
            return DataResult.success(list::forEach);
        }
        return DataResult.error(() -> "Not an list: " + input);
    }

    @Override
    public Object createList(Stream<Object> input) {
        return input.toList();
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(Object input) {
        if (input instanceof ByteList value) {
            return DataResult.success(ByteBuffer.wrap(value.toByteArray()));
        }
        return DataResult.error(() -> "Not a byte list: " + input);
    }

    @Override
    public Object createByteList(ByteBuffer input) {
        // Set .limit to .capacity to match default method
        ByteBuffer wholeBuffer = input.duplicate().clear();
        ByteArrayList result = new ByteArrayList();
        result.size(wholeBuffer.capacity());
        wholeBuffer.get(0, result.elements(), 0, result.size());
        return result;
    }

    @Override
    public DataResult<IntStream> getIntStream(Object input) {
        if (input instanceof IntList value) {
            return DataResult.success(value.intStream());
        }
        return DataResult.error(() -> "Not an int list: " + input);
    }

    @Override
    public Object createIntList(IntStream input) {
        return IntArrayList.toList(input);
    }

    @Override
    public DataResult<LongStream> getLongStream(Object input) {
        if (input instanceof LongList value) {
            return DataResult.success(value.longStream());
        }
        return DataResult.error(() -> "Not a long list: " + input);
    }

    @Override
    public Object createLongList(LongStream input) {
        return LongArrayList.toList(input);
    }

    @Override
    public Object remove(Object input, String key) {
        if (input instanceof Map<?, ?> map) {
            Map<Object, Object> result = new LinkedHashMap<>(map);
            result.remove(key);
            return Map.copyOf(result);
        }
        return input;
    }

    @Override
    public RecordBuilder<Object> mapBuilder() {
        return new FixedMapBuilder<>(this);
    }

    @Override
    public String toString() {
        return "Java";
    }

    private static class FixedMapBuilder<T> extends RecordBuilder.AbstractUniversalBuilder<T, ImmutableMap.Builder<T, T>> {
        public FixedMapBuilder(DynamicOps<T> ops) {
            super(ops);
        }

        @Override
        protected ImmutableMap.Builder<T, T> initBuilder() {
            return ImmutableMap.builder();
        }

        @Override
        protected ImmutableMap.Builder<T, T> append(T key, T value, ImmutableMap.Builder<T, T> builder) {
            return builder.put(key, value);
        }

        @Override
        protected DataResult<T> build(ImmutableMap.Builder<T, T> builder, T prefix) {
            ImmutableMap<T, T> result = builder.buildKeepingLast();
            return ops().mergeToMap(prefix, result);
        }
    }
}
