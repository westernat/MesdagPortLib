package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.client.gui.components.toasts.Toast;
import net.neoforged.neoforge.client.event.ToastAddEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortToastAddEvent extends PortEvent {
    private final ToastAddEvent e;

    @Diff
    public PortToastAddEvent(ToastAddEvent e) {
        super(e);
        this.e = e;
    }

    public Toast getToast() {
        return e.getToast();
    }

    public void setCanceled(boolean canceled) {
        e.setCanceled(canceled);
    }

    public boolean isCanceled() {
        return e.isCanceled();
    }

    static {
        PortEventHooks.register(ToastAddEvent.class, PortToastAddEvent.class, PortToastAddEvent::new);
    }
}