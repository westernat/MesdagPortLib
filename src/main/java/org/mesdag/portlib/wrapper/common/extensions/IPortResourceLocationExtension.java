package org.mesdag.portlib.wrapper.common.extensions;

import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public interface IPortResourceLocationExtension {
    PortStreamCodec<ByteBuf, ResourceLocation> STREAM_CODEC = PortByteBufCodecs.STRING_UTF8
            .map(ResourceLocation::parse, ResourceLocation::toString);

}
