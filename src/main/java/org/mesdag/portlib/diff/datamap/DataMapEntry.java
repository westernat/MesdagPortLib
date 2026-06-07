package org.mesdag.portlib.diff.datamap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import org.mesdag.portlib.datamap.PortAdvancedDataMapType;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.datamap.PortDataMapValueRemover;
import org.mesdag.portlib.diff.Diff;

import java.util.Optional;

@Diff
public record DataMapEntry<T>(T value, boolean replace) {
    public JsonObject write(DataMapEntry<T> value) {
        return new JsonObject();
    }

    public static <T> DataMapEntry<T> read(JsonElement json, Codec<T> codec) {
        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            if (object.has("value")) {
                T value = codec.parse(JsonOps.INSTANCE, object.get("value")).result().orElseThrow();
                return new DataMapEntry<>(value, GsonHelper.getAsBoolean(object, "replace", false));
            }
            return new DataMapEntry<>(codec.parse(JsonOps.INSTANCE, object).result().orElseThrow(), false);
        }
        return new DataMapEntry<>(codec.parse(JsonOps.INSTANCE, json).result().orElseThrow(), false);
    }

    public record Removal<T, R>(
            Either<TagKey<R>, ResourceKey<R>> key,
            Optional<PortDataMapValueRemover<R, T>> remover
    ) {
        public JsonElement write(PortDataMapType<R, T> dataMap) {
            return new JsonObject();
        }

        public static <T, R> Removal<T, R> read(ResourceKey<Registry<R>> registryKey, JsonObject json, PortDataMapType<R, T> dataMap) {
            Either<TagKey<R>, ResourceKey<R>> removalKey = DataMapFile.readTagOrValue(registryKey, GsonHelper.getAsString(json, "key"));
            if (dataMap instanceof PortAdvancedDataMapType<R, T, ?> advanced) {
                PortDataMapValueRemover<R, T> remover = ((Codec<PortDataMapValueRemover<R, T>>) advanced.remover()).parse(JsonOps.INSTANCE, json.get("remover")).result().orElse(null);
                return new Removal<>(removalKey, Optional.ofNullable(remover));
            }
            return new Removal<>(removalKey, Optional.empty());
        }
    }
}
