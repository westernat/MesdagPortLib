package org.mesdag.portlib.diff.datamap;

import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import PortLib.extensions.net.minecraft.core.HolderLookup.PortHolderLookupExtension;
import PortLib.extensions.net.minecraft.network.FriendlyByteBuf.PortFriendlyByteBufExtension;
import PortLib.extensions.net.minecraft.resources.ResourceLocation.PortResourceLocationExtension;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.registries.PortDataMapsUpdatedEvent;
import org.mesdag.portlib.network.IPortPacket;
import org.mesdag.portlib.network.PortFriendlyByteBuf;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.util.Collections;
import java.util.Map;

@Diff
@SuppressWarnings({"unchecked", "rawtypes", "UnstableApiUsage"})
public record PortRegistryDataMapSyncPayload<T>(
        ResourceKey<? extends Registry<T>> registryKey,
        Map<ResourceLocation, Map<ResourceKey<T>, ?>> dataMaps
) implements IPortPacket.S2C {
    public static final ResourceLocation IDENTIFIER = PortLib.asResource("registry_data_map_sync");
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortRegistryDataMapSyncPayload<?>> STREAM_CODEC = PortStreamCodec.ofMember(
            PortRegistryDataMapSyncPayload::write, PortRegistryDataMapSyncPayload::decode);

    public static <T> PortRegistryDataMapSyncPayload<T> decode(PortRegistryFriendlyByteBuf buf) {
        final ResourceKey<Registry<T>> registryKey = (ResourceKey<Registry<T>>) (Object) /* <- don't delete */ PortFriendlyByteBuf.readRegistryKey(buf);
        final Map<ResourceLocation, Map<ResourceKey<T>, ?>> attach = PortFriendlyByteBufExtension.readMap(buf, PortResourceLocationExtension.streamCodec(), (b1, key) -> {
            final PortDataMapType<T, ?> dataMap = PortDataMapLoader.getDataMap(registryKey, key);
            return PortFriendlyByteBufExtension.readMap(b1, (FriendlyByteBuf bf) -> bf.readResourceKey(registryKey), (FriendlyByteBuf bf, ResourceKey<T> k) -> readJsonWithRegistryCodec((PortRegistryFriendlyByteBuf) bf, dataMap.networkCodec()));
        });
        return new PortRegistryDataMapSyncPayload<>(registryKey, attach);
    }

    public void write(PortRegistryFriendlyByteBuf buf) {
        buf.writeResourceKey(registryKey);
        PortFriendlyByteBufExtension.writeMap(buf, dataMaps, PortResourceLocationExtension.streamCodec(), (b1, key, attach) -> {
            final PortDataMapType<T, ?> dataMap = PortDataMapLoader.getDataMap(registryKey, key);
            PortFriendlyByteBufExtension.writeMap(b1, (Map) attach, FriendlyByteBuf::writeResourceKey, (FriendlyByteBuf bf, ResourceKey<T> k, Object value) -> writeJsonWithRegistryCodec((PortRegistryFriendlyByteBuf) bf, (Codec) dataMap.networkCodec(), value));
        });
    }

    private static final Gson GSON = new Gson();

    private static <T> T readJsonWithRegistryCodec(PortRegistryFriendlyByteBuf buf, Codec<T> codec) {
        JsonElement jsonelement = GsonHelper.fromJson(GSON, buf.readUtf(), JsonElement.class);
        DataResult<T> dataresult = codec.parse(PortHolderLookupExtension.Provider.createSerializationContext(buf.registryAccess(), JsonOps.INSTANCE), jsonelement);
        return PortDataResultExtension.getOrThrow(dataresult, name -> new DecoderException("Failed to decode json: " + name));
    }

    private static <T> void writeJsonWithRegistryCodec(PortRegistryFriendlyByteBuf buf, Codec<T> codec, T value) {
        DataResult<JsonElement> dataresult = codec.encodeStart(PortHolderLookupExtension.Provider.createSerializationContext(buf.registryAccess(), JsonOps.INSTANCE), value);
        buf.writeUtf(GSON.toJson(PortDataResultExtension.getOrThrow(dataresult, message -> new EncoderException("Failed to encode: " + message + " " + value))));
    }

    @Override
    public void handle(Context context) {
        context.enqueueWork(() -> {
            try {
                var regAccess = PortEnvironment.registryAccess();
                IForgeRegistry<?> registry = RegistryManager.ACTIVE.getRegistry(registryKey);
                Map innerMap = PortDataMapLoader.getInstance().getInnerMap(registryKey);
                innerMap.clear();
                dataMaps.forEach((attachKey, maps) -> innerMap.put(PortDataMapLoader.getDataMap(registryKey, attachKey), Collections.unmodifiableMap(maps)));
                PortEventHandler.postEvent(new PortDataMapsUpdatedEvent(regAccess, registry, PortDataMapsUpdatedEvent.PortUpdateCause.CLIENT_SYNC));
            } catch (Throwable t) {
                PortLib.LOGGER.error("Failed to handle registry data map sync: ", t);
                context.disconnect(Component.translatable("portlib.network.data_maps.failed", registryKey.location().toString(), t.toString()));
            }
        });
    }

    @Override
    public void work(Player player) {}

    @Override
    public ResourceLocation identifier() {
        return IDENTIFIER;
    }
}
