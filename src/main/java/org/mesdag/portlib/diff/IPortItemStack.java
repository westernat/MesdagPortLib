package org.mesdag.portlib.diff;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.component.PortPatchedDataComponentMap;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.world.item.component.PortTool;

@Diff
public interface IPortItemStack extends PortSelfGetter<ItemStack> {
    String DATA_COMPONENTS = "portlib:data_components";

    @Nullable FoodProperties portlib$getFood(@Nullable LivingEntity living);

    void portlib$setFood(@Nullable FoodProperties food, boolean encode);

    @Nullable PortTool portlib$getTool();

    void portlib$setTool(@Nullable PortTool tool, boolean encode);

    PortPatchedDataComponentMap portlib$patch();

    default void updateTag() {
        portlib$self().getOrCreateTag().put(DATA_COMPONENTS, portlib$patch().serializeNBT(PortEnvironment.registryAccess()));
    }

    static IPortItemStack of(ItemStack stack) {
        return (IPortItemStack) (Object) stack;
    }
}
