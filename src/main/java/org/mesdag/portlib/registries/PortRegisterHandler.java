package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DataPackRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortBus;

import java.util.List;
import java.util.function.Consumer;

public class PortRegisterHandler {
    public static <T> PortRegistration<T> create(String namespace, ResourceKey<? extends Registry<T>> registryKey) {
        return new PortRegistration<>(namespace, registryKey);
    }

    public static <T> PortCustomRegistration<T> custom(String namespace, ResourceKey<? extends Registry<T>> registryKey, Consumer<PortRegistryMaker<T>> consumer) {
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

    public static PortAttributeRegistration attribute(String namespace) {
        return new PortAttributeRegistration(namespace);
    }

    public static PortIngredientTypeRegistration ingredientType(String namespace) {
        return new PortIngredientTypeRegistration(namespace);
    }

    public static PortParticleTypeRegistration particleType(String namespace) {
        return new PortParticleTypeRegistration(namespace);
    }

    public static PortItemRegistration item(String namespace) {
        return new PortItemRegistration(namespace);
    }

    public static PortBlockRegistration block(String namespace) {
        return new PortBlockRegistration(namespace);
    }

    @ApiStatus.Internal
    @Diff
    @SuppressWarnings("unchecked")
    public static <T, R extends Registry<T>> void init(IEventBus eventBus) {
        eventBus.addListener((DataPackRegistryEvent.NewRegistry e) -> {
            for (var byModId : PortRegistration.registrations.entrySet()) {
                String modId = byModId.getKey();
                var registration = byModId.getValue();
                PortBus.MOD.unwrap(modId).addListener(EventPriority.HIGH, (RegisterEvent event) -> {
                    ResourceKey<? extends Registry<?>> registryKey = event.getRegistryKey();
                    for (var entries : registration.getOrDefault(registryKey, List.of())) {
                        for (PortRegistryEntry<?, ?> entry : entries) {
                            if (entry.valueSupplier == null) continue;
                            event.register(
                                    (ResourceKey<R>) registryKey,
                                    entry.identifier,
                                    (Supplier<T>) entry.valueSupplier
                            );
                            entry.valueSupplier = null;
                        }
                    }
                });
            }
        });
    }
}
