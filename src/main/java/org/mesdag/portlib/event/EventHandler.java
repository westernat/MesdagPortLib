package org.mesdag.portlib.event;

import net.jodah.typetools.TypeResolver;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class EventHandler {
    public static <E extends Event> void addListener(Consumer<E> consumer) {
        getBus(consumer).unwrap().addListener(consumer);
    }

    public static <E extends Event> void addListener(Priority priority, Consumer<E> consumer) {
        getBus(consumer).unwrap().addListener(priority.unwrap(), consumer);
    }

    public static <E extends Event> void addListener(Priority priority, boolean receiveCancelled, Consumer<E> consumer) {
        getBus(consumer).unwrap().addListener(priority.unwrap(), receiveCancelled, consumer);
    }

    public static <E extends Event> void addListener(Priority priority, boolean receiveCancelled, Class<E> clazz, Consumer<E> consumer) {
        getBus(clazz).unwrap().addListener(priority.unwrap(), receiveCancelled, clazz, consumer);
    }

    private static <E extends Event> EventBus getBus(Consumer<E> consumer) {
        Class<?> clazz = TypeResolver.resolveRawArgument(Consumer.class, consumer.getClass());
        // addListener时会自动检查
        return getBus(clazz);
    }

    private static EventBus getBus(Class<?> clazz) {
        return clazz.isAssignableFrom(IModBusEvent.class) ? EventBus.MOD : EventBus.GAME;
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
