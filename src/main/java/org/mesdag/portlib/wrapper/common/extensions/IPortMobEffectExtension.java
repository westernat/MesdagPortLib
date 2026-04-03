package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.util.PortSets;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.PortEffectCure;

import java.util.Set;

public interface IPortMobEffectExtension extends PortSelfGetter<MobEffect> {
    default void fillEffectCures(Set<PortEffectCure> cures, MobEffectInstance effectInstance) {
        portlib$self().fillEffectCures(PortSets.immutableTransform(cures, PortEffectCure::unwrap), effectInstance);
    }

    static IPortMobEffectExtension of(MobEffect effect) {
        return effect instanceof IPortMobEffectExtension
                ? (IPortMobEffectExtension) effect
                : new Delegate(effect);
    }

    @Diff
    record Delegate(MobEffect effect) implements IPortMobEffectExtension {
        @Override
        public MobEffect portlib$self() {
            return effect;
        }
    }
}
