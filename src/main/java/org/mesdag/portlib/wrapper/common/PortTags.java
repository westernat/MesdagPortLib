package org.mesdag.portlib.wrapper.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import org.mesdag.portlib.PortLib;

public class PortTags {
    public static class DamageTypes {
        public static final TagKey<DamageType> IS_MAGIC = common("is_magic");

        private static TagKey<DamageType> portlib(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, PortLib.asResource(name));
        }

        private static TagKey<DamageType> common(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("c", name));
        }
    }
}
