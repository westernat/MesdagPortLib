package org.mesdag.portlib.diff.mixin;

import PortLib.extensions.net.minecraft.world.entity.ai.attributes.Attributes.PortAttributesExtension;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements PortSelfGetter<LocalPlayer> {
    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getSneakingSpeedBonus(Lnet/minecraft/world/entity/LivingEntity;)F"))
    private float withAttribute(float original) {
        return original - 0.3F + (float) portlib$self().getAttributeValue(PortAttributesExtension.sneakingSpeed());
    }
}
