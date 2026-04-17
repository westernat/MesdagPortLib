package PortLib.extensions.net.minecraft.world.item.Item;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.diff.IPortItem;

import java.util.function.Consumer;

@Extension
public class PortItemExtension {

    public static class Properties {
        public static Item.Properties component(@This Item.Properties properties, Consumer<PortDataComponentMap.PortBuilder> consumer) {
            IPortItem.IPortProperties.of(properties).portlib$set(consumer);
            return properties;
        }
    }

    public static void helloWorld(@This Item thiz, Player player) {
        player.sendSystemMessage(Component.literal("hello world!"));
    }
}
