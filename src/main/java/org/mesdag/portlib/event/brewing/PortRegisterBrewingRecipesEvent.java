package org.mesdag.portlib.event.brewing;

import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionBrewing;

public class PortRegisterBrewingRecipesEvent extends PortEvent<RegisterBrewingRecipesEvent> {
    @Diff
    public PortRegisterBrewingRecipesEvent(RegisterBrewingRecipesEvent e) {
        super(e);
    }

    public PortPotionBrewing.PortBuilder getBuilder() {
        return new PortPotionBrewing.PortBuilder(e.getBuilder());
    }

    public PortRegistryAccess getRegistryAccess() {
        return new PortRegistryAccess(e.getRegistryAccess());
    }

    static {
        PortEventHooks.register(RegisterBrewingRecipesEvent.class, PortRegisterBrewingRecipesEvent.class, PortRegisterBrewingRecipesEvent::new);
    }
}
