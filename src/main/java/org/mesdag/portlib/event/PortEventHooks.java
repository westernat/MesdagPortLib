package org.mesdag.portlib.event;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PortEventHooks {
    static final Map<Class<? extends Event>, Function<? extends Event, ? extends PortEvent>> wrappers = new IdentityHashMap<>();
    static final Map<Class<? extends PortEvent>, Class<? extends Event>> rawGetter = new IdentityHashMap<>();

    public static void init() {}

    @Diff
    public static <F extends Event, T extends PortEvent> void register(Class<F> from, Class<T> to, Function<F, T> function) {
        wrappers.put(from, function);
        rawGetter.put(to, from);
    }

    @SuppressWarnings("unchecked")
    static <F extends Event, T extends PortEvent> void wrapEvent(PortEventPriority priority, boolean receiveCancelled, Class<F> from, Consumer<F> consumer) {
        if (PortEvent.class.isAssignableFrom(from)) {
            Class<F> rawFrom = (Class<F>) rawGetter.get(from);
            if (rawFrom == null) {
                try {
                    Class.forName(from.getName(), true, from.getClassLoader()); // cinit
                    rawFrom = (Class<F>) rawGetter.get(from);
                } catch (Exception ignored) {}
            }
            if (rawFrom == null) {
                PortLib.LOGGER.warn("Failed to find wrapped class for {}", from);
            } else {
                Function<F, T> wrapper = (Function<F, T>) wrappers.get(rawFrom);
                if (wrapper == null) {
                    PortLib.LOGGER.warn("Failed to find wrapper function for {}", rawFrom);
                } else {
                    PortBus bus = IModBusEvent.class.isAssignableFrom(rawFrom) ? PortBus.MOD : PortBus.GAME;
                    bus.unwrap().addListener(priority.unwrap(), receiveCancelled, rawFrom, raw -> consumer.accept((F) wrapper.apply(raw)));
                }
            }
        } else {
            PortBus bus = IModBusEvent.class.isAssignableFrom(from) ? PortBus.MOD : PortBus.GAME;
            bus.unwrap().addListener(priority.unwrap(), receiveCancelled, from, consumer);
        }
    }
}
