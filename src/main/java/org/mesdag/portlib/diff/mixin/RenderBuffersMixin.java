package org.mesdag.portlib.diff.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortRegisterRenderBuffersEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBuffers.class)
public abstract class RenderBuffersMixin {
    // refmap in build gradle
    @Inject(method = "lambda$new$1", at = @At("TAIL"), remap = false)
    private void postEvent(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> p_269658_, CallbackInfo ci) {
        PortEventHandler.postEvent(new PortRegisterRenderBuffersEvent(p_269658_));
    }
}
