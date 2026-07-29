package org.mesdag.portlib.wrapper.nbt;

import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagTypes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PortNbtIo {
    public static Tag readAnyTag(DataInput input, NbtAccounter accounter) throws IOException {
        byte b0 = input.readByte();
        return b0 == 0 ? EndTag.INSTANCE : readTagSafe(input, accounter, b0);
    }

    private static Tag readTagSafe(DataInput input, NbtAccounter accounter, byte type) {
        try {
            // load 的第二个参数表示当前嵌套深度，不是允许的最大深度；根标签必须从零开始计数。
            return TagTypes.getType(type).load(input, 0, accounter);
        } catch (IOException ioexception) {
            CrashReport crashreport = CrashReport.forThrowable(ioexception, "Loading NBT data");
            CrashReportCategory crashreportcategory = crashreport.addCategory("NBT Tag");
            crashreportcategory.setDetail("Tag type", type);
            throw new ReportedException(crashreport);
        }
    }

    public static void writeAnyTag(Tag tag, DataOutput output) throws IOException {
        output.writeByte(tag.getId());
        if (tag.getId() != 0) {
            tag.write(output);
        }
    }
}
