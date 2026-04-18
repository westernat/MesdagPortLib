package org.mesdag.portlib.event.client;

import com.google.common.collect.BiMap;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import org.mesdag.portlib.diff.Diff;

public class PortRegisterSpriteSourceTypesEvent extends Event {
    private final BiMap<ResourceLocation, SpriteSourceType> types;

    @Diff
    public PortRegisterSpriteSourceTypesEvent(BiMap<ResourceLocation, SpriteSourceType> types) {
        this.types = types;
    }

    public void register(ResourceLocation id, SpriteSourceType sourceType) {
        if (this.types.containsKey(id)) {
            throw new IllegalStateException("Duplicate sprite source type registration " + id);
        }
        this.types.put(id, sourceType);
    }
}
