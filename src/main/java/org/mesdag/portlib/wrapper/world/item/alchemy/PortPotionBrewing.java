package org.mesdag.portlib.wrapper.world.item.alchemy;

import com.google.common.collect.Lists;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.brewing.IPortBrewingRecipe;

import java.util.List;

public class PortPotionBrewing {
    private static final PortPotionBrewing INSTANCE = new PortPotionBrewing();

    private PortPotionBrewing() {}

    public static PortPotionBrewing getInstance() {
        return INSTANCE;
    }

    public boolean isValidIngredient(ItemStack stack) {
        return BrewingRecipeRegistry.isValidIngredient(stack);
    }

    public boolean isValidInput(ItemStack stack) {
        return BrewingRecipeRegistry.isValidInput(stack);
    }

    public boolean hasOutput(ItemStack reagent, ItemStack potionItem) {
        return BrewingRecipeRegistry.hasOutput(reagent, potionItem);
    }

    public ItemStack getOutput(ItemStack reagent, ItemStack potionItem) {
        return BrewingRecipeRegistry.getOutput(reagent, potionItem);
    }

    public boolean isIngredient(ItemStack stack) {
        return PotionBrewing.isIngredient(stack);
    }

    public boolean isInput(ItemStack stack) {
        return isValidInput(stack) || PotionBrewing.ALLOWED_CONTAINER.test(stack);
    }

    public List<IPortBrewingRecipe> getRecipes() {
        return Lists.transform(BrewingRecipeRegistry.getRecipes(), IPortBrewingRecipe::wrap);
    }

    public boolean isContainerIngredient(ItemStack stack) {
        return PotionBrewing.isContainerIngredient(stack);
    }

    public boolean isPotionIngredient(ItemStack stack) {
        return PotionBrewing.isPotionIngredient(stack);
    }

    public boolean isBrewablePotion(PotionHolder potion) {
        return PotionBrewing.isBrewablePotion(potion.value());
    }

    public boolean hasMix(ItemStack reagent, ItemStack potionItem) {
        return PotionBrewing.hasMix(reagent, potionItem);
    }

    public boolean hasContainerMix(ItemStack reagent, ItemStack potionItem) {
        return PotionBrewing.hasContainerMix(reagent, potionItem);
    }

    public boolean hasPotionMix(ItemStack reagent, ItemStack potionItem) {
        return PotionBrewing.hasPotionMix(reagent, potionItem);
    }

    public ItemStack mix(ItemStack potion, ItemStack potionItem) {
        return PotionBrewing.mix(potion, potionItem);
    }

    public static class PortBuilder {
        private final FeatureFlagSet enabledFeatures;

        @Diff
        public PortBuilder(FeatureFlagSet enabledFeatures) {
            this.enabledFeatures = enabledFeatures;
        }

        public void addContainerRecipe(Item input, Item reagent, Item result) {
            if (input.isEnabled(enabledFeatures) && reagent.isEnabled(enabledFeatures) && result.isEnabled(enabledFeatures)) {
                PotionBrewing.addContainerRecipe(input, reagent, result);
            }
        }

        public void addContainer(Item container) {
            if (container.isEnabled(enabledFeatures)) {
                PotionBrewing.addContainer(container);
            }
        }

        public void addMix(PotionHolder input, Item reagent, PotionHolder result) {
            if (reagent.isEnabled(enabledFeatures)) {
                PotionBrewing.addMix(input.value(), reagent, result.value());
            }
        }

        public void addStartMix(Item reagent, PotionHolder result) {
            if (reagent.isEnabled(enabledFeatures)) {
                PotionBrewing.addMix(Potions.WATER, reagent, Potions.MUNDANE);
                PotionBrewing.addMix(Potions.AWKWARD, reagent, result.value());
            }
        }

        public void addRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
            BrewingRecipeRegistry.addRecipe(input, ingredient, output);
        }

        public void addRecipe(IPortBrewingRecipe recipe) {
            BrewingRecipeRegistry.addRecipe(recipe.unwrap());
        }
    }
}
