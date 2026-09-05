package org.mesdag.portlib.diff.mixin;

import net.minecraft.world.entity.projectile.ProjectileUtil;
import org.mesdag.portlib.wrapper.common.extensions.IPortProjectileUtilExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ProjectileUtil.class)
public abstract class ProjectileUtilMixin implements IPortProjectileUtilExtension {
}
