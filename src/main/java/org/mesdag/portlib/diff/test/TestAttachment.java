package org.mesdag.portlib.diff.test;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.attachment.PortAttachmentHolder;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.registries.PortAttachmentRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.wrapper.IPortNBTSerializable;
import org.mesdag.portlib.wrapper.core.PortRegistryAccess;

import java.util.function.Supplier;

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

    @Override
    public CompoundTag serializeNBT(PortRegistryAccess provider) {
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
    public void deserializeNBT(PortRegistryAccess provider, CompoundTag nbt) {
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

    public static void test() {
        PortAttachmentRegistration attachment = PortRegisterHandler.attachment(PortLib.MODID);
        Supplier<PortAttachmentType<TestAttachment>> test = attachment.registerTyped("test", () -> PortAttachmentType.serializable(() -> new TestAttachment(true)).sync(STREAM_CODEC).copyOnDeath().build());

        PortEventHandler.addListener((PlayerInteractEvent.EntityInteract event) -> {
            if (!event.getItemStack().isEmpty()) {
                if (!event.getLevel().isClientSide) {
                    PortAttachmentHolder holder = PortAttachmentHolder.wrap(event.getTarget());
                    TestAttachment data = holder.getData(test);
                    data.stack = event.getItemStack();
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        });
    }
}
