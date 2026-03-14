package org.mesdag.portlib;

import net.minecraftforge.fml.common.Mod;
import org.mesdag.portlib.diff.*;
import org.mesdag.portlib.diff.test.TestAttachment;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.wrapper.PortEnvironment;
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
        PortAttachmentInternals.init();
        if (PortEnvironment.isDeveloper()) {
            TestAttachment.test();
        }
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
