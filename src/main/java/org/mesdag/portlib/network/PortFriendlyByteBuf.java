package org.mesdag.portlib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PortFriendlyByteBuf {
    public static <T> ResourceKey<? extends Registry<T>> readRegistryKey(FriendlyByteBuf buf) {
        return buf.readRegistryKey();
    }

    public static @Nullable Tag readNbt(ByteBuf buffer, NbtAccounter nbtAccounter) {
        return FriendlyByteBuf.readNbt(buffer, nbtAccounter);
    }

    public static void writeNbt(ByteBuf buffer, @javax.annotation.Nullable Tag nbt) {
        FriendlyByteBuf.writeNbt(buffer, nbt);
    }

    public static byte[] readByteArray(ByteBuf buffer) {
        return FriendlyByteBuf.readByteArray(buffer);
    }

    public static void writeByteArray(ByteBuf buffer, byte[] array) {
        FriendlyByteBuf.writeByteArray(buffer, array);
    }

    public static byte[] readByteArray(ByteBuf buffer, int maxSize) {
        return FriendlyByteBuf.readByteArray(buffer, maxSize);
    }

    public static Vector3f readVector3f(ByteBuf buffer) {
        return FriendlyByteBuf.readVector3f(buffer);
    }

    public static void writeVector3f(ByteBuf buffer, Vector3f vector3f) {
        FriendlyByteBuf.writeVector3f(buffer, vector3f);
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
