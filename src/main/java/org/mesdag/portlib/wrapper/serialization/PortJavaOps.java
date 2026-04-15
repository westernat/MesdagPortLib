package org.mesdag.portlib.wrapper.serialization;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;

import java.nio.ByteBuffer;
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
        return JavaOps.INSTANCE.empty();
    }

    @Override
    public Object emptyMap() {
        return JavaOps.INSTANCE.emptyMap();
    }

    @Override
    public Object emptyList() {
        return JavaOps.INSTANCE.emptyMap();
    }

    @Override
    public <U> U convertTo(DynamicOps<U> outOps, Object input) {
        return JavaOps.INSTANCE.convertTo(outOps, input);
    }

    @Override
    public DataResult<Number> getNumberValue(Object input) {
        return JavaOps.INSTANCE.getNumberValue(input);
    }

    @Override
    public Object createNumeric(Number value) {
        return JavaOps.INSTANCE.createNumeric(value);
    }

    @Override
    public Object createByte(byte value) {
        return JavaOps.INSTANCE.createByte(value);
    }

    @Override
    public Object createShort(short value) {
        return JavaOps.INSTANCE.createShort(value);
    }

    @Override
    public Object createInt(int value) {
        return JavaOps.INSTANCE.createInt(value);
    }

    @Override
    public Object createLong(long value) {
        return JavaOps.INSTANCE.createLong(value);
    }

    @Override
    public Object createFloat(float value) {
        return JavaOps.INSTANCE.createFloat(value);
    }

    @Override
    public Object createDouble(double value) {
        return JavaOps.INSTANCE.createDouble(value);
    }

    @Override
    public DataResult<Boolean> getBooleanValue(Object input) {
        return JavaOps.INSTANCE.getBooleanValue(input);
    }

    @Override
    public Object createBoolean(boolean value) {
        return JavaOps.INSTANCE.createBoolean(value);
    }

    @Override
    public DataResult<String> getStringValue(Object input) {
        return JavaOps.INSTANCE.getStringValue(input);
    }

    @Override
    public Object createString(String value) {
        return JavaOps.INSTANCE.createString(value);
    }

    @Override
    public DataResult<Object> mergeToList(Object input, Object value) {
        return JavaOps.INSTANCE.mergeToList(input, value);
    }

    @Override
    public DataResult<Object> mergeToList(Object input, List<Object> values) {
        return JavaOps.INSTANCE.mergeToList(input, values);
    }

    @Override
    public DataResult<Object> mergeToMap(Object input, Object key, Object value) {
        return JavaOps.INSTANCE.mergeToMap(input, key, value);
    }

    @Override
    public DataResult<Object> mergeToMap(Object input, Map<Object, Object> values) {
        return JavaOps.INSTANCE.mergeToMap(input, values);
    }

    @Override
    public DataResult<Object> mergeToMap(Object input, MapLike<Object> values) {
        return JavaOps.INSTANCE.mergeToMap(input, values);
    }

    @Override
    public DataResult<Stream<Pair<Object, Object>>> getMapValues(Object input) {
        return JavaOps.INSTANCE.getMapValues(input);
    }

    @Override
    public DataResult<Consumer<BiConsumer<Object, Object>>> getMapEntries(Object input) {
        return JavaOps.INSTANCE.getMapEntries(input);
    }

    @Override
    public Object createMap(Stream<Pair<Object, Object>> map) {
        return JavaOps.INSTANCE.createMap(map);
    }

    @Override
    public DataResult<MapLike<Object>> getMap(Object input) {
        return JavaOps.INSTANCE.getMap(input);
    }

    @Override
    public Object createMap(Map<Object, Object> map) {
        return JavaOps.INSTANCE.createMap(map);
    }

    @Override
    public DataResult<Stream<Object>> getStream(Object input) {
        return JavaOps.INSTANCE.getStream(input);
    }

    @Override
    public DataResult<Consumer<Consumer<Object>>> getList(Object input) {
        return JavaOps.INSTANCE.getList(input);
    }

    @Override
    public Object createList(Stream<Object> input) {
        return JavaOps.INSTANCE.createList(input);
    }

    @Override
    public DataResult<ByteBuffer> getByteBuffer(Object input) {
        return JavaOps.INSTANCE.getByteBuffer(input);
    }

    @Override
    public Object createByteList(ByteBuffer input) {
        return JavaOps.INSTANCE.createByteList(input);
    }

    @Override
    public DataResult<IntStream> getIntStream(Object input) {
        return JavaOps.INSTANCE.getIntStream(input);
    }

    @Override
    public Object createIntList(IntStream input) {
        return JavaOps.INSTANCE.createIntList(input);
    }

    @Override
    public DataResult<LongStream> getLongStream(Object input) {
        return JavaOps.INSTANCE.getLongStream(input);
    }

    @Override
    public Object createLongList(LongStream input) {
        return JavaOps.INSTANCE.createLongList(input);
    }

    @Override
    public Object remove(Object input, String key) {
        return JavaOps.INSTANCE.remove(input, key);
    }

    @Override
    public RecordBuilder<Object> mapBuilder() {
        return JavaOps.INSTANCE.mapBuilder();
    }

    @Override
    public String toString() {
        return JavaOps.INSTANCE.toString();
    }
}
