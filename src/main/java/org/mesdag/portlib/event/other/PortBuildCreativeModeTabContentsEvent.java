package org.mesdag.portlib.event.other;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;
import org.mesdag.portlib.diff.Diff;

public final class PortBuildCreativeModeTabContentsEvent extends Event implements IModBusEvent, CreativeModeTab.Output {
    private final CreativeModeTab tab;
    private final ResourceKey<CreativeModeTab> tabKey;
    private final CreativeModeTab.ItemDisplayParameters parameters;
    private final MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> parentEntries;
    private final MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> searchEntries;

    @Diff
    public PortBuildCreativeModeTabContentsEvent(
            CreativeModeTab tab,
            ResourceKey<CreativeModeTab> tabKey,
            CreativeModeTab.ItemDisplayParameters parameters,
            MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> parentEntries,
            MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> searchEntries
    ) {
        this.tab = tab;
        this.tabKey = tabKey;
        this.parameters = parameters;
        this.parentEntries = parentEntries;
        this.searchEntries = searchEntries;
    }

    public CreativeModeTab getTab() {
        return tab;
    }

    public ResourceKey<CreativeModeTab> getTabKey() {
        return tabKey;
    }

    public FeatureFlagSet getFlags() {
        return parameters.enabledFeatures();
    }

    public CreativeModeTab.ItemDisplayParameters getParameters() {
        return parameters;
    }

    public boolean hasPermissions() {
        return parameters.hasPermissions();
    }

    public MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> getParentEntries() {
        return parentEntries;
    }

    public MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> getSearchEntries() {
        return searchEntries;
    }

    @Override
    public void accept(ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        assertStackCount(newEntry);

        if (isParentTab(visibility)) {
            assertNewEntryDoesNotAlreadyExists(parentEntries, newEntry);
            parentEntries.put(newEntry, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        }

        if (isSearchTab(visibility)) {
            assertNewEntryDoesNotAlreadyExists(searchEntries, newEntry);
            searchEntries.put(newEntry, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
        }
    }

    public void insertAfter(ItemStack existingEntry, ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        assertStackCount(newEntry);

        if (isParentTab(visibility)) {
            assertTargetExists(parentEntries, existingEntry);
            assertNewEntryDoesNotAlreadyExists(parentEntries, newEntry);
            parentEntries.putAfter(existingEntry, newEntry, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
        }

        if (isSearchTab(visibility)) {
            assertTargetExists(searchEntries, existingEntry);
            assertNewEntryDoesNotAlreadyExists(searchEntries, newEntry);
            searchEntries.putAfter(existingEntry, newEntry, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        }
    }

    public void insertBefore(ItemStack existingEntry, ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        assertStackCount(newEntry);

        if (isParentTab(visibility)) {
            assertTargetExists(parentEntries, existingEntry);
            assertNewEntryDoesNotAlreadyExists(parentEntries, newEntry);
            parentEntries.putBefore(existingEntry, newEntry, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        }

        if (isSearchTab(visibility)) {
            assertTargetExists(searchEntries, existingEntry);
            assertNewEntryDoesNotAlreadyExists(searchEntries, newEntry);
            searchEntries.putBefore(existingEntry, newEntry, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
        }
    }

    public void insertFirst(ItemStack newEntry, CreativeModeTab.TabVisibility visibility) {
        assertStackCount(newEntry);

        if (isParentTab(visibility)) {
            assertNewEntryDoesNotAlreadyExists(parentEntries, newEntry);
            parentEntries.putFirst(newEntry, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
        }

        if (isSearchTab(visibility)) {
            assertNewEntryDoesNotAlreadyExists(searchEntries, newEntry);
            searchEntries.putFirst(newEntry, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
        }
    }

    public void remove(ItemStack existingEntry, CreativeModeTab.TabVisibility visibility) {
        if (isParentTab(visibility)) {
            parentEntries.remove(existingEntry);
        }

        if (isSearchTab(visibility)) {
            searchEntries.remove(existingEntry);
        }
    }

    @ApiStatus.Internal
    public static boolean isParentTab(CreativeModeTab.TabVisibility visibility) {
        return visibility == CreativeModeTab.TabVisibility.PARENT_TAB_ONLY || visibility == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
    }

    @ApiStatus.Internal
    public static boolean isSearchTab(CreativeModeTab.TabVisibility visibility) {
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
}
