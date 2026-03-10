package org.mesdag.portlib.event;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import org.mesdag.portlib.PortLib;

public enum EventBus {
    MOD {
        @Override
        IEventBus unwrap() {
            return ModList.get().getModContainerById(PortLib.MODID).orElseThrow().getEventBus();
        }
    },
    GAME {
        @Override
        IEventBus unwrap() {
            return NeoForge.EVENT_BUS;
        }
    };

    abstract IEventBus unwrap();
}
