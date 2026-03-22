package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(ChunkAccess.class)
public abstract class ChunkAccessMixin implements CPortAttachmentHolder {
    @Unique
    private @Nullable Map<PortAttachmentType<?>, Object> portlib$attachments = null;

    @Override
    public @Nullable Map<PortAttachmentType<?>, Object> portlib$attachments() {
        return portlib$attachments;
    }

    @Override
    public void portlib$attachments(Map<PortAttachmentType<?>, Object> map) {
        this.portlib$attachments = map;
    }
}
