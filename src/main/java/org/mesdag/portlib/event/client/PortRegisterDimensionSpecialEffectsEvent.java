package org.mesdag.portlib.event.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterDimensionSpecialEffectsEvent extends PortEvent<RegisterDimensionSpecialEffectsEvent> {
    @Diff
    public PortRegisterDimensionSpecialEffectsEvent(RegisterDimensionSpecialEffectsEvent e) {
        super(e);
    }

    public void register(ResourceLocation dimensionType, DimensionSpecialEffects effects) {
        e.register(dimensionType, effects);
    }

    static {
        PortEventHooks.register();
    }
}
