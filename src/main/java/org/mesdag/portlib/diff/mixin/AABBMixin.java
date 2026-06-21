package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.phys.AABB;
import org.mesdag.portlib.wrapper.common.extensions.IPortAABBExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AABB.class)
public abstract class AABBMixin implements IPortAABBExtension {
}
