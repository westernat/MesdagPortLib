package PortLib.extensions.com.mojang.serialization.Codec;

import com.mojang.serialization.Codec;
import manifold.ext.rt.api.Extension;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector4f;

@Extension
public class PortCodecExtension {
    @Extension
    public static Codec<Vector4f> vector4f() {
        return ExtraCodecs.VECTOR4F;
    }
}
