package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.ai.behavior.WorkAtComposter;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

/// 允许 PortLib 在 Data Map 重载后刷新农民村民可投入堆肥桶的物品列表。
@Mixin(WorkAtComposter.class)
public interface WorkAtComposterAccessor {
    @Accessor("COMPOSTABLE_ITEMS")
    @Mutable
    static void setCompostableItems(List<Item> items) {
        throw new UnsupportedOperationException();
    }

    @Accessor("COMPOSTABLE_ITEMS")
    static List<Item> getCompostableItems() {
        throw new UnsupportedOperationException();
    }
}
