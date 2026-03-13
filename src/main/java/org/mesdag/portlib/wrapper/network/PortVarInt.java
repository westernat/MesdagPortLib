package org.mesdag.portlib.wrapper.network;

import io.netty.buffer.ByteBuf;

public class PortVarInt {
    public static int getByteSize(int data) {
        for (int i = 1; i < 5; i++) {
            if ((data & -1 << i * 7) == 0) {
                return i;
            }
        }

        return 5;
    }

    public static boolean hasContinuationBit(byte data) {
        return (data & 128) == 128;
    }

    public static int read(ByteBuf buffer) {
        int i = 0;
        int j = 0;

        byte b0;
        do {
            b0 = buffer.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while (hasContinuationBit(b0));

        return i;
    }

    public static ByteBuf write(ByteBuf buffer, int value) {
        while ((value & -128) != 0) {
            buffer.writeByte(value & 127 | 128);
            value >>>= 7;
        }

        buffer.writeByte(value);
        return buffer;
    }
}
