package org.mesdag.portlib.network;

import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class PortPayloadHandler {
    /// delegate已通过[PortPacketDistributor#PACKET_OWNER]来防止被GC
    private final PortNetworkHandler delegate;

    public PortPayloadHandler(String namespace, String version) {
        this.delegate = new PortNetworkHandler(namespace, version);
    }

    public PortNetworkHandler getDelegate() {
        return delegate;
    }

    public <P extends IPortPacket.S2C> PortPayloadHandler registerInGameS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        delegate.register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> delegate.s2c(p, s, handler), clazz, PortNetworkDirection.PLAY_TO_CLIENT);
        return this;
    }

    public <P extends IPortPacket.S2C> PortPayloadHandler registerInGameS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameS2C(clazz, identifier, codec, IPortPacket.S2C::handle);
        return this;
    }

    public <P extends IPortPacket.C2S> PortPayloadHandler registerInGameC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        delegate.register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> delegate.c2s(p, s, handler), clazz, PortNetworkDirection.PLAY_TO_SERVER);
        return this;
    }

    public <P extends IPortPacket.C2S> PortPayloadHandler registerInGameC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameC2S(clazz, identifier, codec, IPortPacket.C2S::handle);
        return this;
    }

    /// 登录期（LOGIN 协议内、进世界之前）方向：与 registerLoginS2C/registerLoginC2S 配套的是
    /// {@link PortNetworkHandler#sendLoginToClient(Connection, int, IPortPacket)} 走 fml:loginwrapper 信封发送。
    public <P extends IPortPacket.S2C> PortPayloadHandler registerLoginS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        delegate.register(identifier, codec, (p, s) -> delegate.s2c(p, s, handler), clazz, PortNetworkDirection.LOGIN_TO_CLIENT);
        return this;
    }

    public <P extends IPortPacket.S2C> PortPayloadHandler registerLoginS2C(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec) {
        registerLoginS2C(clazz, identifier, codec, IPortPacket.S2C::handle);
        return this;
    }

    public <P extends IPortPacket.C2S> PortPayloadHandler registerLoginC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        delegate.register(identifier, codec, (p, s) -> delegate.c2s(p, s, handler), clazz, PortNetworkDirection.LOGIN_TO_SERVER);
        return this;
    }

    public <P extends IPortPacket.C2S> PortPayloadHandler registerLoginC2S(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super FriendlyByteBuf, P> codec) {
        registerLoginC2S(clazz, identifier, codec, IPortPacket.C2S::handle);
        return this;
    }

    public <P extends IPortPacket> PortPayloadHandler registerInGameBidirectional(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec, BiConsumer<P, IPortPacket.Context> handler) {
        delegate.register(identifier, (PortStreamCodec<? super FriendlyByteBuf, P>) codec, (p, s) -> {
            if (s.get().getDirection().getOriginationSide().isServer()) {
                delegate.s2c(p, s, handler);
            } else {
                delegate.c2s(p, s, handler);
            }
        }, clazz, null);
        return this;
    }

    public <P extends IPortPacket> PortPayloadHandler registerInGameBidirectional(Class<P> clazz, ResourceLocation identifier, PortStreamCodec<? super PortRegistryFriendlyByteBuf, P> codec) {
        registerInGameBidirectional(clazz, identifier, codec, IPortPacket::handle);
        return this;
    }
}
