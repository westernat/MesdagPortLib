package org.mesdag.portlib.diff.mixin;

import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.wrapper.common.extensions.IPortFriendlyByteBufExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FriendlyByteBuf.class)
public abstract class FriendlyByteBufMixin implements IPortFriendlyByteBufExtension {
}
