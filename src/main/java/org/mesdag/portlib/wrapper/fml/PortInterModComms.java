package org.mesdag.portlib.wrapper.fml;

import net.minecraftforge.fml.InterModComms;
import org.mesdag.portlib.diff.Diff;

import java.util.function.Supplier;

public class PortInterModComms {
    public record PortIMCMessage(String senderModId, String modId, String method,
                                 Supplier<?> messageSupplier) {
        @Diff
        public InterModComms.IMCMessage unwrap() {
            return new InterModComms.IMCMessage(senderModId, modId, method, messageSupplier);
        }
    }
}
