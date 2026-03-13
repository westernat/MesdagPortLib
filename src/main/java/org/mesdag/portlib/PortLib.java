package org.mesdag.portlib;

import net.minecraftforge.fml.common.Mod;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.PortAttachmentSync;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.diff.PortSyncAttachmentsPayload;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
@Mod(PortLib.MODID)
public class PortLib {
    public static final String MODID = "portlib";
    public static final Logger LOGGER = LoggerFactory.getLogger("PortLib");
    @Diff
    public static final PortNetworkHandler NETWORK_HANDLER = new PortNetworkHandler(MODID, "1");

    public PortLib() {
        PortRegisterHandler.init();
        PortRegistries.init();
        PortAttachmentSync.init();
        PortEventHooks.init();
        NETWORK_HANDLER.registerInGameS2C(
                PortSyncAttachmentsPayload.IDENTIFIER,
                PortSyncAttachmentsPayload.STREAM_CODEC,
                PortSyncAttachmentsPayload::handle
        );
    }

    public static PortIdentifier asResource(String path) {
        return PortIdentifier.fromNamespaceAndPath(MODID, path);
    }
}
