package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.DeferredRegister;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.event.PortBus;
import org.mesdag.portlib.wrapper.common.crafting.PortIngredientType;

public class PortIngredientTypeRegistration extends PortRegistration<PortIngredientType<?>> {
    private final DeferredRegister<PortIngredientType<?>> register;

    PortIngredientTypeRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.INGREDIENT_TYPES);
        this.register = DeferredRegister.create(PortRegistries.Keys.INGREDIENT_TYPES, namespace);
        register.register(PortBus.MOD.unwrap(namespace));
    }

    @Override
    public <R extends PortIngredientType<?>> PortRegistryEntry<R> register(String name, Supplier<R> valueSupplier) {
        Supplier<R> memoize = Suppliers.memoize(() -> {
            R type = valueSupplier.get();
            CraftingHelper.register(ResourceLocation.fromNamespaceAndPath(namespace, name), type.serializer());
            return type;
        });
        register.register(name, memoize);
        return new PortRegistryEntry.Memoized<>(namespace, name, memoize);
    }
}
