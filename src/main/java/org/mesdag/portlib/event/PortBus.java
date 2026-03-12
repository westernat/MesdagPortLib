package org.mesdag.portlib.event;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import org.mesdag.portlib.diff.Diff;

@SuppressWarnings("all")
public enum PortBus {
    MOD {
        @Override
        public IEventBus unwrap() {
            return Mod.EventBusSubscriber.Bus.MOD.bus().get();
        }
    },
    GAME {
        @Override
        public IEventBus unwrap() {
            return Mod.EventBusSubscriber.Bus.FORGE.bus().get();
        }
    };

    @Diff
    public abstract IEventBus unwrap();
}
