package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;

public class PortVarLong {
    public static int getByteSize(long data) {
        for (int i = 1; i < 10; i++) {
            if ((data & -1L << i * 7) == 0L) {
                return i;
            }
        }

        return 10;
    }

    public static boolean hasContinuationBit(byte data) {
        return (data & 128) == 128;
    }

    public static long read(ByteBuf buffer) {
        long i = 0L;
        int j = 0;

        byte b0;
        do {
            b0 = buffer.readByte();
            i |= (long)(b0 & 127) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while (hasContinuationBit(b0));

        return i;
    }

    public static ByteBuf write(ByteBuf buffer, long value) {
        while ((value & -128L) != 0L) {
            buffer.writeByte((int)(value & 127L) | 128);
            value >>>= 7;
        }

        buffer.writeByte((int)value);
        return buffer;
    }
}
