package org.mesdag.portlib.wrapper.common;

import net.neoforged.neoforge.common.EffectCures;
import org.mesdag.portlib.util.PortSets;

import java.util.Set;

public class PortEffectCures {
    public static final PortEffectCure MILK = PortEffectCure.wrap(EffectCures.MILK);
    public static final PortEffectCure HONEY = PortEffectCure.wrap(EffectCures.HONEY);
    public static final PortEffectCure PROTECTED_BY_TOTEM = PortEffectCure.wrap(EffectCures.PROTECTED_BY_TOTEM);

    public static final Set<PortEffectCure> DEFAULT_CURES = PortSets.immutableTransform(EffectCures.DEFAULT_CURES, PortEffectCure::wrap);
}
