package PortLib.extensions.net.minecraft.world.item.crafting.Ingredient;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import manifold.ext.rt.api.Extension;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.List;

@Extension
public class PortIngredientExtension {
    private static final PortStreamCodec<RegistryFriendlyByteBuf, Ingredient> CONTENTS_STREAM_CODEC = PortStreamCodec.wrap(Ingredient.CONTENTS_STREAM_CODEC);

    @Extension
    public static Codec<Ingredient> codec() {
        return Ingredient.CODEC;
    }

    @Extension
    public static Codec<Ingredient> codecNonempty() {
        return Ingredient.CODEC_NONEMPTY;
    }

    @Extension
    public static MapCodec<Ingredient> mapCodecNonempty() {
        return Ingredient.MAP_CODEC_NONEMPTY;
    }

    @Extension
    public static Codec<List<Ingredient>> listCodec() {
        return Ingredient.LIST_CODEC;
    }

    @Extension
    public static Codec<List<Ingredient>> listCodecNonempty() {
        return Ingredient.LIST_CODEC_NONEMPTY;
    }

    @SuppressWarnings("unchecked")
    @Extension
    public static PortStreamCodec<PortRegistryFriendlyByteBuf, Ingredient> contentsStreamCodec() {
        return (PortStreamCodec<PortRegistryFriendlyByteBuf, Ingredient>) (PortStreamCodec<?, ?>) CONTENTS_STREAM_CODEC;
    }
}
