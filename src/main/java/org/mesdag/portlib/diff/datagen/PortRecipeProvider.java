package org.mesdag.portlib.diff.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.level.block.Blocks;
import org.mesdag.portlib.PortLib;

import java.util.function.Consumer;

public class PortRecipeProvider extends VanillaRecipeProvider {
    public PortRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeOutput) {
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_SLAB, Blocks.TUFF, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_STAIRS, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.DECORATIONS, PortLib.TUFF_WALL, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.CHISELED_TUFF, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.POLISHED_TUFF, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.POLISHED_TUFF_SLAB, Blocks.TUFF, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.POLISHED_TUFF_STAIRS, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.DECORATIONS, PortLib.POLISHED_TUFF_WALL, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_BRICKS, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_BRICK_SLAB, Blocks.TUFF, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_BRICK_STAIRS, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.DECORATIONS, PortLib.TUFF_BRICK_WALL, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.CHISELED_TUFF_BRICKS, Blocks.TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.POLISHED_TUFF_SLAB, PortLib.POLISHED_TUFF, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.POLISHED_TUFF_STAIRS, PortLib.POLISHED_TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.DECORATIONS, PortLib.POLISHED_TUFF_WALL, PortLib.POLISHED_TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_BRICKS, PortLib.POLISHED_TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_BRICK_SLAB, PortLib.POLISHED_TUFF, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_BRICK_STAIRS, PortLib.POLISHED_TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.DECORATIONS, PortLib.TUFF_BRICK_WALL, PortLib.POLISHED_TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.CHISELED_TUFF_BRICKS, PortLib.POLISHED_TUFF);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_BRICK_SLAB, PortLib.TUFF_BRICKS, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.TUFF_BRICK_STAIRS, PortLib.TUFF_BRICKS);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.DECORATIONS, PortLib.TUFF_BRICK_WALL, PortLib.TUFF_BRICKS);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, PortLib.CHISELED_TUFF_BRICKS, PortLib.TUFF_BRICKS);
    }
}
