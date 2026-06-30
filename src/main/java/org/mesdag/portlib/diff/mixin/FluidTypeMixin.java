package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.IPortFluidType;
import org.mesdag.portlib.wrapper.common.PortSoundActions;
import org.mesdag.portlib.wrapper.fluids.PortFluidType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = FluidType.class, remap = false)
public abstract class FluidTypeMixin implements IPortFluidType {
    @Shadow
    private Object renderProperties;
    @Unique
    private @Nullable PortFluidType.DripstoneDripInfo portlib$dripInfo;

    @Override
    public void portlib$setRenderPropertiesInternal(Object properties) {
        this.renderProperties = properties;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setup(FluidType.Properties properties, CallbackInfo ci) {
        this.portlib$dripInfo = IPortProperties.of(properties).portlib$getDripInfo();
    }

    @Override
    public @Nullable PortFluidType.DripstoneDripInfo portlib$getDripInfo() {
        return portlib$dripInfo;
    }

    @Mixin(value = FluidType.Properties.class, remap = false)
    public static abstract class PropertiesMixin implements IPortFluidType.IPortProperties {
        @Shadow
        @Final
        private Map<SoundAction, SoundEvent> sounds;
        @Unique
        private @Nullable PortFluidType.DripstoneDripInfo portlib$dripInfo;

        @Override
        public void portlib$setDripInfo(float chance, ParticleOptions dripParticle, Block cauldron, @Nullable SoundEvent fillSound) {
            if (fillSound != null) {
                sounds.put(PortSoundActions.CAULDRON_DRIP, fillSound);
            }
            this.portlib$dripInfo = new PortFluidType.DripstoneDripInfo(chance, dripParticle, cauldron);
        }

        @Override
        public @Nullable PortFluidType.DripstoneDripInfo portlib$getDripInfo() {
            return portlib$dripInfo;
        }
    }
}
