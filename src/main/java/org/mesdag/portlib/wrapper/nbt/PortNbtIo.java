package org.mesdag.portlib.wrapper.nbt;

import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PortNbtIo {
    public static Tag readAnyTag(DataInput input, NbtAccounter accounter) throws IOException {
        return NbtIo.readAnyTag(input, accounter);
    }

    public static void writeAnyTag(Tag tag, DataOutput output) throws IOException {
        NbtIo.writeAnyTag(tag, output);
    }
}
