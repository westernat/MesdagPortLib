package org.mesdag.portlib.registries;

import PortLib.extensions.com.mojang.serialization.DataResult.PortDataResultExtension;
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
import net.minecraft.resources.RegistryOps;
import org.mesdag.portlib.network.PortRegistryFriendlyByteBuf;
import org.mesdag.portlib.network.codec.PortStreamCodec;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.common.extensions.IPortFriendlyByteBufExtension;

public class PortParticleTypeRegistration extends PortRegistration<ParticleType<?>> {
    PortParticleTypeRegistration(String namespace) {
        super(namespace, Registries.PARTICLE_TYPE);
    }

    public <T extends ParticleOptions> PortRegistryEntry<ParticleType<?>, ParticleType<T>> register(String name, boolean overrideLimiter, MapCodec<T> codec, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
        return register(name, () -> new PortLibParticleType<>(overrideLimiter, codec, streamCodec));
    }

    public PortRegistryEntry<ParticleType<?>, SimpleParticleType> register(String name, boolean overrideLimiter) {
        return register(name, () -> new SimpleParticleType(overrideLimiter));
    }

    public static class PortLibParticleType<T extends ParticleOptions> extends ParticleType<T> {
        private final MapCodec<T> codec;
        private final PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec;

        PortLibParticleType(boolean overrideLimiter, MapCodec<T> codec, PortStreamCodec<? super PortRegistryFriendlyByteBuf, T> streamCodec) {
            super(overrideLimiter, new ParticleOptions.Deserializer<>() {
                @Override
                public T fromCommand(ParticleType<T> type, StringReader reader) throws CommandSyntaxException {
                    return PortDataResultExtension.getOrThrow(codec.compressedDecode(NbtOps.INSTANCE, TagParser.parseTag(reader.getString())));
                }

                @Override
                public T fromNetwork(ParticleType<T> type, FriendlyByteBuf buffer) {
                    return streamCodec.decode(IPortFriendlyByteBufExtension.of(buffer).wrap());
                }
            });
            this.codec = codec;
            this.streamCodec = streamCodec;
        }

        @Override
        public Codec<T> codec() {
            return codec.codec();
        }

        public void writeToNetwork(T option, FriendlyByteBuf buffer) {
            streamCodec.encode(IPortFriendlyByteBufExtension.of(buffer).wrap(), option);
        }

        public String writeToString(T option) {
            return codec.codec().encodeStart(RegistryOps.create(NbtOps.INSTANCE, PortEnvironment.registryAccess()), option).result().map(Object::toString).orElse("");
        }
    }
}
