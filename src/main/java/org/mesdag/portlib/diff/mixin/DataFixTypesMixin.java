package org.mesdag.portlib.diff.mixin;

import net.minecraft.util.datafix.DataFixTypes;
import org.mesdag.portlib.wrapper.common.extensions.IPortDataFixTypesExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DataFixTypes.class)
public abstract class DataFixTypesMixin implements IPortDataFixTypesExtension {
}
