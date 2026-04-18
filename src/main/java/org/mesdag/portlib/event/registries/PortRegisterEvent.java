package org.mesdag.portlib.event.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class PortRegisterEvent extends PortEvent<RegisterEvent> implements IPortModBusEvent {
    @Diff
    public PortRegisterEvent(RegisterEvent e) {
        super(e);
    }

    @Diff
    public <T> void register(ResourceKey<? extends Registry<T>> registryKey, PortIdentifier name, Supplier<T> valueSupplier) {
        e.register(registryKey, name, valueSupplier);
    }

    public <T> void register(ResourceKey<? extends Registry<T>> registryKey, Consumer<PortRegisterHelper<T>> consumer) {
        e.register(registryKey, helper -> consumer.accept(PortRegisterHelper.wrap(helper)));
    }

    public ResourceKey<? extends Registry<?>> getRegistryKey() {
        return e.getRegistryKey();
    }

    public Registry<?> getRegistry() {
        return e.getVanillaRegistry();
    }

    public <T> @Nullable Registry<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        return (Registry<T>) BuiltInRegistries.REGISTRY.get(key.location());
    }

    @FunctionalInterface
    public interface PortRegisterHelper<T> {
        default void register(ResourceKey<T> key, T value) {
            ResourceLocation name = key.location();
            register(PortIdentifier.fromNamespaceAndPath(name.getNamespace(), name.getPath()), value);
        }

        void register(PortIdentifier name, T value);

        @Diff
        default RegisterEvent.RegisterHelper<T> unwrap() {
            return (name, value) -> register(PortIdentifier.fromNamespaceAndPath(name.getNamespace(), name.getPath()), value);
        }

        @Diff
        static <T> PortRegisterHelper<T> wrap(RegisterEvent.RegisterHelper<T> delegate) {
            return new Delegate<>(delegate);
        }

        @Diff
        record Delegate<T>(RegisterEvent.RegisterHelper<T> delegate) implements PortRegisterHelper<T> {
            @Override
            public void register(PortIdentifier name, T value) {
                delegate.register(name, value);
            }

            @Override
            public RegisterEvent.RegisterHelper<T> unwrap() {
                return delegate;
            }
        }
    }

    static {
        PortEventHooks.register();
    }
}
