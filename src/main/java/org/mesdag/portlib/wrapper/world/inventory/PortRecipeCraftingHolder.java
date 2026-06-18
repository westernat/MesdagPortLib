package org.mesdag.portlib.wrapper.world.inventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public interface PortRecipeCraftingHolder {
    void setRecipeUsed(@Nullable Recipe<?> recipe);

    @Nullable Recipe<?> getRecipeUsed();

    default void awardUsedRecipes(Player player, List<ItemStack> items) {
        Recipe<?> recipe = getRecipeUsed();
        if (recipe != null) {
            player.triggerRecipeCrafted(recipe, items);
            if (!recipe.isSpecial()) {
                player.awardRecipes(Collections.singleton(recipe));
                setRecipeUsed(null);
            }
        }
    }

    default boolean setRecipeUsed(Level level, ServerPlayer players, Recipe<?> recipe) {
        if (!recipe.isSpecial()
                && level.getGameRules().getBoolean(GameRules.RULE_LIMITED_CRAFTING)
                && !players.getRecipeBook().contains(recipe)
        ) {
            return false;
        }
        setRecipeUsed(recipe);
        return true;
    }
}
