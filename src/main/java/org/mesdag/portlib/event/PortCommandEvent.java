package org.mesdag.portlib.event;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.CommandEvent;
import org.mesdag.portlib.diff.Diff;

import javax.annotation.Nullable;

public class PortCommandEvent extends PortEvent implements IPortCancellableEvent {
    private final CommandEvent e;

    @Diff
    public PortCommandEvent(CommandEvent e) {
        super();
        this.e = e;
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
        PortEventHooks.register(CommandEvent.class, PortCommandEvent.class, PortCommandEvent::new);
    }
}