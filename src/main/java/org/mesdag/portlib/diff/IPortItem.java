package org.mesdag.portlib.diff;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.component.PortDataComponentMap;

import java.util.function.Consumer;

public interface IPortItem extends PortDataComponentMap {
    static IPortItem of(Item item) {
        return (IPortItem) item;
    }

    interface IPortProperties {
        void portlib$set(Consumer<PortBuilder> consumer);

        @Nullable Consumer<PortBuilder> portlib$get();

        static IPortProperties of(Item.Properties properties) {
            return (IPortProperties) properties;
        }
    }
}
