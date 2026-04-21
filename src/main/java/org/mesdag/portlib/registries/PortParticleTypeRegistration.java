package org.mesdag.portlib.registries;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.core.particles.PortParticleOptions;

public class PortParticleTypeRegistration extends PortRegistration<ParticleType<?>> {
    PortParticleTypeRegistration(String namespace) {
        super(namespace, Registries.PARTICLE_TYPE);
    }

    public <T extends PortParticleOptions> PortRegistryEntry<ParticleType<?>, ParticleType<T>> register(String name, boolean overrideLimiter, MapCodec<T> codec, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
        //                                不能删除下方的T,会导致编译不通过
        return register(name, () -> new ParticleType<T>(overrideLimiter, new ParticleOptions.Deserializer<>() {
            @Override
            public T fromCommand(ParticleType<T> particleType, StringReader reader) throws CommandSyntaxException {
                return codec.compressedDecode(NbtOps.INSTANCE, TagParser.parseTag(reader.getString())).getOrThrow();
            }

            @Override
            public T fromNetwork(ParticleType<T> particleType, FriendlyByteBuf buffer) {
                return streamCodec.decode(buffer.wrap());
            }
        }) {
            @Override
            public Codec<T> codec() {
                return codec.codec();
            }
        });
    }

    public PortRegistryEntry<ParticleType<?>, SimpleParticleType> register(String name, boolean overrideLimiter) {
        return register(name, () -> new SimpleParticleType(overrideLimiter));
    }
}
