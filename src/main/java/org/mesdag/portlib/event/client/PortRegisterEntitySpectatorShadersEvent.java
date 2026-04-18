package org.mesdag.portlib.event.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.RegisterEntitySpectatorShadersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterEntitySpectatorShadersEvent extends PortEvent<RegisterEntitySpectatorShadersEvent> {
    @Diff
    public PortRegisterEntitySpectatorShadersEvent(RegisterEntitySpectatorShadersEvent e) {
        super(e);
    }

    public void register(EntityType<?> entityType, ResourceLocation shader) {
        e.register(entityType, shader);
    }

    static {
        PortEventHooks.register();
    }
}
