package org.mesdag.portlib.event.brewing;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.item.alchemy.PortPotionBrewing;

public class PortRegisterBrewingRecipesEvent extends Event {
    private final PortPotionBrewing.Builder builder;
    private final RegistryAccess registryAccess;

    @Diff
    public PortRegisterBrewingRecipesEvent(FeatureFlagSet enabledFeatures, RegistryAccess registryAccess) {
        this.builder = new PortPotionBrewing.Builder(enabledFeatures);
        this.registryAccess = registryAccess;
    }

    public PortPotionBrewing.Builder getBuilder() {
        return builder;
    }

    public RegistryAccess getRegistryAccess() {
        return registryAccess;
    }
}
