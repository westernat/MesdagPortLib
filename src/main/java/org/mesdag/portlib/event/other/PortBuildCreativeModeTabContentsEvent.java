package org.mesdag.portlib.event.other;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
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
//        return Iterables.unmodifiableIterable(Iterables.transform(Iterables.filter(e.getEntries(), entry -> entry.getValue() == CreativeModeTab.TabVisibility.PARENT_TAB_ONLY), Map.Entry::getKey));
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
        assertStackCount(newEntry);

        assertTargetExists(e.getEntries(), existingEntry);
        assertNewEntryDoesNotAlreadyExists(e.getEntries(), newEntry);
        e.getEntries().putAfter(existingEntry, newEntry, visibility);
    }

    public void insertBefore(ItemStack existingEntry, ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        assertStackCount(newEntry);

        assertTargetExists(e.getEntries(), existingEntry);
        assertNewEntryDoesNotAlreadyExists(e.getEntries(), newEntry);
        e.getEntries().putBefore(existingEntry, newEntry, visibility);
    }

    public void insertFirst(ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        assertStackCount(newEntry);

        e.getEntries().putFirst(newEntry, visibility);
    }

    public void remove(ItemStack existingEntry, CreativeModeTab.TabVisibility visibility) {
        assertTargetExists(e.getEntries(), existingEntry);
        e.getEntries().remove(existingEntry);
    }

    static boolean isParentTab(CreativeModeTab.TabVisibility visibility) {
        return visibility == CreativeModeTab.TabVisibility.PARENT_TAB_ONLY || visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
    }

    static boolean isSearchTab(CreativeModeTab.TabVisibility visibility) {
        return visibility == CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY || visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
    }

    private void assertTargetExists(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> setToCheck, ItemStack existingEntry) {
        if (!setToCheck.contains(existingEntry)) {
            throw new IllegalArgumentException("Itemstack " + existingEntry + " does not exist in tab's list");
        }
    }

    private void assertNewEntryDoesNotAlreadyExists(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> setToCheck, ItemStack newEntry) {
        if (setToCheck.contains(newEntry)) {
            throw new IllegalArgumentException("Itemstack " + newEntry + " already exists in the tab's list");
        }
    }

    private static void assertStackCount(ItemStack newEntry) {
        if (newEntry.getCount() != 1) {
            throw new IllegalArgumentException("The stack count must be 1 for " + newEntry);
        }
    }

    static {
        PortEventHooks.register(BuildCreativeModeTabContentsEvent.class, PortBuildCreativeModeTabContentsEvent.class, PortBuildCreativeModeTabContentsEvent::new);
    }
}
