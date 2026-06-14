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
    public <T extends PortIngredientType<?>> PortRegistryEntry<PortIngredientType<?>, T> register(String name, Supplier<T> valueSupplier) {
        return super.register(name, () -> {
            T type = valueSupplier.get();
            CraftingHelper.register(ResourceLocation.fromNamespaceAndPath(namespace, name), type.serializer());
            return type;
        });
    }
}
