package org.mesdag.portlib.diff.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.diff.attachment.PortAttachmentInternals;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements PortSelfGetter<LevelChunk> {
    @Inject(method = "<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ProtoChunk;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;)V", at = @At("TAIL"))
    private void copy(ServerLevel level, ProtoChunk chunk, LevelChunk.PostLoadProcessor postLoad, CallbackInfo ci) {
        PortAttachmentInternals.copyChunkAttachmentsOnPromotion(new PortRegistryAccess(level.registryAccess()), CPortAttachmentHolder.of(chunk), CPortAttachmentHolder.of(portlib$self()));
    }
}
