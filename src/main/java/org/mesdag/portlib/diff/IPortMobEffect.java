package org.mesdag.portlib.diff;

import net.minecraft.world.effect.MobEffect;
import org.mesdag.portlib.wrapper.common.extensions.IPortMobEffectExtension;

public interface IPortMobEffect extends IPortClientExtensionsSetter, IPortMobEffectExtension {
    static IPortMobEffect of(MobEffect effect) {
        return (IPortMobEffect) effect;
    }
}
