package org.mesdag.portlib.diff;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.util.Final;
import org.mesdag.portlib.util.Private;
import org.mesdag.portlib.util.Protected;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

@Diff
public interface CPortAttachmentHolder extends IPortAttachmentHolder {
    String ATTACHMENTS_NBT_KEY = "portlib:attachments";
    @Private
    boolean IN_DEV = PortEnvironment.isDeveloper();

    private void validateAttachmentType(PortAttachmentType<?> type) {
        Objects.requireNonNull(type);
        if (!IN_DEV) return;

        if (!PortRegistries.ATTACHMENT_TYPES.containsValue(type)) {
            throw new IllegalArgumentException("Data attachment type with default value " + type.defaultValueSupplier.apply(getExposedHolder()) + " must be registered!");
        }
    }

    @Protected
    @Contract(pure = true)
    @Nullable Map<PortAttachmentType<?>, Object> portlib$attachments();

    void portlib$attachments(Map<PortAttachmentType<?>, Object> map);

    @Protected
    @Final
    default Map<PortAttachmentType<?>, Object> getAttachmentMap() {
        if (portlib$attachments() == null) {
            portlib$attachments(new IdentityHashMap<>(4));
        }
        return portlib$attachments();
    }

    @Protected
    @Final
    default IPortAttachmentHolder getExposedHolder() {
        return this;
    }

    @Final
    @Override
    default boolean hasAttachments() {
        return portlib$attachments() != null && !portlib$attachments().isEmpty();
    }

    @Final
    @Override
    default boolean hasData(PortAttachmentType<?> type) {
        validateAttachmentType(type);
        return portlib$attachments() != null && portlib$attachments().containsKey(type);
    }

    @Final
    @Override
    default <T> T getData(PortAttachmentType<T> type) {
        validateAttachmentType(type);
        T ret = (T) getAttachmentMap().get(type);
        if (ret == null) {
            ret = type.defaultValueSupplier.apply(getExposedHolder());
            portlib$attachments().put(type, ret);
            syncData(type);
        }
        return ret;
    }

    @Override
    default <T> @Nullable T getExistingDataOrNull(PortAttachmentType<T> type) {
        validateAttachmentType(type);
        if (portlib$attachments() == null) {
            return null;
        }
        return (T) this.portlib$attachments().get(type);
    }

    @Override
    @MustBeInvokedByOverriders
    default <T> @Nullable T setData(PortAttachmentType<T> type, T data) {
        validateAttachmentType(type);
        Objects.requireNonNull(data);
        var previousData = (T) getAttachmentMap().put(type, data);
        syncData(type);
        return previousData;
    }

    @Override
    @MustBeInvokedByOverriders
    default <T> @Nullable T removeData(PortAttachmentType<T> type) {
        validateAttachmentType(type);
        if (portlib$attachments() == null) {
            return null;
        }
        var previousData = (T) portlib$attachments().remove(type);
        syncData(type);
        return previousData;
    }

    @Final
    default @Nullable CompoundTag serializeAttachments(PortRegistryAccess provider) {
        if (portlib$attachments() == null) {
            return null;
        }
        CompoundTag tag = null;
        for (var entry : portlib$attachments().entrySet()) {
            var type = entry.getKey();
            var key = PortRegistries.ATTACHMENT_TYPES.getKey(type);
            if (type.serializer != null) {
                try {
                    Tag serialized = ((IPortAttachmentSerializer<?, Object>) type.serializer).write(entry.getValue(), provider);
                    if (serialized != null) {
                        if (tag == null)
                            tag = new CompoundTag();
                        tag.put(key.toString(), serialized);
                    }
                } catch (Exception exception) {
                    PortLib.LOGGER.error("Failed to serialize data attachment {}. Skipping.", key, exception);
                }
            }
        }
        return tag;
    }

    @Protected(onlyInClass = true)
    @Final
    default void deserializeAttachments(PortRegistryAccess provider, CompoundTag tag) {
        for (var key : tag.getAllKeys()) {
            ResourceLocation keyLocation = ResourceLocation.tryParse(key);
            if (keyLocation == null) {
                PortLib.LOGGER.error("Encountered invalid data attachment key {}. Skipping.", key);
                continue;
            }

            var type = PortRegistries.ATTACHMENT_TYPES.get(keyLocation);
            if (type == null || type.serializer == null) {
                PortLib.LOGGER.error("Encountered unknown or non-serializable data attachment {}. Skipping.", key);
                continue;
            }

            try {
                getAttachmentMap().put(type, ((IPortAttachmentSerializer<Tag, ?>) type.serializer).read(getExposedHolder(), tag.get(key), provider));
            } catch (Exception exception) {
                PortLib.LOGGER.error("Failed to deserialize data attachment {}. Skipping.", key, exception);
            }
        }
    }

    static CPortAttachmentHolder of(BlockEntity o) {
        return (CPortAttachmentHolder) o;
    }

    static CPortAttachmentHolder of(Entity o) {
        return (CPortAttachmentHolder) o;
    }

    static CPortAttachmentHolder of(Level o) {
        return (CPortAttachmentHolder) o;
    }

    static CPortAttachmentHolder of(ChunkAccess o) {
        return (CPortAttachmentHolder) o;
    }
}
