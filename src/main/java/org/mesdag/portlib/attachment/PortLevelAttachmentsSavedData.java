package org.mesdag.portlib.attachment;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

import java.util.Objects;

@ApiStatus.Internal
public class PortLevelAttachmentsSavedData extends SavedData {
    private static final String NAME = "portlib_data_attachments";

    public static void init(ServerLevel level) {
        level.getDataStorage().computeIfAbsent(
                tag -> new PortLevelAttachmentsSavedData(level, tag),
                () -> new PortLevelAttachmentsSavedData(level),
                NAME
        );
    }

    private final ServerLevel level;

    public PortLevelAttachmentsSavedData(ServerLevel level) {
        this.level = level;
    }

    public PortLevelAttachmentsSavedData(ServerLevel level, CompoundTag tag) {
        this.level = level;
        PortAttachmentHolder.of(level).deserializeAttachments(new PortRegistryAccess(level.registryAccess()), tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        return Objects.requireNonNullElseGet(PortAttachmentHolder.of(level).serializeAttachments(new PortRegistryAccess(level.registryAccess())), CompoundTag::new);
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
