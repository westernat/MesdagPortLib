package org.mesdag.portlib.wrapper.common;

import java.util.Set;

public class PortEffectCures {
    public static final PortEffectCure MILK = PortEffectCure.get("milk");
    public static final PortEffectCure HONEY = PortEffectCure.get("honey");
    public static final PortEffectCure PROTECTED_BY_TOTEM = PortEffectCure.get("protected_by_totem");

    public static final Set<PortEffectCure> DEFAULT_CURES = Set.of(MILK, PROTECTED_BY_TOTEM);
}
