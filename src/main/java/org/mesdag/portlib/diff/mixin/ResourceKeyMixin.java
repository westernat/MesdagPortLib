package org.mesdag.portlib.diff.mixin;

import net.minecraft.resources.ResourceKey;
import org.mesdag.portlib.wrapper.common.extensions.IPortResourceKeyExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ResourceKey.class)
public abstract class ResourceKeyMixin<T> implements IPortResourceKeyExtension<T> {
}
