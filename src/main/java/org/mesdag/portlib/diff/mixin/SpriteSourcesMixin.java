package org.mesdag.portlib.diff.mixin;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortRegisterSpriteSourceTypesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SpriteSources.class)
public abstract class SpriteSourcesMixin {
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/HashBiMap;create()Lcom/google/common/collect/HashBiMap;", remap = false))
    private static <K, V> HashBiMap<K, V> postEvent(HashBiMap<K, V> original) {
        PortEventHandler.postEvent(new PortRegisterSpriteSourceTypesEvent((BiMap<ResourceLocation, SpriteSourceType>) original));
        return original;
    }
}
