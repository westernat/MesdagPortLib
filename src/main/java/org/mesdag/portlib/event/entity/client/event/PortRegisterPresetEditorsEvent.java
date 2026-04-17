package org.mesdag.portlib.event.entity.client.event;

import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.neoforge.client.event.RegisterPresetEditorsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortRegisterPresetEditorsEvent extends PortEvent {
    private final RegisterPresetEditorsEvent e;

    @Diff
    public PortRegisterPresetEditorsEvent(RegisterPresetEditorsEvent e) {
        super(e);
        this.e = e;
    }

    public void register(ResourceKey<WorldPreset> key, PresetEditor editor) {
        e.register(key, editor);
    }

    static {
        PortEventHooks.register(RegisterPresetEditorsEvent.class, PortRegisterPresetEditorsEvent.class, PortRegisterPresetEditorsEvent::new);
    }
}