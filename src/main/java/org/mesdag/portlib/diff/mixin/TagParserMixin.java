package org.mesdag.portlib.diff.mixin;

import net.minecraft.nbt.TagParser;
import org.mesdag.portlib.wrapper.common.extensions.IPortTagParserExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TagParser.class)
public abstract class TagParserMixin implements IPortTagParserExtension {
}
