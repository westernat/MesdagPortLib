package org.mesdag.portlib.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import org.mesdag.portlib.diff.Diff;

@SuppressWarnings("all")
public enum PortBus {
    MOD,
    GAME;

    @Diff
    public IEventBus unwrap(String modid) {
        if (this == MOD) {
            if (ModList.get().getModContainerById(modid).orElseThrow() instanceof FMLModContainer container) {
                return container.getEventBus();
            }
            throw new IllegalStateException("IEventBus not found for modid: " + modid);
        } else {
            return MinecraftForge.EVENT_BUS;
        }
    }
}
