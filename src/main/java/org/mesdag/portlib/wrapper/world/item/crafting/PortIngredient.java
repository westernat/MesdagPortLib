package org.mesdag.portlib.wrapper.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.List;

public class PortIngredient {
    public static final Codec<Ingredient> CODEC = Ingredient.CODEC;
    public static final Codec<Ingredient> CODEC_NONEMPTY = Ingredient.CODEC_NONEMPTY;
    public static final MapCodec<Ingredient> MAP_CODEC_NONEMPTY = Ingredient.MAP_CODEC_NONEMPTY;
    public static final Codec<List<Ingredient>> LIST_CODEC = Ingredient.LIST_CODEC;
    public static final Codec<List<Ingredient>> LIST_CODEC_NONEMPTY = Ingredient.LIST_CODEC_NONEMPTY;

    public static final PortStreamCodec<RegistryFriendlyByteBuf, Ingredient> CONTENTS_STREAM_CODEC = PortStreamCodec.wrap(Ingredient.CONTENTS_STREAM_CODEC);
}
