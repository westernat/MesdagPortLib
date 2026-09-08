package org.mesdag.portlib.diff;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemExtension;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemStackExtension;
import org.mesdag.portlib.wrapper.common.util.PortTriState;
import org.mesdag.portlib.wrapper.world.item.component.PortTool;

import java.util.Map;

@Diff
public interface IPortItem extends PortDataComponentMap, IPortClientExtensionsSetter, IPortItemExtension {
    Map<PortDataComponentType<?>, Object> portlib$getComponents();

    void portlib$setComponents(Map<PortDataComponentType<?>, Object> map);

    @Nullable CompoundTag portlib$defaultTag();

    static IPortItem of(Item item) {
        return (IPortItem) item;
    }

    // invoked by coremod
    static PortTriState isCorrectToolForDrops(ItemStack stack, BlockState state) {
        PortTool tool = IPortItemStackExtension.of(stack).getTool();
        return tool == null ? PortTriState.DEFAULT : (tool.isCorrectForDrops(state) ? PortTriState.TRUE : PortTriState.FALSE);
    }

    interface IPortProperties {
        void portlib$set(Builder consumer);

        @Nullable PortDataComponentMap.Builder portlib$getBuilder();

        static IPortProperties of(Item.Properties properties) {
            return (IPortProperties) properties;
        }
    }
}
