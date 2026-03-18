package org.mesdag.portlib.event.brewing;

import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionBrewing;

public class PortRegisterBrewingRecipesEvent extends PortEvent {
    private final PortPotionBrewing.PortBuilder builder;
    private final PortRegistryAccess registryAccess;

    @Diff
    public PortRegisterBrewingRecipesEvent(RegisterBrewingRecipesEvent e) {
        this.builder = new PortPotionBrewing.PortBuilder(e.getBuilder());
        this.registryAccess = new PortRegistryAccess(e.getRegistryAccess());
    }

    public PortPotionBrewing.PortBuilder getBuilder() {
        return builder;
    }

    public PortRegistryAccess getRegistryAccess() {
        return registryAccess;
    }

    static {
        PortEventHooks.register(RegisterBrewingRecipesEvent.class, PortRegisterBrewingRecipesEvent.class, PortRegisterBrewingRecipesEvent::new);
    }
}
