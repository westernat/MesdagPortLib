package org.mesdag.portlib.event.client;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.client.event.TextureStitchEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortTextureAtlasStitchedEvent extends PortEvent<TextureStitchEvent.Post> {
    @Diff
    public PortTextureAtlasStitchedEvent(TextureStitchEvent.Post e) {
        super(e);
    }

    public TextureAtlas getAtlas() {
        return e.getAtlas();
    }

    static {
        PortEventHooks.register();
    }
}
