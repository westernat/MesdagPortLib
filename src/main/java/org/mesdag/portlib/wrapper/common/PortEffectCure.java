package org.mesdag.portlib.wrapper.common;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PortEffectCure {
    private static final Map<String, PortEffectCure> CURES = new ConcurrentHashMap<>();

    private final String name;

    private PortEffectCure(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static Collection<PortEffectCure> getAllCures() {
        return Collections.unmodifiableCollection(CURES.values());
    }

    public static PortEffectCure get(String name) {
        return CURES.computeIfAbsent(name, PortEffectCure::new);
    }
}
