package org.mesdag.portlib.event.village;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortWandererTradesEvent extends PortEvent<WandererTradesEvent> {
    @Diff
    public PortWandererTradesEvent(WandererTradesEvent e) {
        super(e);
    }

    public List<VillagerTrades.ItemListing> getGenericTrades() {
        return e.getGenericTrades();
    }

    public List<VillagerTrades.ItemListing> getRareTrades() {
        return e.getRareTrades();
    }

    public RegistryAccess getRegistryAccess() {
        return e.getRegistryAccess();
    }

    static {
        PortEventHooks.register(WandererTradesEvent.class, PortWandererTradesEvent.class, PortWandererTradesEvent::new);
    }
}
