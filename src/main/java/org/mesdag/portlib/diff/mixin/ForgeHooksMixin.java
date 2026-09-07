package org.mesdag.portlib.diff.mixin;

import com.google.common.collect.Iterators;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.other.PortBuildCreativeModeTabContentsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;
import java.util.Map;

@Mixin(value = ForgeHooks.class, remap = false)
public abstract class ForgeHooksMixin {
    @WrapOperation(method = "onCreativeModeTabBuildContents", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/common/util/MutableHashedLinkedMap;iterator()Ljava/util/Iterator;", remap = false), remap = false)
    private static Iterator<Map.Entry<ItemStack, CreativeModeTab.TabVisibility>> portBehaviour(
            MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> instance,
            Operation<Iterator<Map.Entry<ItemStack, CreativeModeTab.TabVisibility>>> original,
            @Local(argsOnly = true) CreativeModeTab tab,
            @Local(argsOnly = true) ResourceKey<CreativeModeTab> tabKey,
            @Local(argsOnly = true) CreativeModeTab.ItemDisplayParameters params
    ) {
        var parentEntries = new MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility>(ItemStackLinkedSet.TYPE_AND_TAG);
        var searchEntries = new MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility>(ItemStackLinkedSet.TYPE_AND_TAG);
        for (Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry : instance) {
            if (PortBuildCreativeModeTabContentsEvent.isParentTab(entry.getValue())) {
                parentEntries.put(entry.getKey(), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            }
            if (PortBuildCreativeModeTabContentsEvent.isSearchTab(entry.getValue())) {
                searchEntries.put(entry.getKey(), CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
            }
        }
        PortEventHandler.postEvent(new PortBuildCreativeModeTabContentsEvent(tab, tabKey, params, parentEntries, searchEntries));
        return Iterators.concat(parentEntries.iterator(), searchEntries.iterator());
    }
}
