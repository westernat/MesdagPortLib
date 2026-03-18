package org.mesdag.portlib.wrapper.common.brewing;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;
import org.mesdag.portlib.diff.Diff;

public interface IPortBrewingRecipe {
    boolean isInput(ItemStack input);

    boolean isIngredient(ItemStack ingredient);

    ItemStack getOutput(ItemStack input, ItemStack ingredient);

    @Diff
    default IBrewingRecipe unwrap() {
        return new IBrewingRecipe() {
            @Override
            public boolean isInput(ItemStack input) {
                return IPortBrewingRecipe.this.isInput(input);
            }

            @Override
            public boolean isIngredient(ItemStack ingredient) {
                return IPortBrewingRecipe.this.isIngredient(ingredient);
            }

            @Override
            public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
                return IPortBrewingRecipe.this.getOutput(input, ingredient);
            }
        };
    }

    @Diff
    static IPortBrewingRecipe wrap(IBrewingRecipe delegate) {
        return new Delegate(delegate);
    }

    @Diff
    record Delegate(IBrewingRecipe delegate) implements IPortBrewingRecipe {
        @Override
        public boolean isInput(ItemStack input) {
            return delegate.isInput(input);
        }

        @Override
        public boolean isIngredient(ItemStack ingredient) {
            return delegate.isIngredient(ingredient);
        }

        @Override
        public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
            return delegate.getOutput(input, ingredient);
        }

        @Override
        public IBrewingRecipe unwrap() {
            return delegate;
        }
    }
}
