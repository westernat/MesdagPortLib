package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;
import java.util.function.Function;

public class PortRegisterRecipeBookCategoriesEvent extends PortEvent {
    private final RegisterRecipeBookCategoriesEvent e;

    @Diff
    public PortRegisterRecipeBookCategoriesEvent(RegisterRecipeBookCategoriesEvent e) {
        super(e);
        this.e = e;
    }

    public void registerAggregateCategory(RecipeBookCategories category, List<RecipeBookCategories> others) {
        e.registerAggregateCategory(category, others);
    }

    public void registerBookCategories(RecipeBookType type, List<RecipeBookCategories> categories) {
        e.registerBookCategories(type, categories);
    }

    public void registerRecipeCategoryFinder(RecipeType<?> type, Function<RecipeHolder<?>, RecipeBookCategories> lookup) {
        e.registerRecipeCategoryFinder(type, lookup);
    }

    static {
        PortEventHooks.register(RegisterRecipeBookCategoriesEvent.class, PortRegisterRecipeBookCategoriesEvent.class, PortRegisterRecipeBookCategoriesEvent::new);
    }
}