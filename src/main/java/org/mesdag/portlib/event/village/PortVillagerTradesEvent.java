package org.mesdag.portlib.event.village;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.List;

public class PortVillagerTradesEvent extends PortEvent<VillagerTradesEvent> {
    @Diff
    public PortVillagerTradesEvent(VillagerTradesEvent e) {
        super(e);
    }

    public Int2ObjectMap<List<ItemListing>> getTrades() {
        return e.getTrades();
    }

    public VillagerProfession getType() {
        return e.getType();
    }

    public RegistryAccess getRegistryAccess() {
        return e.getRegistryAccess();
    }

    static {
        PortEventHooks.register(VillagerTradesEvent.class, PortVillagerTradesEvent.class, PortVillagerTradesEvent::new);
    }
}
