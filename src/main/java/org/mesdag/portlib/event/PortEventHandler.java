package org.mesdag.portlib.event;

import net.jodah.typetools.TypeResolver;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.IModBusEvent;
import org.mesdag.portlib.diff.Diff;

import java.util.function.Consumer;
import java.util.function.Function;

public class PortEventHandler {
    public static <E extends Event> void addListener(Consumer<E> consumer) {
        PortEventHooks.wrapEvent(PortEventPriority.NORMAL, false, getEventClass(consumer), consumer);
    }

    public static <E extends Event> void addListener(PortEventPriority priority, Consumer<E> consumer) {
        PortEventHooks.wrapEvent(priority, false, getEventClass(consumer), consumer);
    }

    public static <E extends Event> void addListener(PortEventPriority priority, boolean receiveCancelled, Consumer<E> consumer) {
        PortEventHooks.wrapEvent(priority, receiveCancelled, getEventClass(consumer), consumer);
    }

    public static <E extends Event> void addListener(PortEventPriority priority, boolean receiveCancelled, Class<E> clazz, Consumer<E> consumer) {
        PortEventHooks.wrapEvent(priority, receiveCancelled, clazz, consumer);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Event> Class<E> getEventClass(Consumer<E> consumer) {
        Class<?> clazz = TypeResolver.resolveRawArgument(Consumer.class, consumer.getClass());
        if (clazz != null && Event.class.isAssignableFrom(clazz)) {
            return (Class<E>) clazz;
        }
        throw new IllegalArgumentException("The consumer's event type could not be inferred. Please use addListener(priority, receiveCancelled, Class<E>, Consumer<E>) instead.");
    }

    @Diff
    public static <F extends Event, T extends Event> void wrapEvent(boolean receiveCancelled, Class<F> from, Function<F, T> to) {
        addListener(PortEventPriority.LOWEST, receiveCancelled, from, f -> postEvent(to.apply(f)));
    }

    public static <E extends Event> void postEvent(E event) {
        if (event instanceof IModBusEvent modBusEvent) {
            ModLoader.get().postEvent((Event & IModBusEvent) modBusEvent);
        } else {
            MinecraftForge.EVENT_BUS.post(event);
        }
    }

    public static <E extends Event> E postEventWithReturn(E event) {
        postEvent(event);
        return event;
    }
}
