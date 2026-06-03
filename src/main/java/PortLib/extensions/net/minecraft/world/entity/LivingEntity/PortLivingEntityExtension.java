package PortLib.extensions.net.minecraft.world.entity.LivingEntity;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import org.mesdag.portlib.wrapper.common.extensions.IPortLivingEntityExtension;

@Extension
public class PortLivingEntityExtension {
    /// @return if false, it will skip original onDamageTaken invoke (Neoforge only)
    public static boolean onDamageTaken(@This LivingEntity thiz, PortDamageContainer damageContainer) {
        return IPortLivingEntityExtension.of(thiz).onDamageTaken(damageContainer);
    }

    public static @Nullable AttributeInstance getAttribute(@This LivingEntity thiz, Holder<Attribute> attribute) {
        return thiz.getAttribute(attribute.value());
    }
}
