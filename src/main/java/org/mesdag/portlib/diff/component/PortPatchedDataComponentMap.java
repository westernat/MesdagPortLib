package org.mesdag.portlib.diff.component;

import PortLib.extensions.net.minecraft.core.HolderLookup.PortHolderLookupExtension;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortItem;
import org.mesdag.portlib.diff.IPortItemStack;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Diff
@SuppressWarnings("unchecked")
public class PortPatchedDataComponentMap {
    public static final PortPatchedDataComponentMap EMPTY = new PortPatchedDataComponentMap(null) {
        @Override
        public <T> @Nullable T set(PortDataComponentType<T> type, T value) {
            return null;
        }

        @Override
        public <T> @Nullable T remove(PortDataComponentType<T> type) {
            return null;
        }
    };

    private final PortDataComponentMap prototype;
    private final Map<PortDataComponentType<?>, Optional<?>> patch;

    public PortPatchedDataComponentMap(@Nullable Item item) {
        this.prototype = item == null ? PortDataComponentMap.EMPTY : IPortItem.of(item);
        this.patch = new Reference2ObjectArrayMap<>();
    }

    public <T> @Nullable T get(PortDataComponentType<T> type) {
        Optional<? extends T> optional = (Optional<? extends T>) patch.get(type);
        return optional == null ? prototype.get(type) : optional.orElse(null);
    }

    public <T> @Nullable T set(PortDataComponentType<T> type, T value) {
        T t = prototype.get(type);
        Optional<T> optional;
        if (Objects.equals(value, t)) {
            optional = (Optional<T>) patch.remove(type);
        } else {
            optional = (Optional<T>) patch.put(type, Optional.ofNullable(value));
        }

        return optional == null ? t : optional.orElse(t);
    }

    public <T> @Nullable T remove(PortDataComponentType<T> type) {
        T t = prototype.get(type);
        Optional<? extends T> optional;
        if (t != null) {
            optional = (Optional<? extends T>) patch.put(type, Optional.empty());
        } else {
            optional = (Optional<? extends T>) patch.remove(type);
        }

        return optional == null ? t : optional.orElse(null);
    }

    public <T> T getOrDefault(PortDataComponentType<? extends T> type, T defaultValue) {
        T t = get(type);
        return t == null ? defaultValue : t;
    }

    public <T> boolean has(PortDataComponentType<T> type) {
        return get(type) != null;
    }

    public CompoundTag serializeNBT(RegistryAccess provider) {
        CompoundTag tag = new CompoundTag();
        RegistryOps<Tag> ops = PortHolderLookupExtension.Provider.createSerializationContext(provider, NbtOps.INSTANCE);
        for (Map.Entry<PortDataComponentType<?>, Optional<?>> entry : patch.entrySet()) {
            var type = entry.getKey();
            var key = PortRegistries.DATA_COMPONENTS.getKey(type);
            if (type.codec == null || entry.getValue().isEmpty()) continue;
            try {
                ((Codec<Object>) type.codec).encodeStart(ops, entry.getValue().get()).result().ifPresent(result -> tag.put(key.toString(), result));
            } catch (Exception exception) {
                PortLib.LOGGER.error("Failed to serialize data component {}. Skipping.", key, exception);
            }
        }
        return tag;
    }

    public void deserializeNBT(RegistryAccess provider, CompoundTag tag) {
        RegistryOps<Tag> ops = PortHolderLookupExtension.Provider.createSerializationContext(provider, NbtOps.INSTANCE);
        for (var key : tag.getAllKeys()) {
            ResourceLocation keyLocation = ResourceLocation.tryParse(key);
            if (keyLocation == null) {
                PortLib.LOGGER.error("Encountered invalid data component key {}. Skipping.", key);
                continue;
            }

            var type = PortRegistries.DATA_COMPONENTS.get(keyLocation);
            if (type == null || type.codec == null) {
                PortLib.LOGGER.error("Encountered unknown or non-serializable data component {}. Skipping.", key);
                continue;
            }

            try {
                patch.put(type, type.codec.parse(ops, tag.get(key)).result());
            } catch (Exception exception) {
                PortLib.LOGGER.error("Failed to deserialize data component {}. Skipping.", key, exception);
            }
        }
    }

    public void load(CompoundTag tag) {
        if (tag.contains(IPortItemStack.DATA_COMPONENTS, Tag.TAG_COMPOUND)) {
            deserializeNBT(PortEnvironment.registryAccess(), tag.getCompound(IPortItemStack.DATA_COMPONENTS));
        }
    }

    public boolean isEmpty() {
        return patch.isEmpty();
    }

    @Diff
    public PortDataComponentMap getPrototype() {
        return prototype;
    }
}
