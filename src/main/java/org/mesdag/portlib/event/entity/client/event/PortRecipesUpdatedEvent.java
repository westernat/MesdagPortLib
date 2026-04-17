package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRecipesUpdatedEvent extends PortEvent {
    private final RecipesUpdatedEvent e;

    @Diff
    public PortRecipesUpdatedEvent(RecipesUpdatedEvent e) {
        super(e);
        this.e = e;
    }

    public RecipeManager getRecipeManager() {
        return e.getRecipeManager();
    }

    static {
        PortEventHooks.register(RecipesUpdatedEvent.class, PortRecipesUpdatedEvent.class, PortRecipesUpdatedEvent::new
        );
    }
}