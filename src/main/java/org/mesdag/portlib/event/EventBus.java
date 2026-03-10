package org.mesdag.portlib.event;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

public enum EventBus {
    MOD {
        @Override
        IEventBus unwrap() {
            return Mod.EventBusSubscriber.Bus.MOD.bus().get();
        }
    },
    GAME {
        @Override
        IEventBus unwrap() {
            return Mod.EventBusSubscriber.Bus.FORGE.bus().get();
        }
    };

    abstract IEventBus unwrap();
}
