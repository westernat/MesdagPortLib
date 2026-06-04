package org.mesdag.portlib.diff;

import net.minecraft.world.effect.MobEffect;
import org.mesdag.portlib.diff.mixin.IPortClientExtensionsSetter;

public interface IPortMobEffect extends IPortClientExtensionsSetter {
    static IPortMobEffect of(MobEffect effect) {
        return (IPortMobEffect) effect;
    }
}
