package PortLib.extensions.net.minecraft.network.FriendlyByteBuf;

import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.util.TriConsumer;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortConnectionType;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamDecoder;
import org.mesdag.portlib.network.codec.PortStreamEncoder;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.util.Map;
import java.util.function.BiFunction;

public class PortFriendlyByteBufExtension {
    public static <K, V> Map<K, V> readMap(FriendlyByteBuf thiz, PortStreamDecoder<? super FriendlyByteBuf, K> keyReader, BiFunction<FriendlyByteBuf, K, V> valueReader) {
        return readMap(
                thiz, keyReader, valueReader, Integer.MAX_VALUE);
    }

    /**
     * 读取带明确条目上限的映射，必须在创建映射前校验远端提供的长度。
     */
    public static <K, V> Map<K, V> readMap(
            FriendlyByteBuf thiz,
            PortStreamDecoder<? super FriendlyByteBuf, K> keyReader,
            BiFunction<FriendlyByteBuf, K, V> valueReader,
            int maxSize
    ) {
        int size = PortByteBufCodecs.readCount(thiz, maxSize);
        Map<K, V> map = Maps.newHashMapWithExpectedSize(size);
        for (int i = 0; i < size; ++i) {
            K k = keyReader.decode(thiz);
            map.put(k, valueReader.apply(thiz, k));
        }
        return map;
    }

    public static <K, V> void writeMap(FriendlyByteBuf thiz, Map<K, V> map, PortStreamEncoder<? super FriendlyByteBuf, K> keyWriter, TriConsumer<FriendlyByteBuf, K, V> valueWriter) {
        writeMap(
                thiz, map, keyWriter, valueWriter, Integer.MAX_VALUE);
    }

    /**
     * 写入带明确条目上限的映射，防止本地异常状态生成接收端无法安全处理的数据包。
     */
    public static <K, V> void writeMap(
            FriendlyByteBuf thiz,
            Map<K, V> map,
            PortStreamEncoder<? super FriendlyByteBuf, K> keyWriter,
            TriConsumer<FriendlyByteBuf, K, V> valueWriter,
            int maxSize
    ) {
        PortByteBufCodecs.writeCount(thiz, map.size(), maxSize);
        map.forEach((key, value) -> {
            keyWriter.encode(thiz, key);
            valueWriter.accept(thiz, key, value);
        });
    }

    @Diff
    public static PortRegistryFriendlyByteBuf wrap(FriendlyByteBuf thiz) {
        return new PortRegistryFriendlyByteBuf(thiz, PortEnvironment.registryAccess(), PortConnectionType.MODDED);
    }
}
