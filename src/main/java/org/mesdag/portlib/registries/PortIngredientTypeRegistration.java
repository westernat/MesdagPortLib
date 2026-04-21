package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.event.PortBus;
import org.mesdag.portlib.wrapper.common.crafting.PortIngredientType;

public class PortIngredientTypeRegistration extends PortRegistration<PortIngredientType<?>> {
    private final DeferredRegister<IngredientType<?>> register;

    PortIngredientTypeRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.INGREDIENT_TYPES, false);
        this.register = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, namespace);
        register.register(PortBus.MOD.unwrap(namespace));
    }

    @Override
    public <R extends PortIngredientType<?>> PortRegistryEntry<PortIngredientType<?>, R> register(String name, Supplier<R> valueSupplier) {
        Supplier<R> memoize = Suppliers.memoize(valueSupplier);
        register.register(name, () -> memoize.get().unwrap());
        PortRegistryEntry.Memoized<PortIngredientType<?>, R> entry = new PortRegistryEntry.Memoized<>(namespace, name, memoize);
        entries.add(entry);
        return entry;
    }
}
