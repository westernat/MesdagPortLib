package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.network.FriendlyByteBuf.PortFriendlyByteBufExtension;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.util.TriConsumer;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamDecoder;
import org.mesdag.portlib.network.codec.PortStreamEncoder;

import java.util.Map;
import java.util.function.BiFunction;

@SuppressWarnings("all")
public interface IPortFriendlyByteBufExtension {

    private FriendlyByteBuf self() {
        return (FriendlyByteBuf) this;
    }

    default <K, V> Map<K, V> readMap(PortStreamDecoder<? super FriendlyByteBuf, K> keyReader, BiFunction<FriendlyByteBuf, K, V> valueReader) {
        return PortFriendlyByteBufExtension.readMap(self(), keyReader, valueReader);
    }

    default <K, V> void writeMap(Map<K, V> map, PortStreamEncoder<? super FriendlyByteBuf, K> keyWriter, TriConsumer<FriendlyByteBuf, K, V> valueWriter) {
        PortFriendlyByteBufExtension.writeMap(self(), map, keyWriter, valueWriter);
    }

    default PortRegistryFriendlyByteBuf wrap() {
        return PortFriendlyByteBufExtension.wrap(self());
    }

    static IPortFriendlyByteBufExtension of(FriendlyByteBuf buf) {
        return (IPortFriendlyByteBufExtension) buf;
    }
}
