package PortLib.extensions.net.minecraft.resources.ResourceLocation;

import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public class PortResourceLocationExtension {
    private static final int MAX_SERIALIZED_LENGTH = 1024;
    private static final PortStreamCodec<ByteBuf, ResourceLocation> STREAM_CODEC =
            PortByteBufCodecs.stringUtf8(MAX_SERIALIZED_LENGTH)
                    .map(ResourceLocation::parse, ResourceLocation::toString);

    public static PortStreamCodec<ByteBuf, ResourceLocation> streamCodec() {
        return STREAM_CODEC;
    }
}
