package org.mesdag.portlib.wrapper.common.extensions;

import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.util.TriConsumer;
import org.mesdag.portlib.network.codec.PortStreamDecoder;
import org.mesdag.portlib.network.codec.PortStreamEncoder;

import java.util.Map;
import java.util.function.BiFunction;

public interface IPortFriendlyByteBufExtension {
    static <K, V> Map<K, V> readMap(FriendlyByteBuf self, PortStreamDecoder<? super FriendlyByteBuf, K> keyReader, BiFunction<FriendlyByteBuf, K, V> valueReader) {
        final int size = self.readVarInt();
        final Map<K, V> map = Maps.newHashMapWithExpectedSize(size);

        for (int i = 0; i < size; ++i) {
            final K k = keyReader.decode(self);
            map.put(k, valueReader.apply(self, k));
        }

        return map;
    }

    static <K, V> void writeMap(FriendlyByteBuf self, Map<K, V> map, PortStreamEncoder<? super FriendlyByteBuf, K> keyWriter, TriConsumer<FriendlyByteBuf, K, V> valueWriter) {
        self.writeVarInt(map.size());
        map.forEach((key, value) -> {
            keyWriter.encode(self, key);
            valueWriter.accept(self, key, value);
        });
    }
}
