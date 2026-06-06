package org.mesdag.portlib.diff.mixin;

import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(value = net.minecraft.world.item.crafting.ShapedRecipe.class, remap = false)
public interface ShapedRecipeAccessor {
    @Accessor
    static int getMAX_WIDTH() {throw new UnsupportedOperationException();}

    @Accessor
    static int getMAX_HEIGHT() {throw new UnsupportedOperationException();}
}
