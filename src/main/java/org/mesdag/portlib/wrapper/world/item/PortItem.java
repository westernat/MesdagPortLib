package org.mesdag.portlib.wrapper.world.item;

import net.minecraft.world.item.Item;
import org.mesdag.portlib.component.PortDataComponentMap;

import java.util.function.Consumer;

public class PortItem {
    public static class PortProperties {
        public static Item.Properties component(Item.Properties properties, Consumer<PortDataComponentMap.PortBuilder> consumer) {
            PortDataComponentMap.PortBuilder builder = new PortDataComponentMap.PortBuilder(properties);
            consumer.accept(builder);
            return properties;
        }
    }
}
