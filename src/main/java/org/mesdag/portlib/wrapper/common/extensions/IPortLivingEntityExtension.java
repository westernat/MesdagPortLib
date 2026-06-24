package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.entity.LivingEntity.PortLivingEntityExtension;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.common.PortEffectCure;
import org.mesdag.portlib.wrapper.common.damagesource.PortDamageContainer;

@SuppressWarnings("all")
public interface IPortLivingEntityExtension extends IPortEntityExtension {
    private LivingEntity self() {
        return (LivingEntity) this;
    }

    default boolean onDamageTaken(PortDamageContainer container) {
        return true;
    }

    default @Nullable AttributeInstance getAttribute(Holder<Attribute> attribute) {
        return PortLivingEntityExtension.getAttribute(self(), attribute);
    }

    default boolean removeEffectsCuredBy(PortEffectCure cure) {
        return PortLivingEntityExtension.removeEffectsCuredBy(self(), cure);
    }

    @Override
    default ItemStack getWeaponItem() {
        return PortLivingEntityExtension.getWeaponItem(self());
    }

    default boolean hasInfiniteMaterials() {
        return PortLivingEntityExtension.hasInfiniteMaterials(self());
    }

    @Diff
    default boolean hasEffect(RegistryObject<? extends MobEffect> effect) {
        return self().hasEffect(effect.get());
    }

    static IPortLivingEntityExtension of(LivingEntity living) {
        return (IPortLivingEntityExtension) living;
    }
}
