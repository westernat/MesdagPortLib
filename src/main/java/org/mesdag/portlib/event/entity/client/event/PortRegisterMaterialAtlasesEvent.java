package org.mesdag.portlib.event.entity.client.event;


import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterMaterialAtlasesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterMaterialAtlasesEvent extends PortEvent {
    private final RegisterMaterialAtlasesEvent e;

    @Diff
    public PortRegisterMaterialAtlasesEvent(RegisterMaterialAtlasesEvent e) {
        super(e);
        this.e = e;
    }

    public void register(ResourceLocation atlasLocation, ResourceLocation atlasInfoLocation) {
        e.register(atlasLocation, atlasInfoLocation);
    }

    static {
        PortEventHooks.register(RegisterMaterialAtlasesEvent.class, PortRegisterMaterialAtlasesEvent.class, PortRegisterMaterialAtlasesEvent::new);
    }
}