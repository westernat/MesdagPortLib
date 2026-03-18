package org.mesdag.portlib.wrapper.world.item.alchemy;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.common.brewing.IPortBrewingRecipe;

import java.util.List;
import java.util.Objects;

public class PortPotionBrewing {
    private final PotionBrewing delegate;

    private PortPotionBrewing(PotionBrewing delegate) {
        this.delegate = delegate;
    }

    public static PortPotionBrewing getInstance() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null && PortEnvironment.isPhysicalClient()) {
            return new PortPotionBrewing(Objects.requireNonNull(Minecraft.getInstance().getConnection(), "No Connection Found").potionBrewing());
        }
        return new PortPotionBrewing(Objects.requireNonNull(server, "No Server Found").potionBrewing());
    }

    public boolean isValidIngredient(ItemStack stack) {
        return delegate.registry.isValidIngredient(stack);
    }

    public boolean isValidInput(ItemStack stack) {
        return delegate.registry.isValidInput(stack);
    }

    public boolean hasOutput(ItemStack reagent, ItemStack potionItem) {
        return delegate.registry.hasOutput(reagent, potionItem);
    }

    public ItemStack getOutput(ItemStack reagent, ItemStack potionItem) {
        return delegate.registry.getOutput(reagent, potionItem);
    }

    public boolean isIngredient(ItemStack stack) {
        return delegate.isIngredient(stack);
    }

    public boolean isInput(ItemStack stack) {
        return delegate.isInput(stack);
    }

    public List<IPortBrewingRecipe> getRecipes() {
        return Lists.transform(delegate.getRecipes(), IPortBrewingRecipe::wrap);
    }

    public boolean isContainerIngredient(ItemStack stack) {
        return delegate.isContainerIngredient(stack);
    }

    public boolean isPotionIngredient(ItemStack stack) {
        return delegate.isPotionIngredient(stack);
    }

    public boolean isBrewablePotion(PotionHolder potion) {
        return delegate.isBrewablePotion(potion.delegate());
    }

    public boolean hasMix(ItemStack reagent, ItemStack potionItem) {
        return delegate.hasMix(reagent, potionItem);
    }

    public boolean hasContainerMix(ItemStack reagent, ItemStack potionItem) {
        return delegate.hasContainerMix(reagent, potionItem);
    }

    public boolean hasPotionMix(ItemStack reagent, ItemStack potionItem) {
        return delegate.hasPotionMix(reagent, potionItem);
    }

    public ItemStack mix(ItemStack potion, ItemStack potionItem) {
        return delegate.mix(potion, potionItem);
    }

    public static class PortBuilder {
        private final PotionBrewing.Builder builder;

        @Diff
        public PortBuilder(PotionBrewing.Builder builder) {
            this.builder = builder;
        }

        public void addContainerRecipe(Item input, Item reagent, Item result) {
            builder.addContainerRecipe(input, reagent, result);
        }

        public void addContainer(Item container) {
            builder.addContainer(container);
        }

        public void addMix(PotionHolder input, Item reagent, PotionHolder result) {
            builder.addMix(input.delegate(), reagent, result.delegate());
        }

        public void addStartMix(Item reagent, PotionHolder result) {
            builder.addStartMix(reagent, result.delegate());
        }

        public void addRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
            builder.addRecipe(input, ingredient, output);
        }

        public void addRecipe(IPortBrewingRecipe recipe) {
            builder.addRecipe(recipe.unwrap());
        }
    }
}
