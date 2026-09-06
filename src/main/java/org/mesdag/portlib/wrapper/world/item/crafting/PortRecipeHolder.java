package org.mesdag.portlib.wrapper.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.common.extensions.IPortRecipeExtension;
import org.mesdag.portlib.wrapper.common.extensions.IPortResourceLocationExtension;

public record PortRecipeHolder<T extends Recipe<?>>(ResourceLocation id, T value) {
    public static final PortStreamCodec<PortRegistryFriendlyByteBuf, PortRecipeHolder<?>> STREAM_CODEC = PortStreamCodec.composite(
            IPortResourceLocationExtension.STREAM_CODEC, PortRecipeHolder::id,
            IPortRecipeExtension.STREAM_CODEC, PortRecipeHolder::value,
            PortRecipeHolder::new
    );

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof PortRecipeHolder<?> holder && holder.id.equals(id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public @NotNull String toString() {
        return id.toString();
    }
}
