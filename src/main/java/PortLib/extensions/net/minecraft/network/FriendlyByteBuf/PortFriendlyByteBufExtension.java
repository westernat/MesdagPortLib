package PortLib.extensions.net.minecraft.network.FriendlyByteBuf;

import com.google.common.collect.Maps;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.util.TriConsumer;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortConnectionType;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamDecoder;
import org.mesdag.portlib.network.codec.PortStreamEncoder;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.util.Map;
import java.util.function.BiFunction;

@Extension
public class PortFriendlyByteBufExtension {
    public static <K, V> Map<K, V> readMap(@This FriendlyByteBuf thiz, PortStreamDecoder<? super FriendlyByteBuf, K> keyReader, BiFunction<FriendlyByteBuf, K, V> valueReader) {
        int size = thiz.readVarInt();
        Map<K, V> map = Maps.newHashMapWithExpectedSize(size);
        for (int i = 0; i < size; ++i) {
            K k = keyReader.decode(thiz);
            map.put(k, valueReader.apply(thiz, k));
        }
        return map;
    }

    public static <K, V> void writeMap(@This FriendlyByteBuf thiz, Map<K, V> map, PortStreamEncoder<? super FriendlyByteBuf, K> keyWriter, TriConsumer<FriendlyByteBuf, K, V> valueWriter) {
        thiz.writeVarInt(map.size());
        map.forEach((key, value) -> {
            keyWriter.encode(thiz, key);
            valueWriter.accept(thiz, key, value);
        });
    }

    @Diff
    public static PortRegistryFriendlyByteBuf wrap(@This FriendlyByteBuf thiz) {
        return new PortRegistryFriendlyByteBuf(thiz, PortEnvironment.registryAccess(), PortConnectionType.MODDED);
    }
}
