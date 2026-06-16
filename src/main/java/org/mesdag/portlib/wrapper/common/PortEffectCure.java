package org.mesdag.portlib.wrapper.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record PortEffectCure(String name) {
    private static final Map<String, PortEffectCure> CURES = new ConcurrentHashMap<>();

    public static Collection<PortEffectCure> getAllCures() {
        return Collections.unmodifiableCollection(CURES.values());
    }

    public static PortEffectCure get(String name) {
        return CURES.computeIfAbsent(name, PortEffectCure::new);
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof PortEffectCure cure && cure.name.equals(name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
