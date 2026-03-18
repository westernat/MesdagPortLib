package org.mesdag.portlib.diff.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateEnabledFeaturesPacket;
import net.minecraft.world.flag.FeatureFlagSet;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.brewing.PortRegisterBrewingRecipesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Shadow
    public abstract RegistryAccess registryAccess();

    @Shadow
    public abstract FeatureFlagSet enabledFeatures();

    @Unique
    private boolean portlib$logged;

    @Unique
    private boolean portlib$enabled;

    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void handle(ClientboundLoginPacket packet, CallbackInfo ci) {
        this.portlib$logged = true;
        portlib$postEvent();
    }

    @Inject(method = "handleEnabledFeatures", at = @At("TAIL"))
    private void handle(ClientboundUpdateEnabledFeaturesPacket packet, CallbackInfo ci) {
        this.portlib$enabled = true;
        portlib$postEvent();
    }

    @Unique
    private void portlib$postEvent() {
        if (portlib$logged && portlib$enabled) {
            PortEventHandler.postEvent(new PortRegisterBrewingRecipesEvent(enabledFeatures(), registryAccess()));
        }
    }
}
