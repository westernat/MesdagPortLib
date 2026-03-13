package org.mesdag.portlib.event;

import net.jodah.typetools.TypeResolver;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("all")
public class PortEventHandler {
    public static <E extends Event> void addListener(Consumer<E> consumer) {
        getBus(consumer).unwrap().addListener(consumer);
    }

    public static <E extends Event> void addListener(PortPriority priority, Consumer<E> consumer) {
        getBus(consumer).unwrap().addListener(priority.unwrap(), consumer);
    }

    public static <E extends Event> void addListener(PortPriority priority, boolean receiveCancelled, Consumer<E> consumer) {
        getBus(consumer).unwrap().addListener(priority.unwrap(), receiveCancelled, consumer);
    }

    public static <E extends Event> void addListener(PortPriority priority, boolean receiveCancelled, Class<E> clazz, Consumer<E> consumer) {
        getBus(clazz).unwrap().addListener(priority.unwrap(), receiveCancelled, clazz, consumer);
    }

    @ApiStatus.Internal
    public static <F extends Event, T extends Event> void wrapEvent(boolean receiveCancelled, Class<F> from, Function<F, T> to) {
        addListener(PortPriority.LOWEST, receiveCancelled, from, to::apply);
    }

    private static <E extends Event> PortBus getBus(Consumer<E> consumer) {
        Class<?> clazz = TypeResolver.resolveRawArgument(Consumer.class, consumer.getClass());
        // addListener时会自动检查
        return getBus(clazz);
    }

    private static PortBus getBus(Class<?> clazz) {
        return IModBusEvent.class.isAssignableFrom(clazz) ? PortBus.MOD : PortBus.GAME;
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
