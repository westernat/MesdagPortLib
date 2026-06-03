package org.mesdag.portlib.event.client;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;
import java.util.function.BiFunction;

public class PortRegisterRecipeBookCategoriesEvent extends PortEvent<RegisterRecipeBookCategoriesEvent> {
    @Diff
    public PortRegisterRecipeBookCategoriesEvent(RegisterRecipeBookCategoriesEvent e) {
        super(e);
    }

    public void registerAggregateCategory(RecipeBookCategories category, List<RecipeBookCategories> others) {
        e.registerAggregateCategory(category, others);
    }

    public void registerBookCategories(RecipeBookType type, List<RecipeBookCategories> categories) {
        e.registerBookCategories(type, categories);
    }

    public void registerRecipeCategoryFinder(RecipeType<?> type, BiFunction<ResourceLocation, Recipe<?>, RecipeBookCategories> lookup) {
        e.registerRecipeCategoryFinder(type, recipe -> lookup.apply(recipe.getId(), recipe));
    }

    static {
        PortEventHooks.register();
    }
}
