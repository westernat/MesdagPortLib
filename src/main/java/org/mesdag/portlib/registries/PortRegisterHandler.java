package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventPriority;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class PortRegisterHandler {
    static final Map<ResourceKey<? extends Registry<?>>, List<PortRegistryEntry<?>>> registrations = new IdentityHashMap<>();

    public static <T, R extends Registry<T>> PortRegistration<T> create(String namespace, ResourceKey<R> registryKey) {
        return new PortRegistration<>(namespace, registryKey);
    }

    public static <T, R extends Registry<T>> PortCustomRegistration<T> custom(String namespace, ResourceKey<R> registryKey, Consumer<PortRegistryMaker<T>> consumer) {
        return new PortCustomRegistration<>(namespace, registryKey, consumer);
    }

    public static PortAttachmentRegistration attachment(String namespace) {
        return new PortAttachmentRegistration(namespace);
    }

    public static PortDataComponentRegistration dataComponent(String namespace) {
        return new PortDataComponentRegistration(namespace);
    }

    public static PortArmorMaterialRegistration armorMaterial(String namespace) {
        return new PortArmorMaterialRegistration(namespace);
    }

    @Diff
    public static <T, R extends Registry<T>> void init() {
        PortEventHandler.addListener(PortEventPriority.LOWEST, (RegisterEvent event) -> {
            ResourceKey<? extends Registry<?>> registryKey = event.getRegistryKey();
            List<PortRegistryEntry<?>> entries = registrations.get(registryKey);
            if (entries == null) return;
            for (PortRegistryEntry<?> entry : entries) {
                event.register(
                    (ResourceKey<R>) registryKey,
                    entry.identifier,
                    (Supplier<T>) entry.valueSupplier
                );
                entry.get(); // bind
                entry.valueSupplier = null;
            }
        });
    }
}
