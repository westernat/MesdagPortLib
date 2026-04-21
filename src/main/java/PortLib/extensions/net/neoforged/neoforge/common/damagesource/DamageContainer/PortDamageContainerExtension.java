package PortLib.extensions.net.neoforged.neoforge.common.damagesource.DamageContainer;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

@Extension
public class PortDamageContainerExtension {
    public static PortDamageContainer wrap(@This DamageContainer thiz) {
        return new PortDamageContainer(thiz);
    }

    public static class Reduction {
        public static PortDamageContainer.PortReduction wrap(@This DamageContainer.Reduction thiz) {
            return switch (thiz) {
                case INVULNERABILITY -> PortDamageContainer.PortReduction.INVULNERABILITY;
                case ARMOR -> PortDamageContainer.PortReduction.ARMOR;
                case ENCHANTMENTS -> PortDamageContainer.PortReduction.ENCHANTMENTS;
                case MOB_EFFECTS -> PortDamageContainer.PortReduction.MOB_EFFECTS;
                case ABSORPTION -> PortDamageContainer.PortReduction.ABSORPTION;
                case INNATE_RESISTANCE -> PortDamageContainer.PortReduction.INNATE_RESISTANCE;
            };
        }
    }
}
