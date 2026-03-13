package org.mesdag.portlib.wrapper.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.VarInt;

public class PortVarInt {
    public static int getByteSize(int data) {
        return VarInt.getByteSize(data);
    }

    public static boolean hasContinuationBit(byte data) {
        return VarInt.hasContinuationBit(data);
    }

    public static int read(ByteBuf buffer) {
        return VarInt.read(buffer);
    }

    public static ByteBuf write(ByteBuf buffer, int value) {
        return VarInt.write(buffer, value);
    }
}
