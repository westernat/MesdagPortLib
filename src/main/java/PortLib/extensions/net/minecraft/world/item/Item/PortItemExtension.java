package PortLib.extensions.net.minecraft.world.item.Item;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

@Extension
public class PortItemExtension {
    public static void helloWorld(@This Item thiz, Player player) {
        player.sendSystemMessage(Component.literal("hello world!"));
    }
}
