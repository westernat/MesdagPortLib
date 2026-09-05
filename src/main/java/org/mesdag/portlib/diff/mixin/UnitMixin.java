package org.mesdag.portlib.diff.mixin;

import net.minecraft.util.Unit;
import org.mesdag.portlib.wrapper.common.extensions.IPortUnitExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Unit.class)
public abstract class UnitMixin implements IPortUnitExtension {
}
