package org.mesdag.portlib.diff;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.entity.animation.AnimationTarget;
import net.neoforged.neoforge.client.event.RegisterJsonAnimationTypesEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

@Diff
public class PortRegisterJsonAnimationTypesEvent extends PortEvent<RegisterJsonAnimationTypesEvent> {
    @Diff
    public PortRegisterJsonAnimationTypesEvent(RegisterJsonAnimationTypesEvent e) {
        super(e);
    }

    public void registerTarget(ResourceLocation key, AnimationTarget target) {
        e.registerTarget(key, target);
    }

    public void registerInterpolation(ResourceLocation key, AnimationChannel.Interpolation interpolation) {
        e.registerInterpolation(key, interpolation);
    }

    static {
        PortEventHooks.register();
    }
}
