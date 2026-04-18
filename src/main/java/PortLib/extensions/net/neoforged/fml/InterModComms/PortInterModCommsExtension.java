package PortLib.extensions.net.neoforged.fml.InterModComms;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.fml.InterModComms;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.fml.PortInterModComms;

@Extension
public class PortInterModCommsExtension {
    public static class IMCMessage {
        @Diff
        public static PortInterModComms.PortIMCMessage wrap(@This InterModComms.IMCMessage thiz) {
            return new PortInterModComms.PortIMCMessage(thiz.senderModId(), thiz.modId(), thiz.method(), thiz.messageSupplier());
        }
    }
}
