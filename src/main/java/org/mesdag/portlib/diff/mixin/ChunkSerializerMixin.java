package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.CPortAttachmentHolder;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin {
    @Inject(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/ChunkAccess;setLightCorrect(Z)V"))
    private static void readAttachments(ServerLevel level, PoiManager poiManager, ChunkPos pos, CompoundTag tag, CallbackInfoReturnable<ProtoChunk> cir, @Local(name = "chunkaccess") ChunkAccess chunk) {
        if (tag.contains(CPortAttachmentHolder.ATTACHMENTS_NBT_KEY, Tag.TAG_COMPOUND)) {
            CPortAttachmentHolder.of(chunk).deserializeAttachments(new PortRegistryAccess(level.registryAccess()), tag.getCompound(CPortAttachmentHolder.ATTACHMENTS_NBT_KEY));
        }
    }

    @Inject(method = "write", at = @At("RETURN"))
    private static void writeAttachments(ServerLevel level, ChunkAccess chunk, CallbackInfoReturnable<CompoundTag> cir, @Local(name = "compoundtag") CompoundTag tag) {
        try {
            final CompoundTag capTag = CPortAttachmentHolder.of(chunk).serializeAttachments(new PortRegistryAccess(level.registryAccess()));
            if (capTag != null) {
                tag.put(CPortAttachmentHolder.ATTACHMENTS_NBT_KEY, capTag);
            }
        } catch (Exception exception) {
            PortLib.LOGGER.error("Failed to write chunk attachments. An attachment has likely thrown an exception trying to write state. It will not persist. Report this to the mod author", exception);
        }
    }
}
