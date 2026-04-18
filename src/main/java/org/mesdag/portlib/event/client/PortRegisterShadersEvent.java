package org.mesdag.portlib.event.client;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.function.Consumer;

public class PortRegisterShadersEvent extends PortEvent<RegisterShadersEvent> {
    @Diff
    public PortRegisterShadersEvent(RegisterShadersEvent e) {
        super(e);
    }

    public ResourceProvider getResourceProvider() {
        return e.getResourceProvider();
    }

    public void registerShader(ShaderInstance shaderInstance, Consumer<ShaderInstance> onLoaded) {
        e.registerShader(shaderInstance, onLoaded);
    }

    static {
        PortEventHooks.register();
    }
}
