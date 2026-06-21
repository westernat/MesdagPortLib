package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.item.crafting.Ingredient;
import org.mesdag.portlib.wrapper.common.extensions.IPortIngredientExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Ingredient.class)
public abstract class IngredientMixin implements IPortIngredientExtension {
}
