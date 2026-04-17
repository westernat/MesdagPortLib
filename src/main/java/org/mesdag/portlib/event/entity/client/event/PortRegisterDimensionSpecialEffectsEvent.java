package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortRegisterDimensionSpecialEffectsEvent extends PortEvent {
    private final RegisterDimensionSpecialEffectsEvent e;

    @Diff
    public PortRegisterDimensionSpecialEffectsEvent(RegisterDimensionSpecialEffectsEvent e) {
        super(e);
        this.e = e;
    }

    public void register(ResourceLocation dimensionType, DimensionSpecialEffects effects) {
        e.register(dimensionType, effects);
    }

    static {
        PortEventHooks.register(RegisterDimensionSpecialEffectsEvent.class, PortRegisterDimensionSpecialEffectsEvent.class, PortRegisterDimensionSpecialEffectsEvent::new);
    }
}