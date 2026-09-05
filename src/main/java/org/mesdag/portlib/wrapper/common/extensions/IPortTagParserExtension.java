package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

public interface IPortTagParserExtension {
    Codec<CompoundTag> AS_CODEC = Codec.STRING.comapFlatMap(s -> {
        try {
            return DataResult.success(new TagParser(new StringReader(s)).readSingleStruct(), Lifecycle.stable());
        } catch (CommandSyntaxException commandsyntaxexception) {
            return DataResult.error(commandsyntaxexception::getMessage);
        }
    }, CompoundTag::toString);
    Codec<CompoundTag> LENIENT_CODEC = PortCodecExtension.withAlternative(AS_CODEC, CompoundTag.CODEC);
}
