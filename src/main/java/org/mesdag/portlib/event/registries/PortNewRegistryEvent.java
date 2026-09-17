/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package org.mesdag.portlib.event.registries;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.PortCustomRegistration;

import java.util.function.Supplier;

public class PortNewRegistryEvent extends Event implements IModBusEvent {
    private final NewRegistryEvent e;

    @Diff
    public PortNewRegistryEvent(NewRegistryEvent e) {
        this.e = e;
    }

    public <T> Supplier<IForgeRegistry<T>> create(RegistryBuilder<T> builder) {
        return e.create(builder);
    }

    public <T> void register(PortCustomRegistration<T> registry) {
        registry.registerToRootRegistry(); // 时序已控制好
    }
}
