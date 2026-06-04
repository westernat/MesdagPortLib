package org.mesdag.portlib.diff.mixin;

import net.minecraftforge.fluids.FluidType;
import org.mesdag.portlib.diff.IPortFluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = FluidType.class, remap = false)
public abstract class FluidTypeMixin implements IPortFluidType {
    @Shadow
    private Object renderProperties;

    @Override
    public void portlib$setRenderPropertiesInternal(Object properties) {
        this.renderProperties = properties;
    }
}
