package PortLib.extensions.net.minecraft.world.entity.LivingEntity;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.minecraft.world.entity.LivingEntity;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

@Extension
public class PortLivingEntityExtension {
    /// @return if false, it will skip original [onDamageTaken][net.neoforged.neoforge.common.extensions.ILivingEntityExtension#onDamageTaken] invoke
    public static boolean onDamageTaken(@This LivingEntity thiz, PortDamageContainer damageContainer) {
        return true;
    }
}
