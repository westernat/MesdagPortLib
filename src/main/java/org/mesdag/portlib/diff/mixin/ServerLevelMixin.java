package org.mesdag.portlib.diff.mixin;

import net.minecraft.server.level.ServerLevel;
import org.mesdag.portlib.attachment.PortLevelAttachmentsSavedData;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements PortSelfGetter<ServerLevel> {
    @Inject(method = "initCapabilities", at = @At("TAIL"), remap = false)
    private void initAttachments(CallbackInfo ci) {
        PortLevelAttachmentsSavedData.init(portlib$self());
    }
}
