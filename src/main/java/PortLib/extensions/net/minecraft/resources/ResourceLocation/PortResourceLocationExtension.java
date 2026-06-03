package PortLib.extensions.net.minecraft.resources.ResourceLocation;

import io.netty.buffer.ByteBuf;
import manifold.ext.rt.api.Extension;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

@Extension
public class PortResourceLocationExtension {
    private static final PortStreamCodec<ByteBuf, ResourceLocation> STREAM_CODEC = PortByteBufCodecs.STRING_UTF8.map(ResourceLocation::parse, ResourceLocation::toString);

    @Extension
    public static PortStreamCodec<ByteBuf, ResourceLocation> streamCodec() {
        return STREAM_CODEC;
    }
}
