package org.mesdag.portlib.diff;

import net.minecraft.world.effect.MobEffect;

public interface IPortMobEffect extends IPortClientExtensionsSetter {
    static IPortMobEffect of(MobEffect effect) {
        return (IPortMobEffect) effect;
    }
}
