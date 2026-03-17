package org.mesdag.portlib.event;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.mesdag.portlib.diff.Diff;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class PortEventHooks {
    static final Map<Class<? extends Event>, Function<? extends Event, ? extends PortEvent>> wrappers = new HashMap<>();
    static final Map<Class<? extends PortEvent>, Class<? extends Event>> rawGetter = new HashMap<>();

    public static void init() {}

    @Diff
    public static <F extends Event, T extends PortEvent> void register(Class<F> from, Class<T> to, Function<F, T> function) {
        wrappers.put(from, function);
        rawGetter.put(to, from);
    }

    @SuppressWarnings("unchecked")
    static <F extends Event, T extends PortEvent> void wrapEvent(PortEventPriority priority, boolean receiveCancelled, Class<F> from, Consumer<F> consumer) {
        if (PortEvent.class.isAssignableFrom(from)) {
            try {
                Class.forName(from.getName(), true, from.getClassLoader()); // init
                from = (Class<F>) rawGetter.get(from);
                if (from != null) {
                    Function<F, T> function = (Function<F, T>) wrappers.get(from);
                    if (function != null) {
                        PortBus bus = IModBusEvent.class.isAssignableFrom(from) ? PortBus.MOD : PortBus.GAME;
                        bus.unwrap().addListener(priority.unwrap(), receiveCancelled, from, f -> consumer.accept((F) function.apply(f)));
                    }
                }
            } catch (Exception ignored) {}
        } else {
            PortBus bus = IModBusEvent.class.isAssignableFrom(from) ? PortBus.MOD : PortBus.GAME;
            bus.unwrap().addListener(priority.unwrap(), receiveCancelled, from, consumer);
        }
    }
}
