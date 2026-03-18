package org.mesdag.portlib.event.brewing;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionBrewing;

public class PortRegisterBrewingRecipesEvent extends Event {
    private final PortPotionBrewing.PortBuilder builder;
    private final PortRegistryAccess registryAccess;

    @Diff
    public PortRegisterBrewingRecipesEvent(FeatureFlagSet enabledFeatures, RegistryAccess registryAccess) {
        this.builder = new PortPotionBrewing.PortBuilder(enabledFeatures);
        this.registryAccess = new PortRegistryAccess(registryAccess);
    }

    public PortPotionBrewing.PortBuilder getBuilder() {
        return builder;
    }

    public PortRegistryAccess getRegistryAccess() {
        return registryAccess;
    }
}
