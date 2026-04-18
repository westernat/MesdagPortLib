package org.mesdag.portlib.event.client;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortTextureAtlasStitchedEvent extends PortEvent<TextureAtlasStitchedEvent> {
    @Diff
    public PortTextureAtlasStitchedEvent(TextureAtlasStitchedEvent e) {
        super(e);
    }

    public TextureAtlas getAtlas() {
        return e.getAtlas();
    }

    static {
        PortEventHooks.register();
    }
}
