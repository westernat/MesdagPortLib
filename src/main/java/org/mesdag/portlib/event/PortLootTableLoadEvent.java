package org.mesdag.portlib.event;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import org.mesdag.portlib.diff.Diff;


public class PortLootTableLoadEvent extends PortEvent implements IPortCancellableEvent {

    private final LootTableLoadEvent e;

    @Diff
    public PortLootTableLoadEvent(LootTableLoadEvent e) {
        this.e = e;
    }

    public HolderLookup.Provider getRegistries() {
        return e.getRegistries();
    }

    public ResourceLocation getName() {
        return e.getName();
    }

    public ResourceKey<LootTable> getKey() {
        return e.getKey();
    }

    public LootTable getTable() {
        return e.getTable();
    }

    public void setTable(LootTable table) {
        e.setTable(table);
    }

    static {
        PortEventHooks.register(LootTableLoadEvent.class, PortLootTableLoadEvent.class, PortLootTableLoadEvent::new);
    }
}