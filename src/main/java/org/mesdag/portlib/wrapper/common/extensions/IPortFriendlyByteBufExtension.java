package org.mesdag.portlib.wrapper.common.extensions;

import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.util.TriConsumer;
import org.mesdag.portlib.network.PortConnectionType;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamDecoder;
import org.mesdag.portlib.network.codec.PortStreamEncoder;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings("all")
public interface IPortFriendlyByteBufExtension {

    private FriendlyByteBuf self() {
        return (FriendlyByteBuf) this;
    }

    default <K, V> Map<K, V> readMap(PortStreamDecoder<? super FriendlyByteBuf, K> keyReader, BiFunction<FriendlyByteBuf, K, V> valueReader) {
        FriendlyByteBuf thiz = self();
        int size = thiz.readVarInt();
        Map<K, V> map = Maps.newHashMapWithExpectedSize(size);
        for (int i = 0; i < size; ++i) {
            K k = keyReader.decode(thiz);
            map.put(k, valueReader.apply(thiz, k));
        }
        return map;
    }

    default <K, V> void writeMap(Map<K, V> map, PortStreamEncoder<? super FriendlyByteBuf, K> keyWriter, TriConsumer<FriendlyByteBuf, K, V> valueWriter) {
        FriendlyByteBuf thiz = self();
        thiz.writeVarInt(map.size());
        map.forEach((key, value) -> {
            keyWriter.encode(thiz, key);
            valueWriter.accept(thiz, key, value);
        });
    }

    default PortRegistryFriendlyByteBuf wrap() {
        return new PortRegistryFriendlyByteBuf(self(), PortEnvironment.registryAccess(), PortConnectionType.MODDED);
    }

    static IPortFriendlyByteBufExtension of(FriendlyByteBuf buf) {
        return (IPortFriendlyByteBufExtension) buf;
    }
}
