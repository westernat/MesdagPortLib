package org.mesdag.portlib.event;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class PortEventHooks {
    private static final Map<Class<? extends Event>, Function<? extends Event, ? extends PortEvent<?>>> wrappers = new ConcurrentHashMap<>();
    private static final Map<Class<? extends PortEvent<?>>, Class<? extends Event>> rawGetter = new ConcurrentHashMap<>();

    private static  void validateIfAbstract(Class<? extends Event> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException("event class cannot be abstract: " + clazz.getName());
        }
    }

    private static void validateIfPortEvent(Class<? extends Event> clazz) {
        if (PortEvent.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("PortEvent cannot be wrapped");
        }
    }

    @Diff
    public static <F extends Event, T extends PortEvent<?>> void register(Class<F> from, Class<T> to, Function<F, T> function) {
        validateIfPortEvent(from);
        validateIfAbstract(from);
        validateIfAbstract(to);
        wrappers.put(from, function);
        rawGetter.put(to, from);
    }

    @Diff
    public static <F extends Event, T extends PortEvent<?>> void register(Class<F> from, Class<T> to) {
        try {
            Constructor<T> ctorReflect = to.getDeclaredConstructor(from);
            ctorReflect.setAccessible(true);
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle ctor = lookup.unreflectConstructor(ctorReflect);
            CallSite site = LambdaMetafactory.metafactory(
                    lookup, "apply",
                    MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    ctor, ctor.type()
            );
            @SuppressWarnings("unchecked")
            Function<F, T> factory = (Function<F, T>) site.getTarget().invokeExact();
            register(from, to, factory);
        } catch (Throwable e) {
            if (PortEnvironment.isDeveloper()) {
                throw new IllegalStateException(e);
            }
            PortLib.LOGGER.error("Failed to register Event Wrapper for from={}, to={}", from, to, e);
        }
    }

    @Diff
    public static <F extends Event, T extends PortEvent<?>> void register(Class<T> to) {
        try {
            Constructor<?>[] ctors = to.getDeclaredConstructors();
            for (Constructor<?> ctor : ctors) {
                Class<?>[] params = ctor.getParameterTypes();
                if (params.length == 1 && Event.class.isAssignableFrom(params[0])) {
                    @SuppressWarnings("unchecked")
                    Class<F> from = (Class<F>) ctor.getParameterTypes()[0];
                    register(from, to);
                    return;
                }
            }
            throw new IllegalArgumentException("No suitable constructor in " + to + " with single parameter of Event subtype");
        } catch (Throwable e) {
            if (PortEnvironment.isDeveloper()) {
                throw new IllegalStateException(e);
            }
            PortLib.LOGGER.error("Failed to register Event Wrapper for to={}", to, e);
        }
    }

    @Diff
    public static void register() {
        Class<?> caller = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        if (caller == null) {
            throw new IllegalStateException("Unable to determine caller class");
        }
        if (!PortEvent.class.isAssignableFrom(caller)) {
            throw new IllegalArgumentException(String.format("Cannot register non-PortEvent class: %s.register() must be called from a static block of a PortEvent subclass.", caller.getName()));
        }
        @SuppressWarnings("unchecked")
        Class<? extends PortEvent<?>> eventClass = (Class<? extends PortEvent<?>>) caller;
        register(eventClass);
    }

    @SuppressWarnings("unchecked")
    static <F extends Event, T extends PortEvent<?>> void wrapEvent(PortEventPriority priority, boolean receiveCancelled, Class<F> from, Consumer<F> consumer) {
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
                    bus.unwrap(PortLib.MODID).addListener(priority.unwrap(), receiveCancelled, rawFrom, raw -> consumer.accept((F) wrapper.apply(raw)));
                }
            }
        } else {
            PortBus bus = IModBusEvent.class.isAssignableFrom(from) ? PortBus.MOD : PortBus.GAME;
            bus.unwrap(PortLib.MODID).addListener(priority.unwrap(), receiveCancelled, from, consumer);
        }
    }
}
