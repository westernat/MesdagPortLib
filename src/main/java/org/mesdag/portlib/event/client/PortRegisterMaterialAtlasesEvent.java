package org.mesdag.portlib.event.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;

import java.util.HashMap;
import java.util.Map;

public class PortRegisterMaterialAtlasesEvent extends Event {
    private final Map<ResourceLocation, ResourceLocation> originalAtlases;
    private Map<ResourceLocation, ResourceLocation> atlases;

    @Diff
    public PortRegisterMaterialAtlasesEvent(Map<ResourceLocation, ResourceLocation> atlases) {
        this.originalAtlases = atlases;
    }

    public void register(ResourceLocation atlasLocation, ResourceLocation atlasInfoLocation) {
        if (atlases == null) {
            this.atlases = new HashMap<>(originalAtlases);
        }
        ResourceLocation oldAtlasInfoLoc = atlases.putIfAbsent(atlasLocation, atlasInfoLocation);
        if (oldAtlasInfoLoc != null) {
            throw new IllegalStateException(String.format(
                    "Duplicate registration of atlas: %s (old info: %s, new info: %s)",
                    atlasLocation,
                    oldAtlasInfoLoc,
                    atlasInfoLocation
            ));
        }
    }

    @Diff
    public Map<ResourceLocation, ResourceLocation> getAtlases() {
        return atlases == null ? originalAtlases : atlases;
    }
}
