package org.mesdag.portlib.event;

import net.jodah.typetools.TypeResolver;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Consumer;

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

    public static <E extends Event> void postEvent(E event) {
        if (event instanceof IModBusEvent modBusEvent) {
            ModLoader.postEvent((Event & IModBusEvent) modBusEvent);
        } else {
            NeoForge.EVENT_BUS.post(event);
        }
    }

    public static <E extends Event> E postEventWithReturn(E event) {
        postEvent(event);
        return event;
    }
}
