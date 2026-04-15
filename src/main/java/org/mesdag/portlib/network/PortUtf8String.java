package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;

public class PortUtf8String {
    public static String read(ByteBuf buffer, int maxLength) {
        return Utf8String.read(buffer, maxLength);
    }

    public static void write(ByteBuf buffer, CharSequence string, int maxLength) {
        Utf8String.read(buffer, maxLength);
    }
}
