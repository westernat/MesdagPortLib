package org.mesdag.portlib.event.other;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.CommandEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import javax.annotation.Nullable;

public class PortCommandEvent extends PortEvent<CommandEvent> implements IPortCancellableEvent {
    @Diff
    public PortCommandEvent(CommandEvent e) {
        super(e);
    }

    public ParseResults<CommandSourceStack> getParseResults() {
        return e.getParseResults();
    }

    public void setParseResults(ParseResults<CommandSourceStack> parse) {
        e.setParseResults(parse);
    }

    @Nullable
    public Throwable getException() {
        return e.getException();
    }

    public void setException(@Nullable Throwable exception) {
        e.setException(exception);
    }

    static {
        PortEventHooks.register();
    }
}
