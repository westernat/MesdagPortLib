package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.ModifyRegistriesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortPriority;
import org.mesdag.portlib.event.registries.PortModifyRegistriesEvent;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class PortRegisterHandler {
    static final Map<ResourceKey<? extends Registry<?>>, List<PortRegistryEntry<?>>> registrations = new IdentityHashMap<>();

    public static <T, R extends Registry<T>> Registration<T> registration(String namespace, ResourceKey<R> registryKey) {
        return new Registration<>(namespace, registryKey);
    }

    public static <T, R extends Registry<T>> CustomRegistration<T> custom(String namespace, ResourceKey<R> registryKey, Consumer<PortRegistryMaker<T>> consumer) {
        return new CustomRegistration<>(namespace, registryKey, consumer);
    }

    @Diff
    public static <T, R extends Registry<T>> void init() {
        PortEventHandler.addListener(PortPriority.LOWEST, (RegisterEvent event) -> {
            ResourceKey<? extends Registry<?>> registryKey = event.getRegistryKey();
            List<PortRegistryEntry<?>> entries = registrations.get(registryKey);
            if (entries == null) return;
            for (PortRegistryEntry<?> entry : entries) {
                event.register((ResourceKey<R>) registryKey, entry.identifier, (Supplier<T>) entry.valueSupplier.get());
                entry.get(); // bind
            }
        });
        PortEventHandler.addListener((ModifyRegistriesEvent event) -> {
            PortEventHandler.postEvent(new PortModifyRegistriesEvent(event));
        });
    }
}
