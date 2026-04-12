package org.mesdag.portlib.event.other;

import net.minecraftforge.event.TagsUpdatedEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

public class PortTagsUpdatedEvent extends PortEvent<TagsUpdatedEvent> {
    @Diff
    public PortTagsUpdatedEvent(TagsUpdatedEvent e) {
        super(e);
    }

    public PortRegistryAccess getRegistryAccess() {
        return new PortRegistryAccess(e.getRegistryAccess());
    }

    public PortUpdateCause getUpdateCause() {
        return PortUpdateCause.wrap(e.getUpdateCause());
    }

    public boolean shouldUpdateStaticData() {
        return e.shouldUpdateStaticData();
    }

    public enum PortUpdateCause {
        SERVER_DATA_LOAD,
        CLIENT_PACKET_RECEIVED;

        @Diff
        public TagsUpdatedEvent.UpdateCause unwrap() {
            return this == SERVER_DATA_LOAD ? TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD : TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED;
        }

        @Diff
        public static PortUpdateCause wrap(TagsUpdatedEvent.UpdateCause cause) {
            return cause == TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD ? SERVER_DATA_LOAD : CLIENT_PACKET_RECEIVED;
        }
    }

    static {
        PortEventHooks.register(TagsUpdatedEvent.class, PortTagsUpdatedEvent.class, PortTagsUpdatedEvent::new);
    }
}
