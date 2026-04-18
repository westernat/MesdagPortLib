package org.mesdag.portlib.event.other;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterCommandsEvent extends PortEvent<RegisterCommandsEvent> {
    @Diff
    public PortRegisterCommandsEvent(RegisterCommandsEvent e) {
        super(e);
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
        PortEventHooks.register();
    }
}
