package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.wrapper.world.item.PortArmorMaterial;

import java.util.Objects;

public class PortArmorMaterialRegistration extends PortRegistration<PortArmorMaterial> {
    PortArmorMaterialRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.ARMOR_MATERIALS, false);
    }

    @Override
    public <R extends PortArmorMaterial> PortRegistryEntry<PortArmorMaterial, R> register(String name, Supplier<R> valueSupplier) {
        PortRegistryEntry.Memoized<PortArmorMaterial, R> entry = new PortRegistryEntry.Memoized<>(namespace, name, Suppliers.memoize(valueSupplier));
        entries.add(entry);
        return entry;
    }

    public PortRegistryEntry<PortArmorMaterial, PortArmorMaterial> register(PortArmorMaterial.Settings settings) {
        Objects.requireNonNull(settings.name, "ArmorMaterial name must not be null for registration!");
        if (settings.name.indexOf(':') == -1) {
            settings.name = namespace + ':' + settings.name; // with namespace
        }
        return register(settings.name, () -> new PortArmorMaterial(settings));
    }
}
