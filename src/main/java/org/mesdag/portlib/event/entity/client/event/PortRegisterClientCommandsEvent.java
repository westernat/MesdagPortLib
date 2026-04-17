package org.mesdag.portlib.event.entity.client.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterClientCommandsEvent extends PortEvent {
    private final RegisterClientCommandsEvent e;

    @Diff
    public PortRegisterClientCommandsEvent(RegisterClientCommandsEvent e) {
        super(e);
        this.e = e;
    }

    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return e.getDispatcher();
    }

    public CommandBuildContext getBuildContext() {
        return e.getBuildContext();
    }

    static {
        PortEventHooks.register(RegisterClientCommandsEvent.class, PortRegisterClientCommandsEvent.class, PortRegisterClientCommandsEvent::new);
    }
}