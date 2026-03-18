package org.mesdag.portlib.wrapper.world.item.alchemy;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.wrapper.common.brewing.IPortBrewingRecipe;

import java.util.ArrayList;
import java.util.List;

public class PortPotionBrewing {


    public static class PortBuilder {
        private final List<Ingredient> containers = new ArrayList<>();
        private final List<PotionBrewing.Mix<Potion>> potionMixes = new ArrayList<>();
        private final List<PotionBrewing.Mix<Item>> containerMixes = new ArrayList<>();
        private final List<IPortBrewingRecipe> recipes = new ArrayList<>();
        private final FeatureFlagSet enabledFeatures;

        public PortBuilder(FeatureFlagSet enabledFeatures) {
            this.enabledFeatures = enabledFeatures;
        }

        private static void expectPotion(Item item) {
            if (!(item instanceof PotionItem)) {
                throw new IllegalArgumentException("Expected a potion, got: " + ForgeRegistries.ITEMS.getKey(item));
            }
        }

        public void addContainerRecipe(Item input, Item reagent, Item result) {
            if (input.isEnabled(enabledFeatures) && reagent.isEnabled(enabledFeatures) && result.isEnabled(enabledFeatures)) {
                PotionBrewing.addContainerRecipe(input, Ingredient.of(reagent), result);
            }
        }

        public void addContainer(Item container) {
            if (container.isEnabled(enabledFeatures)) {
                PotionBrewing.addContainer(container);
            }
        }

        public void addMix(PotionHolder input, Item reagent, PotionHolder result) {
            if (reagent.isEnabled(enabledFeatures)) {
                PotionBrewing.addMix(input.value(), Ingredient.of(reagent), result.value());
            }
        }

        public void addStartMix(Item reagent, PotionHolder result) {
            addMix(Potions.WATER, reagent, Potions.MUNDANE);
            addMix(Potions.AWKWARD, reagent, result);
        }

        public void addRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
            addRecipe(new net.neoforged.neoforge.common.brewing.BrewingRecipe(input, ingredient, output));
        }

        public void addRecipe(IPortBrewingRecipe recipe) {
            recipes.add(recipe);
        }

        public PotionBrewing build() {
            return new PotionBrewing(List.copyOf(containers), List.copyOf(potionMixes), List.copyOf(containerMixes), List.copyOf(recipes));
        }
    }
}
