package org.mesdag.portlib.event;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import org.mesdag.portlib.diff.Diff;

@SuppressWarnings("all")
public enum PortBus {
    MOD,
    GAME;

    @Diff
    public IEventBus unwrap(String modid) {
        return this == MOD ? ModList.get().getModContainerById(modid).orElseThrow().getEventBus() : NeoForge.EVENT_BUS;
    }
}
