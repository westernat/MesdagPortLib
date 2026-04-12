package org.mesdag.portlib.event.other;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public final class PortBuildCreativeModeTabContentsEvent extends PortEvent<BuildCreativeModeTabContentsEvent> implements IPortModBusEvent, CreativeModeTab.Output {
    @Diff
    public PortBuildCreativeModeTabContentsEvent(BuildCreativeModeTabContentsEvent e) {
        super(e);
    }

    public CreativeModeTab getTab() {
        return e.getTab();
    }

    public ResourceKey<CreativeModeTab> getTabKey() {
        return e.getTabKey();
    }

    public FeatureFlagSet getFlags() {
        return e.getFlags();
    }

    public CreativeModeTab.ItemDisplayParameters getParameters() {
        return e.getParameters();
    }

    public boolean hasPermissions() {
        return e.hasPermissions();
    }

//    public Iterable<ItemStack> getParentEntries() {
//        return e.getParentEntries();
//    }
//
//    public Iterable<ItemStack> getSearchEntries() {
//        return e.getSearchEntries();
//    }

    @Override
    public void accept(ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        e.accept(newEntry, visibility);
    }

    public void insertAfter(ItemStack existingEntry, ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        e.insertAfter(existingEntry, newEntry, visibility);
    }

    public void insertBefore(ItemStack existingEntry, ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        e.insertBefore(existingEntry, newEntry, visibility);
    }

    public void insertFirst(ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        e.insertFirst(newEntry, visibility);
    }

    public void remove(ItemStack existingEntry, CreativeModeTab.TabVisibility visibility) {
        e.remove(existingEntry, visibility);
    }

    static {
        PortEventHooks.register(BuildCreativeModeTabContentsEvent.class, PortBuildCreativeModeTabContentsEvent.class, PortBuildCreativeModeTabContentsEvent::new);
    }
}
