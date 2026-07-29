package org.mesdag.portlib.event;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.client.PortRegisterMenuScreensEvent;
import org.mesdag.portlib.event.client.extensions.common.PortRegisterClientExtensionsEvent;
import org.mesdag.portlib.event.level.PortChunkWatchEvent;
import org.mesdag.portlib.event.other.PortBlockEntityTypeAddBlocksEvent;
import org.mesdag.portlib.event.registries.PortModifyRegistriesEvent;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.lang.invoke.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class PortEventHooks {
    /**
     * Forge 会并行构造多个模组；事件包装类的静态初始化因此可能同时注册。
     * 三张表共同描述一次注册，必须在同一把锁下写入和读取快照。
     */
    private static final Object REGISTRY_LOCK = new Object();
    private static final Table<Class<? extends Event>, Class<? extends PortEvent<?>>, Function<? extends Event, ? extends PortEvent<?>>> wrappers = HashBasedTable.create();
    private static final Map<Class<? extends PortEvent<?>>, Class<? extends Event>> rawGetter = new Reference2ObjectOpenHashMap<>();
    private static final Table<Class<? extends Event>, Class<? extends PortEvent<?>>, Predicate<? extends Event>> predicates = HashBasedTable.create();

    @ApiStatus.Internal
    @Diff
    public static void init() {
        PortEventHandler.wrapEvent(false, RegisterCapabilitiesEvent.class, e -> new PortModifyRegistriesEvent());
        PortEventHandler.wrapEvent(false, SpawnPlacementRegisterEvent.class, e -> new PortBlockEntityTypeAddBlocksEvent());
        PortEventHandler.addListener((RegisterClientReloadListenersEvent event) -> {
            PortEventHandler.postEvent(new PortRegisterClientExtensionsEvent());
            PortEventHandler.postEvent(new PortRegisterMenuScreensEvent());
        });
    }

    private static void validateIfAbstract(Class<? extends Event> clazz) {
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
    public static <F extends Event, T extends PortEvent<?>> void registerPredicated(Class<F> from, Class<T> to, Function<F, T> wrapper, Predicate<F> predicate) {
        validateIfPortEvent(from);
        validateIfAbstract(from);
        validateIfAbstract(to);
        synchronized (REGISTRY_LOCK) {
            wrappers.put(from, to, wrapper);
            rawGetter.put(to, from);
            predicates.put(from, to, predicate);
        }
    }

    @Diff
    public static <F extends Event, T extends PortEvent<?>> void register(Class<F> from, Class<T> to, Function<F, T> wrapper) {
        validateIfPortEvent(from);
        validateIfAbstract(from);
        validateIfAbstract(to);
        synchronized (REGISTRY_LOCK) {
            wrappers.put(from, to, wrapper);
            rawGetter.put(to, from);
        }
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
                    Class<F> from = (Class<F>) params[0];
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
            throw new IllegalArgumentException(String.format("Cannot register non-PortEvent class: %s register() must be called from a static block of a PortEvent subclass.", caller.getName()));
        }
        @SuppressWarnings("unchecked")
        Class<? extends PortEvent<?>> eventClass = (Class<? extends PortEvent<?>>) caller;
        register(eventClass);
    }

    @SuppressWarnings("unchecked")
    static <F extends Event, T extends PortEvent<?>> void wrapEvent(PortEventPriority priority, boolean receiveCancelled, Class<F> from, Consumer<F> consumer) {
        if (PortEvent.class.isAssignableFrom(from)) {
            Class<F> rawFrom;
            synchronized (REGISTRY_LOCK) {
                rawFrom = (Class<F>) rawGetter.get(from);
            }
            if (rawFrom == null) {
                try {
                    Class.forName(from.getName(), true, from.getClassLoader()); // cinit
                    synchronized (REGISTRY_LOCK) {
                        rawFrom = (Class<F>) rawGetter.get(from);
                    }
                } catch (Exception ignored) {}
            }
            if (rawFrom == null) {
                PortLib.LOGGER.warn("Failed to find wrapped class for {}", from);
            } else {
                Function<F, T> wrapper;
                Predicate<F> predicate;
                synchronized (REGISTRY_LOCK) {
                    wrapper = (Function<F, T>) wrappers.get(rawFrom, from);
                    predicate = (Predicate<F>) predicates.get(rawFrom, from);
                }
                if (wrapper == null) {
                    PortLib.LOGGER.warn("Failed to find wrapper function for {}", rawFrom);
                } else {
                    PortBus bus = IModBusEvent.class.isAssignableFrom(rawFrom) ? PortBus.MOD : PortBus.GAME;
                    bus.unwrap(getCallerModId()).addListener(priority.unwrap(), receiveCancelled, rawFrom, raw -> {
                        if (predicate == null || predicate.test(raw)) {
                            consumer.accept((F) wrapper.apply(raw));
                        }
                    });
                }
            }
        } else {
            PortBus bus = IModBusEvent.class.isAssignableFrom(from) ? PortBus.MOD : PortBus.GAME;
            bus.unwrap(getCallerModId()).addListener(priority.unwrap(), receiveCancelled, from, consumer);
        }
    }

    @Diff
    public static void fireChunkSent(ServerPlayer entity, LevelChunk chunk, ServerLevel level) {
        PortEventHandler.postEvent(new PortChunkWatchEvent.Sent(entity, chunk, level));
    }

    @SuppressWarnings("removal")
    private static String getCallerModId() {
        try {
            return ModLoadingContext.get().getContainer().getModId();
        } catch (Exception e) {
            return PortLib.MODID;
        }
    }
}
