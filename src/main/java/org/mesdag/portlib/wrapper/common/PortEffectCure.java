package org.mesdag.portlib.wrapper.common;

import com.google.common.collect.Collections2;
import net.neoforged.neoforge.common.EffectCure;
import org.mesdag.portlib.diff.Diff;

import java.util.Collection;

public class PortEffectCure {
    private final EffectCure cure;

    private PortEffectCure(EffectCure cure) {
        this.cure = cure;
    }

    public String name() {
        return cure.name();
    }

    @Diff
    public EffectCure unwrap() {
        return cure;
    }

    @Diff
    public static PortEffectCure wrap(EffectCure cure) {
        return new PortEffectCure(cure);
    }

    public static Collection<PortEffectCure> getAllCures() {
        return Collections2.transform(EffectCure.getAllCures(), PortEffectCure::wrap);
    }

    public static PortEffectCure get(String name) {
        return PortEffectCure.wrap(EffectCure.get(name));
    }
}
