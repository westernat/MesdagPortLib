package org.mesdag.portlib.event.village;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

import java.util.List;

public class PortWandererTradesEvent extends PortEvent {
    private final WandererTradesEvent internal;

    public PortWandererTradesEvent(WandererTradesEvent internal) {
        this.internal = internal;
    }

    public List<VillagerTrades.ItemListing> getGenericTrades() {
        return internal.getGenericTrades();
    }

    public List<VillagerTrades.ItemListing> getRareTrades() {
        return internal.getRareTrades();
    }

    public PortRegistryAccess getRegistryAccess() {
        return new PortRegistryAccess(internal.getRegistryAccess());
    }

    static {
        PortEventHooks.register(WandererTradesEvent.class, PortWandererTradesEvent.class, PortWandererTradesEvent::new);
    }
}
