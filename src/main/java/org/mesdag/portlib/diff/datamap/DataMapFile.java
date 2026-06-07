package org.mesdag.portlib.diff.datamap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortWithConditions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Diff
public record DataMapFile<T, R>(
        boolean replace,
        Map<Either<TagKey<R>, ResourceKey<R>>, Optional<PortWithConditions<DataMapEntry<T>>>> values,
        List<DataMapEntry.Removal<T, R>> removals
) {
    public JsonObject write() {
        return new JsonObject();
    }

    public static <T, R> DataMapFile<T, R> read(ResourceKey<Registry<R>> registryKey, JsonObject json, PortDataMapType<R, T> dataMap) {
        boolean replace = GsonHelper.getAsBoolean(json, "replace", false);
        ImmutableMap.Builder<Either<TagKey<R>, ResourceKey<R>>, Optional<PortWithConditions<DataMapEntry<T>>>> valuesBuilder = ImmutableMap.builder();
        JsonObject valuesObject = GsonHelper.getAsJsonObject(json, "values");
        for (Map.Entry<String, JsonElement> entry : valuesObject.asMap().entrySet()) {
            JsonElement valueElement = entry.getValue();
            List<ICondition> conditions = null;
            if (valueElement.isJsonObject()) {
                JsonObject valueObject = valueElement.getAsJsonObject();
                if (valueObject.has("forge:conditions")) {
                    ImmutableList.Builder<ICondition> conditionBuilder = ImmutableList.builder();
                    JsonArray conditionsArray = GsonHelper.getAsJsonArray(valueObject, "forge:conditions");
                    for (JsonElement conditionElement : conditionsArray) {
                        if (!conditionElement.isJsonObject()) {
                            throw new JsonSyntaxException("Conditions must be an array of JsonObjects");
                        }
                        ICondition condition = CraftingHelper.getCondition(conditionElement.getAsJsonObject());
                        conditionBuilder.add(condition);
                    }
                    conditions = conditionBuilder.build();
                }
            }
            if (conditions == null) {
                conditions = List.of();
            }
            Either<TagKey<R>, ResourceKey<R>> key = readTagOrValue(registryKey, entry.getKey());
            DataMapEntry<T> value = DataMapEntry.read(valueElement, dataMap.codec());
            valuesBuilder.put(key, Optional.of(new PortWithConditions<>(conditions, value)));
        }
        ImmutableList.Builder<DataMapEntry.Removal<T, R>> removalsBuilder = ImmutableList.builder();
        JsonArray removeArray = GsonHelper.getAsJsonArray(json, "remove", null);
        if (removeArray != null) {
            for (JsonElement removeElement : removeArray) {
                removalsBuilder.add(DataMapEntry.Removal.read(registryKey, removeElement.getAsJsonObject(), dataMap));
            }
        }
        return new DataMapFile<>(replace, valuesBuilder.build(), removalsBuilder.build());
    }

    public static <R> Either<TagKey<R>, ResourceKey<R>> readTagOrValue(ResourceKey<Registry<R>> registryKey, String value) {
        if (value.startsWith("#")) {
            return Either.left(TagKey.create(registryKey, ResourceLocation.parse(value.substring(1))));
        } else {
            return Either.right(ResourceKey.create(registryKey, ResourceLocation.parse(value)));
        }
    }
}
