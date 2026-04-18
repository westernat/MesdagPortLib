package org.mesdag.portlib.event.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterMaterialAtlasesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterMaterialAtlasesEvent extends PortEvent<RegisterMaterialAtlasesEvent> {
    @Diff
    public PortRegisterMaterialAtlasesEvent(RegisterMaterialAtlasesEvent e) {
        super(e);
    }

    public void register(ResourceLocation atlasLocation, ResourceLocation atlasInfoLocation) {
        e.register(atlasLocation, atlasInfoLocation);
    }

    static {
        PortEventHooks.register();
    }
}
