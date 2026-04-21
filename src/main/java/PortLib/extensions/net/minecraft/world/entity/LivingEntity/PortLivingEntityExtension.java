package PortLib.extensions.net.minecraft.world.entity.LivingEntity;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;
import org.mesdag.portlib.wrapper.common.extension.IPortLivingEntityExtension;

@Extension
public class PortLivingEntityExtension {
    /// @return if false, it will skip original [onDamageTaken][net.neoforged.neoforge.common.extensions.ILivingEntityExtension#onDamageTaken] invoke (Neoforge only)
    public static boolean onDamageTaken(@This LivingEntity thiz, PortDamageContainer damageContainer) {
        return IPortLivingEntityExtension.of(thiz).onDamageTaken(damageContainer);
    }
}
