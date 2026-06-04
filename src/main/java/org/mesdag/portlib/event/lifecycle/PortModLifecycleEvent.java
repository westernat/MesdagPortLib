package org.mesdag.portlib.event.lifecycle;

import PortLib.extensions.net.minecraftforge.fml.InterModComms.PortInterModCommsExtension;
import net.minecraftforge.fml.event.lifecycle.ModLifecycleEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.wrapper.fml.PortInterModComms;

import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class PortModLifecycleEvent<E extends ModLifecycleEvent> extends PortEvent<E> implements IPortModBusEvent {
    @Diff
    public PortModLifecycleEvent(E e) {
        super(e);
    }

    public final String description() {
        return e.description();
    }

    public Stream<PortInterModComms.PortIMCMessage> getIMCStream() {
        return e.getIMCStream().map(PortInterModCommsExtension.IMCMessage::wrap);
    }

    public Stream<PortInterModComms.PortIMCMessage> getIMCStream(Predicate<String> methodFilter) {
        return e.getIMCStream(methodFilter).map(PortInterModCommsExtension.IMCMessage::wrap);
    }

    @Override
    public String toString() {
        return e.toString();
    }
}
