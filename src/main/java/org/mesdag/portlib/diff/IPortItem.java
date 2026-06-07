package org.mesdag.portlib.diff;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentType;

import java.util.Map;

@Diff
public interface IPortItem extends PortDataComponentMap, IPortClientExtensionsSetter {
    Map<PortDataComponentType<?>, Object> portlib$getComponents();

    void portlib$setComponents(Map<PortDataComponentType<?>, Object> map);

    static IPortItem of(Item item) {
        return (IPortItem) item;
    }

    interface IPortProperties {
        void portlib$set(PortBuilder consumer);

        @Nullable PortBuilder portlib$get();

        static IPortProperties of(Item.Properties properties) {
            return (IPortProperties) properties;
        }
    }
}
