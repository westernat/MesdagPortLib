package org.mesdag.portlib.event.client;

import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.neoforge.client.event.RegisterPresetEditorsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterPresetEditorsEvent extends PortEvent<RegisterPresetEditorsEvent> {
    @Diff
    public PortRegisterPresetEditorsEvent(RegisterPresetEditorsEvent e) {
        super(e);
    }

    public void register(ResourceKey<WorldPreset> key, PresetEditor editor) {
        e.register(key, editor);
    }

    static {
        PortEventHooks.register();
    }
}
