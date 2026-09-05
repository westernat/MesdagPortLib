package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.crafting.Recipe;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public interface IPortRecipeExtension {
    PortStreamCodec<PortRegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public Recipe<?> decode(PortRegistryFriendlyByteBuf buffer) {
            return ClientboundUpdateRecipesPacket.fromNetwork(buffer);
        }

        @Override
        public void encode(PortRegistryFriendlyByteBuf buffer, Recipe<?> value) {
            ClientboundUpdateRecipesPacket.toNetwork(buffer, value);
        }
    };

}
