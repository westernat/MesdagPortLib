package PortLib.extensions.net.minecraft.data.DataProvider;

import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class PortDataProviderExtension {
    public static <A> CompletableFuture<?> saveStable(CachedOutput output, HolderLookup.Provider provider, Codec<A> codec, A value, Path path) {
//        DynamicOps<JsonElement> ops = PortHolderLookupExtension.Provider.createSerializationContext(provider, JsonOps.INSTANCE);
        return DataProvider.saveStable(output, PortDataResultExtension.getOrThrow(codec.encodeStart(JsonOps.INSTANCE, value)), path);
    }
}
