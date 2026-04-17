package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.function.Consumer;


public class PortRegisterShadersEvent extends PortEvent {
    private final RegisterShadersEvent e;

    @Diff
    public PortRegisterShadersEvent(RegisterShadersEvent e) {
        super(e);
        this.e = e;
    }

    public ResourceProvider getResourceProvider() {
        return e.getResourceProvider();
    }

    public void registerShader(ShaderInstance shaderInstance, Consumer<ShaderInstance> onLoaded) {
        e.registerShader(shaderInstance, onLoaded);
    }

    static {
        PortEventHooks.register(RegisterShadersEvent.class, PortRegisterShadersEvent.class, PortRegisterShadersEvent::new);
    }
}