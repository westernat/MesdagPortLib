package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.EntityDimensions;
import org.mesdag.portlib.diff.IPortEntityDimensions;
import org.mesdag.portlib.wrapper.world.entity.PortEntityAttachments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityDimensions.class)
public abstract class EntityDimensionsMixin implements IPortEntityDimensions {
    @Unique
    private PortEntityAttachments portlib$attachments;
    @Unique
    private float portlib$eyeHeight;

    @Override
    public PortEntityAttachments portlib$getAttachments() {
        return portlib$attachments;
    }

    @Override
    public void portlib$setAttachments(PortEntityAttachments attachments) {
        this.portlib$attachments = attachments;
    }

    @Override
    public float portlib$getEyeHeight() {
        return portlib$eyeHeight;
    }

    @Override
    public void portlib$setEyeHeight(float eyeHeight) {
        this.portlib$eyeHeight = eyeHeight;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void defaultValue(float width, float height, boolean fixed, CallbackInfo ci) {
        portlib$setEyeHeight(height * 0.85F);
        portlib$setAttachments(PortEntityAttachments.createDefault(width, height));
    }
}
