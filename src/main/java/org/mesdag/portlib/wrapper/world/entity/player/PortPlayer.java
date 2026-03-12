package org.mesdag.portlib.wrapper.world.entity.player;

import net.minecraft.world.entity.player.Player;

public class PortPlayer {
    public static boolean hasInfiniteMaterials(Player player) {
        return player.getAbilities().instabuild;
    }
}
