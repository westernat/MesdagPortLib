package org.mesdag.portlib.wrapper.common.crafting;

import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
import PortLib.extensions.net.minecraft.network.FriendlyByteBuf.PortFriendlyByteBufExtension;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public final class PortIngredientType<T extends PortCustomIngredient> {
    private final MapCodec<T> codec;
    private final PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec;
    private final IIngredientSerializer<T> serializer;

    public PortIngredientType(MapCodec<T> codec, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
        this.codec = codec;
        this.streamCodec = streamCodec;
        this.serializer = new IIngredientSerializer<>() {
            @Override
            public T parse(FriendlyByteBuf buffer) {
                return streamCodec.decode(PortFriendlyByteBufExtension.wrap(buffer));
            }

            @Override
            public T parse(JsonObject json) {
                return PortDataResultExtension.getOrThrow(codec.compressedDecode(JsonOps.INSTANCE, json));
            }

            @Override
            public void write(FriendlyByteBuf buffer, T ingredient) {
                streamCodec.encode(PortFriendlyByteBufExtension.wrap(buffer), ingredient);
            }
        };
    }

    public PortIngredientType(MapCodec<T> codec) {
        this(codec, PortByteBufCodecs.fromCodecWithRegistries(codec.codec()));
    }

    public MapCodec<T> codec() {
        return codec;
    }

    public PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }

    @Diff
    public IIngredientSerializer<T> serializer() {
        return serializer;
    }
}
