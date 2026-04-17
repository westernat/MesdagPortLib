package org.mesdag.portlib.event.entity.client.event;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterSpriteSourceTypesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;



public class PortRegisterSpriteSourceTypesEvent extends PortEvent {
    private final RegisterSpriteSourceTypesEvent e;

    @Diff
    public PortRegisterSpriteSourceTypesEvent(RegisterSpriteSourceTypesEvent e) {
        super(e);
        this.e = e;
    }

    @Deprecated
    public SpriteSourceType register(ResourceLocation id, MapCodec<? extends SpriteSource> codec) {
        return e.register(id, codec);
    }

    public void register(ResourceLocation id, SpriteSourceType sourceType) {
        e.register(id, sourceType);
    }

    static {
        PortEventHooks.register(RegisterSpriteSourceTypesEvent.class, PortRegisterSpriteSourceTypesEvent.class, PortRegisterSpriteSourceTypesEvent::new);
    }
}