package org.mesdag.portlib.wrapper.resources;

import com.mojang.serialization.Codec;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.function.UnaryOperator;

public class PortIdentifier extends ResourceLocation {
    public static final Codec<PortIdentifier> CODEC = Codec.STRING.xmap(PortIdentifier::parse, PortIdentifier::toString).stable();
    public static final PortStreamCodec<FriendlyByteBuf, PortIdentifier> STREAM_CODEC = new PortStreamCodec<>() {
        @Override
        public void encode(FriendlyByteBuf byteBuf, PortIdentifier value) {
            byteBuf.writeResourceLocation(value);
        }

        @Override
        public PortIdentifier decode(FriendlyByteBuf byteBuf) {
            return parse(byteBuf.readUtf(32767));
        }
    };

    @SuppressWarnings("removal")
    private PortIdentifier(String namespace, String path) {
        super(namespace, path);
    }

    @Override
    public PortIdentifier withPath(String path) {
        return new PortIdentifier(getNamespace(), assertValidPath(getNamespace(), path));
    }

    @Override
    public PortIdentifier withPath(UnaryOperator<String> pathOperator) {
        return withPath(pathOperator.apply(getPath()));
    }

    @Override
    public PortIdentifier withPrefix(String pathPrefix) {
        return withPath(pathPrefix + getPath());
    }

    @Override
    public PortIdentifier withSuffix(String pathSuffix) {
        return withPath(getPath() + pathSuffix);
    }

    public static PortIdentifier fromNamespaceAndPath(String namespace, String path) {
        return new PortIdentifier(assertValidNamespace(namespace, path), assertValidPath(namespace, path));
    }

    public static PortIdentifier parse(String location) {
        return bySeparator(location, ':');
    }

    public static PortIdentifier bySeparator(String location, char seperator) {
        int i = location.indexOf(seperator);
        if (i >= 0) {
            String s = location.substring(i + 1);
            if (i != 0) {
                String s1 = location.substring(0, i);
                return createUntrusted(s1, s);
            } else {
                return withDefaultNamespace(s);
            }
        } else {
            return withDefaultNamespace(location);
        }
    }

    public static PortIdentifier withDefaultNamespace(String location) {
        return new PortIdentifier("minecraft", assertValidPath("minecraft", location));
    }

    public static @Nullable PortIdentifier tryParse(String location) {
        return tryBySeparator(location, ':');
    }

    public static @Nullable PortIdentifier tryBySeparator(String location, char separator) {
        int i = location.indexOf(separator);
        if (i >= 0) {
            String s = location.substring(i + 1);
            if (!isValidPath(s)) {
                return null;
            } else if (i != 0) {
                String s1 = location.substring(0, i);
                return isValidNamespace(s1) ? new PortIdentifier(s1, s) : null;
            } else {
                return new PortIdentifier(DEFAULT_NAMESPACE, s);
            }
        } else {
            return isValidPath(location) ? new PortIdentifier(DEFAULT_NAMESPACE, location) : null;
        }
    }

    private static String assertValidNamespace(String namespace, String path) {
        if (!isValidNamespace(namespace)) {
            throw new ResourceLocationException("Non [a-z0-9_.-] character in namespace of location: " + namespace + ":" + path);
        } else {
            return namespace;
        }
    }

    private static String assertValidPath(String namespace, String path) {
        if (!isValidPath(path)) {
            throw new ResourceLocationException("Non [a-z0-9/._-] character in path of location: " + namespace + ":" + path);
        } else {
            return path;
        }
    }

    private static PortIdentifier createUntrusted(String namespace, String path) {
        return new PortIdentifier(assertValidNamespace(namespace, path), assertValidPath(namespace, path));
    }
}
