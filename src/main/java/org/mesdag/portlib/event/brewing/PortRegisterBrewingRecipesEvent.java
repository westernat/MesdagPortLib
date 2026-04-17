package org.mesdag.portlib.event.brewing;

import net.minecraft.core.RegistryAccess;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionBrewing;

public class PortRegisterBrewingRecipesEvent extends PortEvent<RegisterBrewingRecipesEvent> {
    @Diff
    public PortRegisterBrewingRecipesEvent(RegisterBrewingRecipesEvent e) {
        super(e);
    }

    public PortPotionBrewing.PortBuilder getBuilder() {
        return new PortPotionBrewing.PortBuilder(e.getBuilder());
    }

    public RegistryAccess getRegistryAccess() {
        return e.getRegistryAccess();
    }

    static {
        PortEventHooks.register(RegisterBrewingRecipesEvent.class, PortRegisterBrewingRecipesEvent.class, PortRegisterBrewingRecipesEvent::new);
    }
}
