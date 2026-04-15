package org.mesdag.portlib.diff.datamap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortFriendlyByteBuf;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.extensions.IPortFriendlyByteBufExtension;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.Collections;
import java.util.Map;

@Diff
@SuppressWarnings({"unchecked", "rawtypes"})
public record PortRegistryDataMapSyncPayload<T>(
        ResourceKey<? extends Registry<T>> registryKey,
        Map<PortIdentifier, Map<ResourceKey<T>, ?>> dataMaps
) implements IPortPacket.S2C {
    public static final PortIdentifier IDENTIFIER = PortLib.asResource("registry_data_map_sync");
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortRegistryDataMapSyncPayload<?>> STREAM_CODEC = PortStreamCodec.ofMember(
            PortRegistryDataMapSyncPayload::write, PortRegistryDataMapSyncPayload::decode);

    public static <T> PortRegistryDataMapSyncPayload<T> decode(PortRegistryFriendlyByteBuf buf) {
        final ResourceKey<Registry<T>> registryKey = (ResourceKey<Registry<T>>) (Object) PortFriendlyByteBuf.readRegistryKey(buf);
        final Map<PortIdentifier, Map<ResourceKey<T>, ?>> attach = IPortFriendlyByteBufExtension.readMap(buf, PortByteBufCodecs.IDENTIFIER::decode, (b1, key) -> {
            final PortDataMapType<T, ?> dataMap = PortDataMapLoader.getDataMap(registryKey, key);
            return b1.readMap(bf -> bf.readResourceKey(registryKey), bf -> readJsonWithRegistryCodec((PortRegistryFriendlyByteBuf) bf, dataMap.networkCodec()));
        });
        return new PortRegistryDataMapSyncPayload<>(registryKey, attach);
    }

    public void write(PortRegistryFriendlyByteBuf buf) {
        buf.writeResourceKey(registryKey);
        IPortFriendlyByteBufExtension.writeMap(buf, dataMaps, PortByteBufCodecs.IDENTIFIER::encode, (b1, key, attach) -> {
            final PortDataMapType<T, ?> dataMap = PortDataMapLoader.getDataMap(registryKey, key);
            b1.writeMap(attach, FriendlyByteBuf::writeResourceKey, (bf, value) -> writeJsonWithRegistryCodec((PortRegistryFriendlyByteBuf) bf, (Codec) dataMap.networkCodec(), value));
        });
    }

    private static final Gson GSON = new Gson();

    private static <T> T readJsonWithRegistryCodec(PortRegistryFriendlyByteBuf buf, Codec<T> codec) {
        JsonElement jsonelement = GsonHelper.fromJson(GSON, buf.readUtf(), JsonElement.class);
        DataResult<T> dataresult = codec.parse(buf.registryAccess().createSerializationContext(JsonOps.INSTANCE), jsonelement);
        return dataresult.getOrThrow(false, name -> {
            throw new DecoderException("Failed to decode json: " + name);
        });
    }

    private static <T> void writeJsonWithRegistryCodec(PortRegistryFriendlyByteBuf buf, Codec<T> codec, T value) {
        DataResult<JsonElement> dataresult = codec.encodeStart(buf.registryAccess().createSerializationContext(JsonOps.INSTANCE), value);
        buf.writeUtf(GSON.toJson(dataresult.getOrThrow(false, message -> {
            throw new EncoderException("Failed to encode: " + message + " " + value);
        })));
    }

    @Override
    public void handle(Context context) {
        context.enqueueWork(() -> {
            try {
                IForgeRegistry registry = RegistryManager.ACTIVE.getRegistry(registryKey);
                Map innerMap = PortDataMapLoader.INSTANCE.getInnerMap(registry);
                innerMap.clear();
                dataMaps.forEach((attachKey, maps) -> innerMap.put(PortDataMapLoader.getDataMap(registryKey, attachKey), Collections.unmodifiableMap(maps)));
//                NeoForge.EVENT_BUS.post(new DataMapsUpdatedEvent(regAccess, registry, DataMapsUpdatedEvent.UpdateCause.CLIENT_SYNC));
            } catch (Throwable t) {
                PortLib.LOGGER.error("Failed to handle registry data map sync: ", t);
                context.disconnect(Component.translatable("portlib.network.data_maps.failed", registryKey.location().toString(), t.toString()));
            }
        });
    }

    @Override
    public void work(Player player) {}

    @Override
    public PortIdentifier identifier() {
        return IDENTIFIER;
    }
}
