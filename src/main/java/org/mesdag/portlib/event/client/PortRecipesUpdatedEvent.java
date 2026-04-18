package org.mesdag.portlib.event.client;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRecipesUpdatedEvent extends PortEvent<RecipesUpdatedEvent> {
    @Diff
    public PortRecipesUpdatedEvent(RecipesUpdatedEvent e) {
        super(e);
    }

    public RecipeManager getRecipeManager() {
        return e.getRecipeManager();
    }

    static {
        PortEventHooks.register();
    }
}
