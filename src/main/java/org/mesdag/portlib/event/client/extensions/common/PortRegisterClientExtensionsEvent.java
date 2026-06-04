package org.mesdag.portlib.event.client.extensions.common;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.event.IModBusEvent;
import org.mesdag.portlib.diff.IPortBlock;
import org.mesdag.portlib.diff.IPortFluidType;
import org.mesdag.portlib.diff.IPortItem;
import org.mesdag.portlib.diff.IPortMobEffect;

public class PortRegisterClientExtensionsEvent extends Event implements IModBusEvent {
    public PortRegisterClientExtensionsEvent() {}

    public void registerBlock(IClientBlockExtensions extensions, Block... blocks) {
        for (Block block : blocks) {
            IPortBlock.of(block).portlib$setRenderPropertiesInternal(extensions);
        }
    }

    @SafeVarargs
    public final void registerBlock(IClientBlockExtensions extensions, Holder<Block>... blocks) {
        for (Holder<Block> block : blocks) {
            IPortBlock.of(block.value()).portlib$setRenderPropertiesInternal(extensions);
        }
    }

    public boolean isBlockRegistered(Block block) {
        return block.getRenderPropertiesInternal() != null;
    }

    public void registerItem(IClientItemExtensions extensions, Item... items) {
        for (Item item : items) {
            IPortItem.of(item).portlib$setRenderPropertiesInternal(extensions);
        }
    }

    @SafeVarargs
    public final void registerItem(IClientItemExtensions extensions, Holder<Item>... items) {
        for (Holder<Item> item : items) {
            IPortItem.of(item.value()).portlib$setRenderPropertiesInternal(extensions);
        }
    }

    public boolean isItemRegistered(Item item) {
        return item.getRenderPropertiesInternal() != null;
    }

    public void registerMobEffect(IClientMobEffectExtensions extensions, MobEffect... mobEffects) {
        for (MobEffect mobEffect : mobEffects) {
            IPortMobEffect.of(mobEffect).portlib$setRenderPropertiesInternal(extensions);
        }
    }

    @SafeVarargs
    public final void registerMobEffect(IClientMobEffectExtensions extensions, Holder<MobEffect>... mobEffects) {
        for (Holder<MobEffect> mobEffect : mobEffects) {
            IPortMobEffect.of(mobEffect.value()).portlib$setRenderPropertiesInternal(extensions);
        }
    }

    public boolean isMobEffectRegistered(MobEffect mobEffect) {
        return mobEffect.getEffectRendererInternal() != null;
    }

    public void registerFluidType(IClientFluidTypeExtensions extensions, FluidType... fluidTypes) {
        for (FluidType fluidType : fluidTypes) {
            IPortFluidType.of(fluidType).portlib$setRenderPropertiesInternal(extensions);
        }
    }

    @SafeVarargs
    public final void registerFluidType(IClientFluidTypeExtensions extensions, Holder<FluidType>... fluidTypes) {
        for (Holder<FluidType> fluidType : fluidTypes) {
            IPortFluidType.of(fluidType.value()).portlib$setRenderPropertiesInternal(extensions);
        }
    }

    public boolean isFluidTypeRegistered(FluidType fluidType) {
        return fluidType.getRenderPropertiesInternal() != null;
    }
}
