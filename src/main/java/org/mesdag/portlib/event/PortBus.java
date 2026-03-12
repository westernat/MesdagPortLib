package org.mesdag.portlib.event;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.diff.Diff;

@SuppressWarnings("all")
public enum PortBus {
    MOD {
        @Override
        public IEventBus unwrap() {
            return ModList.get().getModContainerById(PortLib.MODID).orElseThrow().getEventBus();
        }
    },
    GAME {
        @Override
        public IEventBus unwrap() {
            return NeoForge.EVENT_BUS;
        }
    };

    @Diff
    public abstract IEventBus unwrap();
}
