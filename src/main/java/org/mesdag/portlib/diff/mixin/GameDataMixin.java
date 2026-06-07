package org.mesdag.portlib.diff.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.GameData;
import org.mesdag.portlib.diff.PortRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Mixin(value = GameData.class, remap = false)
public abstract class GameDataMixin {
    @ModifyVariable(method = "postRegisterEvents", at = @At(value = "INVOKE", target = "Ljava/util/Set;addAll(Ljava/util/Collection;)Z", ordinal = 1, shift = At.Shift.AFTER), name = "ordered")
    private static Set<ResourceLocation> reorderRegistries(Set<ResourceLocation> orderedKeys) {
        ResourceLocation dataComponents = PortRegistries.Keys.DATA_COMPONENTS.location();
        ResourceLocation items = Registries.ITEM.location();
        if (!orderedKeys.contains(dataComponents) || !orderedKeys.contains(items)) {
            return orderedKeys;
        }
        List<ResourceLocation> list = new ArrayList<>(orderedKeys);
        int dcIdx = list.indexOf(dataComponents);
        int itemIdx = list.indexOf(items);
        if (itemIdx >= 0 && dcIdx > itemIdx) {
            list.remove(dcIdx);
            list.add(itemIdx, dataComponents);
            return new LinkedHashSet<>(list);
        }
        return orderedKeys;
    }
}
