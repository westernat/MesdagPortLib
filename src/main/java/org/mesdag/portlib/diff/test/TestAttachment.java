package org.mesdag.portlib.diff.test;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.IPortNBTSerializable;

@Diff
public class TestAttachment implements IPortNBTSerializable<CompoundTag> {
    public static final PortStreamCodec<ByteBuf, TestAttachment> STREAM_CODEC = PortByteBufCodecs.BOOL.map(TestAttachment::new, TestAttachment::z);

    private boolean z;
    private byte b;
    private short s;
    private int i;
    private long l;
    private float f;
    private double d;
    private String str = "";
    private ItemStack stack = ItemStack.EMPTY;

    public TestAttachment(boolean z) {
        this.z = z;
    }

    public boolean z() {
        return z;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("z", z);
        nbt.putByte("b", b);
        nbt.putShort("s", s);
        nbt.putInt("i", i);
        nbt.putLong("l", l);
        nbt.putFloat("f", f);
        nbt.putDouble("d", d);
        nbt.putString("str", str);
        RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        ItemStack.CODEC.encodeStart(ops, stack).result().ifPresent(tag -> nbt.put("stack", tag));
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        this.z = nbt.getBoolean("z");
        this.b = nbt.getByte("b");
        this.s = nbt.getShort("s");
        this.i = nbt.getInt("i");
        this.l = nbt.getLong("l");
        this.f = nbt.getFloat("f");
        this.d = nbt.getDouble("d");
        this.str = nbt.getString("str");
        RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        ItemStack.CODEC.parse(ops, nbt.get("stack")).result().ifPresent(result -> this.stack = result);
    }
}
