package org.mesdag.portlib.event.village;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

import java.util.List;

public class PortVillagerTradesEvent extends PortEvent {
    private final VillagerTradesEvent internal;

    public PortVillagerTradesEvent(VillagerTradesEvent internal) {
        this.internal = internal;
    }

    public Int2ObjectMap<List<ItemListing>> getTrades() {
        return internal.getTrades();
    }

    public VillagerProfession getType() {
        return internal.getType();
    }

    public PortRegistryAccess getRegistryAccess() {
        return new PortRegistryAccess(internal.getRegistryAccess());
    }

    static {
        PortEventHooks.register(VillagerTradesEvent.class, PortVillagerTradesEvent.class, PortVillagerTradesEvent::new);
    }
}
