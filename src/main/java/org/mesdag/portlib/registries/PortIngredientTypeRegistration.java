package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.wrapper.common.crafting.PortIngredientType;

public class PortIngredientTypeRegistration extends PortRegistration<PortIngredientType<?>> {
    PortIngredientTypeRegistration(String namespace) {
        super(namespace, PortRegistries.Keys.INGREDIENT_TYPES);
    }

    @Override
    public <R extends PortIngredientType<?>> PortRegistryEntry<PortIngredientType<?>, R> register(String name, Supplier<R> valueSupplier) {
        return super.register(name, () -> {
            R type = valueSupplier.get();
            CraftingHelper.register(ResourceLocation.fromNamespaceAndPath(namespace, name), type.serializer());
            return type;
        });
    }
}
