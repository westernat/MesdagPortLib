package org.mesdag.portlib.event.client;

import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterSpriteSourceTypesEvent extends PortEvent<RegisterSpriteSourceTypesEvent> {
    @Diff
    public PortRegisterSpriteSourceTypesEvent(RegisterSpriteSourceTypesEvent e) {
        super(e);
    }

    public void register(ResourceLocation id, SpriteSourceType sourceType) {
        e.register(id, sourceType);
    }

    static {
        PortEventHooks.register();
    }
}
