package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarLong;

public class PortVarLong {
    public static int getByteSize(long data) {
        return VarLong.getByteSize(data);
    }

    public static boolean hasContinuationBit(byte data) {
        return VarLong.hasContinuationBit(data);
    }

    public static long read(ByteBuf buffer) {
        return VarLong.read(buffer);
    }

    public static ByteBuf write(ByteBuf buffer, long value) {
        return VarLong.write(buffer, value);
    }
}
