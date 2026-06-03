package org.mesdag.portlib;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortLivingEntity;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.diff.attachment.PortAttachmentInternals;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.diff.datamap.PortDataMapLoader;
import org.mesdag.portlib.diff.test.TestAttachment;
import org.mesdag.portlib.diff.test.TestComponent;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.entity.PortEntityAttributeModificationEvent;
import org.mesdag.portlib.event.other.PortModifyDefaultComponentsEvent;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.registries.*;
import org.mesdag.portlib.wrapper.PortEnvironment;
import org.mesdag.portlib.wrapper.common.PortBooleanAttribute;
import org.mesdag.portlib.wrapper.world.effect.PortMobEffect;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(PortLib.MODID)
public class PortLib {
    public static final String MODID = "portlib";
    public static final Logger LOGGER = LoggerFactory.getLogger("PortLib");
    @Diff
    public static final PortNetworkHandler NETWORK_HANDLER = new PortNetworkHandler(MODID, "1");

    private static final PortAttributeRegistration ATTRIBUTES = PortRegisterHandler.attribute(MODID);
    public static final PortRegistryEntry<Attribute, RangedAttribute> BLOCK_BREAK_SPEED = ATTRIBUTES.register(
            "player.block_break_speed",
            () -> new RangedAttribute("attribute.name.player.block_break_speed", 1.0, 0.0, 1024.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> BURNING_TIME = ATTRIBUTES.register(
            "generic.burning_time",
            () -> new RangedAttribute("attribute.name.generic.burning_time", 1.0, 0.0, 1024.0),
            maker -> maker.setSyncable(true).setSentiment(PortAttribute.PortSentiment.NEGATIVE)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> EXPLOSION_KNOCKBACK_RESISTANCE = ATTRIBUTES.register(
            "generic.explosion_knockback_resistance",
            () -> new RangedAttribute("attribute.name.generic.explosion_knockback_resistance", 0.0, 0.0, 1.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> FALL_DAMAGE_MULTIPLIER = ATTRIBUTES.register(
            "generic.fall_damage_multiplier",
            () -> new RangedAttribute("attribute.name.generic.fall_damage_multiplier", 1.0, 0.0, 100.0),
            maker -> maker.setSyncable(true).setSentiment(PortAttribute.PortSentiment.NEGATIVE)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> FLYING_SPEED = ATTRIBUTES.register(
            "generic.flying_speed",
            () -> new RangedAttribute("attribute.name.generic.flying_speed", 0.4, 0.0, 1024.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> MAX_ABSORPTION = ATTRIBUTES.register(
            "generic.max_absorption",
            () -> new RangedAttribute("attribute.name.generic.max_absorption", 0.0, 0.0, 2048.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> MINING_EFFICIENCY = ATTRIBUTES.register(
            "player.mining_efficiency",
            () -> new RangedAttribute("attribute.name.player.mining_efficiency", 0.0, 0.0, 1024.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> MOVEMENT_EFFICIENCY = ATTRIBUTES.register(
            "generic.movement_efficiency",
            () -> new RangedAttribute("attribute.name.generic.movement_efficiency", 0.0, 0.0, 1.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> OXYGEN_BONUS = ATTRIBUTES.register(
            "generic.oxygen_bonus",
            () -> new RangedAttribute("attribute.name.generic.oxygen_bonus", 0.0, 0.0, 1024.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> SAFE_FALL_DISTANCE = ATTRIBUTES.register(
            "generic.safe_fall_distance",
            () -> new RangedAttribute("attribute.name.generic.safe_fall_distance", 3.0, -1024.0, 1024.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> SCALE = ATTRIBUTES.register(
            "generic.scale",
            () -> new RangedAttribute("attribute.name.generic.scale", 1.0, 0.0625, 16.0),
            maker -> maker.setSyncable(true).setSentiment(PortAttribute.PortSentiment.NEUTRAL)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> SNEAKING_SPEED = ATTRIBUTES.register(
            "player.sneaking_speed",
            () -> new RangedAttribute("attribute.name.player.sneaking_speed", 0.3, 0.0, 1.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> SUBMERGED_MINING_SPEED = ATTRIBUTES.register(
            "player.submerged_mining_speed",
            () -> new RangedAttribute("attribute.name.player.submerged_mining_speed", 0.2, 0.0, 20.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> SWEEPING_DAMAGE_RATIO = ATTRIBUTES.register(
            "player.sweeping_damage_ratio",
            () -> new RangedAttribute("attribute.name.player.sweeping_damage_ratio", 0.0, 0.0, 1.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, RangedAttribute> WATER_MOVEMENT_EFFICIENCY = ATTRIBUTES.register(
            "player.water_movement_efficiency",
            () -> new RangedAttribute("attribute.name.player.water_movement_efficiency", 0.0, 0.0, 1.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, PortBooleanAttribute> CREATIVE_FLIGHT = ATTRIBUTES.register(
            "player.creative_flight",
            () -> new PortBooleanAttribute("attribute.name.player.creative_flight", false),
            maker -> maker.setSyncable(true)
    );

    public PortLib() {
        PortRegisterHandler.init();
        PortRegistries.init();
        PortAttachmentSync.init();
        PortEventHooks.init();
        PortAttachmentInternals.init();
        PortDataMapLoader.init();
        PortNetworkHandler.init();
        IPortLivingEntity.init();
        PortEventHandler.addListener((RegisterCapabilitiesEvent event) -> {
//            ForgeChunkManager
            PortDataMapLoader.initDataMaps();
            PortModifyDefaultComponentsEvent.modifyComponents();
//            extendPoiTypes
        });

        PortEventHandler.addListener((PortEntityAttributeModificationEvent event) -> {
            event.add(EntityType.PLAYER, BLOCK_BREAK_SPEED);
            event.add(EntityType.PLAYER, SUBMERGED_MINING_SPEED);
            event.add(EntityType.PLAYER, SNEAKING_SPEED);
            event.add(EntityType.PLAYER, MINING_EFFICIENCY);
            event.add(EntityType.PLAYER, SWEEPING_DAMAGE_RATIO);
            event.add(EntityType.PLAYER, CREATIVE_FLIGHT);
        });

        if (PortEnvironment.isDeveloper()) {
            PortAttachmentRegistration attachment = PortRegisterHandler.attachment(MODID);
            PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<TestAttachment>> testAttachment = attachment.registerSimple("test", () -> PortAttachmentType.serializable(() -> new TestAttachment(true)).sync(TestAttachment.STREAM_CODEC).copyOnDeath());

            PortDataComponentRegistration dataComponent = PortRegisterHandler.dataComponent(MODID);
            PortRegistryEntry<PortDataComponentType<?>, PortDataComponentType<TestComponent>> testDataComponent = dataComponent.register("test", builder -> builder.persistent(TestComponent.CODEC).networkSynchronized(TestComponent.STREAM_CODEC));

            PortEventHandler.addListener((PlayerInteractEvent.EntityInteract event) -> {
                ItemStack stack = event.getItemStack();
                if (!stack.isEmpty()) {
                    if (!event.getLevel().isClientSide) {
                        TestAttachment data = event.getTarget().getAttach(testAttachment::get);
                        data.setStack(stack);

                        stack.setData(testDataComponent, new TestComponent(1));
                    }
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            });

            PortRegistration<MobEffect> effects = PortRegisterHandler.create(MODID, Registries.MOB_EFFECT);
            effects.register("test", () -> new PortMobEffect(MobEffectCategory.BENEFICIAL, 0xFF0000, ParticleTypes.EXPLOSION));
        }
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
