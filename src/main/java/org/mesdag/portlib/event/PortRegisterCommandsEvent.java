package org.mesdag.portlib.event;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.mesdag.portlib.diff.Diff;

public class PortRegisterCommandsEvent extends PortEvent {
    private final RegisterCommandsEvent e;

    @Diff
    public PortRegisterCommandsEvent(RegisterCommandsEvent e) {
        super();
        this.e = e;
    }

    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return e.getDispatcher();
    }

    public Commands.CommandSelection getCommandSelection() {
        return e.getCommandSelection();
    }

    public CommandBuildContext getBuildContext() {
        return e.getBuildContext();
    }

    static {
        PortEventHooks.register(RegisterCommandsEvent.class, PortRegisterCommandsEvent.class, PortRegisterCommandsEvent::new);
    }
}