package PortLib.extensions.net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.registries.PortDataMapsUpdatedEvent;

@Extension
public class PortDataMapsUpdatedEventExtension {
    public static class UpdateCause {
        @Diff
        public static PortDataMapsUpdatedEvent.PortUpdateCause wrap(@This DataMapsUpdatedEvent.UpdateCause thiz) {
            return thiz == DataMapsUpdatedEvent.UpdateCause.CLIENT_SYNC
                    ? PortDataMapsUpdatedEvent.PortUpdateCause.CLIENT_SYNC
                    : PortDataMapsUpdatedEvent.PortUpdateCause.SERVER_RELOAD;
        }
    }
}
