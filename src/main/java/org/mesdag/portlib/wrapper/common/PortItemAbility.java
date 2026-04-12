package org.mesdag.portlib.wrapper.common;

import com.google.common.collect.Collections2;
import net.minecraftforge.common.ToolAction;
import org.mesdag.portlib.diff.Diff;

import java.util.Collection;

public class PortItemAbility {
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
        return new PortItemAbility(delegate);
    }

    public String name() {
        return delegate.name();
    }
}
