package org.mesdag.portlib.wrapper.common;

import com.google.common.collect.Collections2;
import net.minecraftforge.common.ToolAction;
import org.mesdag.portlib.diff.Diff;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PortItemAbility {
    private static final Map<ToolAction, PortItemAbility> wrapper = new ConcurrentHashMap<>();

    private final ToolAction delegate;

    private PortItemAbility(ToolAction delegate) {
        this.delegate = delegate;
    }

    public static Collection<PortItemAbility> getActions() {
        return Collections2.transform(ToolAction.getActions(), PortItemAbility::wrap);
    }

    public static PortItemAbility get(String name) {
        return wrap(ToolAction.get(name));
    }

    @Diff
    public ToolAction unwrap() {
        return delegate;
    }

    @Diff
    public static PortItemAbility wrap(ToolAction delegate) {
        return wrapper.computeIfAbsent(delegate, PortItemAbility::new);
    }

    public String name() {
        return delegate.name();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof PortItemAbility ability && ability.delegate == delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
