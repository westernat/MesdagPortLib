package org.mesdag.portlib.event.client;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.item.crafting.PortRecipeHolder;

import java.util.List;
import java.util.function.Function;

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

    public void registerRecipeCategoryFinder(RecipeType<?> type, Function<PortRecipeHolder<?>, RecipeBookCategories> lookup) {
        e.registerRecipeCategoryFinder(type, recipe -> lookup.apply(new PortRecipeHolder<>(recipe.getId(), recipe)));
    }

    static {
        PortEventHooks.register();
    }
}
