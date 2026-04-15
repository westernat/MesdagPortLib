package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.event.PortBus;
import org.mesdag.portlib.wrapper.world.item.PortArmorMaterial;

import java.util.Objects;

public class PortArmorMaterialRegistration extends PortRegistration<PortArmorMaterial> {
    private final DeferredRegister<ArmorMaterial> register;

    PortArmorMaterialRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.ARMOR_MATERIALS);
        this.register = DeferredRegister.create(Registries.ARMOR_MATERIAL, namespace);
        register.register(PortBus.MOD.unwrap(namespace));
    }

    @Override
    public <R extends PortArmorMaterial> PortRegistryEntry<R> register(String name, Supplier<R> valueSupplier) {
        Supplier<R> memoize = Suppliers.memoize(valueSupplier);
        register.register(name, () -> memoize.get().unwrap());
        return new PortRegistryEntry.Memoized<>(namespace, name, memoize);
    }

    public PortRegistryEntry<PortArmorMaterial> register(PortArmorMaterial.Settings settings) {
        Objects.requireNonNull(settings.name, "ArmorMaterial name must not be null for registration!");
        int idx = settings.name.indexOf(':');
        if (idx != -1) {
            settings.name = settings.name.substring(idx + 1); // only path
        }
        return register(settings.name, () -> new PortArmorMaterial(settings));
    }
}
