package PortLib.extensions.net.minecraft.resources.ResourceLocation;

import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortResourceLocationExtension {
    private static final PortStreamCodec<ByteBuf, ResourceLocation> STREAM_CODEC =
            PortByteBufCodecs.STRING_UTF8
                    .map(ResourceLocation::parse, ResourceLocation::toString);

    public static PortStreamCodec<ByteBuf, ResourceLocation> streamCodec() {
        return STREAM_CODEC;
    }
}
