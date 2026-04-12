package org.mesdag.portlib.wrapper.common;

import com.google.common.collect.Collections2;
import net.neoforged.neoforge.common.ItemAbility;
import org.mesdag.portlib.diff.Diff;

import java.util.Collection;

public class PortItemAbility {
    private final ItemAbility delegate;

    private PortItemAbility(ItemAbility delegate) {
        this.delegate = delegate;
    }

    public static Collection<PortItemAbility> getActions() {
        return Collections2.transform(ItemAbility.getActions(), PortItemAbility::wrap);
    }

    public static PortItemAbility get(String name) {
        return wrap(ItemAbility.get(name));
    }

    @Diff
    public ItemAbility unwrap() {
        return delegate;
    }

    @Diff
    public static PortItemAbility wrap(ItemAbility delegate) {
        return new PortItemAbility(delegate);
    }

    public String name() {
        return delegate.name();
    }
}
