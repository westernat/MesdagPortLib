package org.mesdag.portlib.event.other;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.event.LootTableLoadEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortLootTableLoadEvent extends PortEvent<LootTableLoadEvent> implements IPortCancellableEvent {
    @Nullable
    private ResourceKey<LootTable> key;

    @Diff
    public PortLootTableLoadEvent(LootTableLoadEvent e) {
        super(e);
    }

    public HolderLookup.Provider getRegistries() {
        return RegistryAccess.EMPTY;
    }

    public ResourceLocation getName() {
        return e.getName();
    }

//    public ResourceKey<LootTable> getKey() {
//        if (this.key == null) {
//            this.key = ResourceKey.create(Registries.LOOT_TABLE, getName());
//        }
//        return this.key;
//    }

    public LootTable getTable() {
        return e.getTable();
    }

    public void setTable(LootTable table) {
        e.setTable(table);
    }

    static {
        PortEventHooks.register();
    }
}
