package org.mesdag.portlib.event.other;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

public class PortAddPackFindersEvent extends PortEvent<AddPackFindersEvent> implements IPortModBusEvent {
    @Diff
    public PortAddPackFindersEvent(AddPackFindersEvent e) {
        super(e);
    }

    public void addRepositorySource(RepositorySource source) {
        e.addRepositorySource(source);
    }

    public PackType getPackType() {
        return e.getPackType();
    }

    public void addPackFinders(PortIdentifier packLocation, PackType packType, Component packNameDisplay, PackSource packSource, boolean alwaysActive, Pack.Position packPosition) {
        e.addPackFinders(packLocation, packType, packNameDisplay, packSource, alwaysActive, packPosition);
    }

    public boolean isTrusted() {
        return e.isTrusted();
    }

    static {
        PortEventHooks.register(AddPackFindersEvent.class, PortAddPackFindersEvent.class, PortAddPackFindersEvent::new);
    }
}
