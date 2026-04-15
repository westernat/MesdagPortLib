package org.mesdag.portlib.wrapper.common.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public final class PortIngredientType<T extends PortCustomIngredient> {
    private final IngredientType<T> delegate;

    public PortIngredientType(MapCodec<T> codec, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
        this.delegate = new IngredientType<>(codec, (StreamCodec<? super RegistryFriendlyByteBuf, T>) streamCodec.unwrap());
    }

    private PortIngredientType(IngredientType<T> delegate) {
        this.delegate = delegate;
    }

    public PortIngredientType(MapCodec<T> codec) {
        this(codec, PortByteBufCodecs.fromCodecWithRegistries(codec.codec()));
    }

    public MapCodec<T> codec() {
        return delegate.codec();
    }

    public PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec() {
        return (PortStreamCodec<? super PortRegistryFriendlyByteBuf, T>) delegate.streamCodec();
    }

    @Diff
    public IngredientType<T> unwrap() {
        return delegate;
    }

    @Diff
    public static <T extends PortCustomIngredient> PortIngredientType<T> wrap(IngredientType<T> type) {
        return new PortIngredientType<>(type);
    }
}
