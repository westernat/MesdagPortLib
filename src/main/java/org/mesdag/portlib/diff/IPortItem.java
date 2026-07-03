package org.mesdag.portlib.diff;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.wrapper.common.extensions.IPortItemExtension;

import java.util.Map;

@Diff
public interface IPortItem extends PortDataComponentMap, IPortClientExtensionsSetter, IPortItemExtension {
    Map<PortDataComponentType<?>, Object> portlib$getComponents();

    void portlib$setComponents(Map<PortDataComponentType<?>, Object> map);

    @Nullable CompoundTag portlib$defaultTag();

    static IPortItem of(Item item) {
        return (IPortItem) item;
    }

    interface IPortProperties {
        void portlib$set(Builder consumer);

        @Nullable PortDataComponentMap.Builder portlib$getBuilder();

        static IPortProperties of(Item.Properties properties) {
            return (IPortProperties) properties;
        }
    }
}
