package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.mesdag.portlib.wrapper.nbt.PortNbtIo;

import java.io.IOException;

public class PortFriendlyByteBuf {
    public static <T> ResourceKey<? extends Registry<T>> readRegistryKey(FriendlyByteBuf buf) {
        ResourceLocation resourcelocation = buf.readResourceLocation();
        return ResourceKey.createRegistryKey(resourcelocation);
    }

    public static @Nullable Tag readNbt(ByteBuf buffer, NbtAccounter nbtAccounter) {
        try {
            Tag tag = PortNbtIo.readAnyTag(new ByteBufInputStream(buffer), nbtAccounter);
            return tag.getId() == 0 ? null : tag;
        } catch (IOException ioexception) {
            throw new EncoderException(ioexception);
        }
    }

    public static @Nullable CompoundTag readNbt(ByteBuf buffer) {
        Tag tag = readNbt(buffer, new NbtAccounter(2097152L));
        if (tag != null && !(tag instanceof CompoundTag)) {
            throw new DecoderException("Not a compound tag: " + tag);
        }
        return (CompoundTag) tag;
    }

    public static void writeNbt(ByteBuf buffer, @javax.annotation.Nullable Tag nbt) {
        if (nbt == null) {
            nbt = EndTag.INSTANCE;
        }

        try {
            PortNbtIo.writeAnyTag(nbt, new ByteBufOutputStream(buffer));
        } catch (IOException ioexception) {
            throw new EncoderException(ioexception);
        }
    }

    public static byte[] readByteArray(ByteBuf buffer) {
        return readByteArray(buffer, buffer.readableBytes());
    }

    public static void writeByteArray(ByteBuf buffer, byte[] array) {
        PortVarInt.write(buffer, array.length);
        buffer.writeBytes(array);
    }

    public static byte[] readByteArray(ByteBuf buffer, int maxSize) {
        int i = PortVarInt.read(buffer);
        if (i > maxSize) {
            throw new DecoderException("ByteArray with size " + i + " is bigger than allowed " + maxSize);
        }
        byte[] abyte = new byte[i];
        buffer.readBytes(abyte);
        return abyte;
    }

    public static Vector3f readVector3f(ByteBuf buffer) {
        return new Vector3f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    public static void writeVector3f(ByteBuf buffer, Vector3f vector3f) {
        buffer.writeFloat(vector3f.x());
        buffer.writeFloat(vector3f.y());
        buffer.writeFloat(vector3f.z());
    }

    public static Vector4f readVector4f(ByteBuf buffer) {
        return new Vector4f(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    public static void writeVector4f(ByteBuf buffer, Vector4f vector4f) {
        buffer.writeFloat(vector4f.x());
        buffer.writeFloat(vector4f.y());
        buffer.writeFloat(vector4f.z());
        buffer.writeFloat(vector4f.w());
    }
}
