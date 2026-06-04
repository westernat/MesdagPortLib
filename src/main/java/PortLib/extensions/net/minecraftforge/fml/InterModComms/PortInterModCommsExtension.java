package PortLib.extensions.net.minecraftforge.fml.InterModComms;

import net.minecraftforge.fml.InterModComms;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.fml.PortInterModComms;

public class PortInterModCommsExtension {
    public static class IMCMessage {
        @Diff
        public static PortInterModComms.PortIMCMessage wrap(InterModComms.IMCMessage thiz) {
            return new PortInterModComms.PortIMCMessage(thiz.senderModId(), thiz.modId(), thiz.method(), thiz.messageSupplier());
        }
    }
}
