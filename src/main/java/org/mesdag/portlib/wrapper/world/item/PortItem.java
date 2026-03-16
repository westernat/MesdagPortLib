package org.mesdag.portlib.wrapper.world.item;

import net.minecraft.world.item.Item;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.diff.IPortItem;

import java.util.function.Consumer;

public class PortItem {
    public static class PortProperties {
        public static Item.Properties component(Item.Properties properties, Consumer<PortDataComponentMap.PortBuilder> consumer) {
            IPortItem.IPortProperties.of(properties).portlib$set(consumer);
            return properties;
        }
    }
}
