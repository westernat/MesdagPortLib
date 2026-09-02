package org.mesdag.portlib;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.datamap.builtin.PortCompostable;
import org.mesdag.portlib.datamap.builtin.PortFurnaceFuel;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortLivingEntity;
import org.mesdag.portlib.diff.PortRegistries;
import org.mesdag.portlib.diff.attachment.PortAttachmentInternals;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.diff.datamap.PortDataMapLoader;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.PortEventHooks;
import org.mesdag.portlib.event.other.PortModifyDefaultComponentsEvent;
import org.mesdag.portlib.event.registries.PortRegisterDataMapTypesEvent;
import org.mesdag.portlib.loot.PortAddTableLootModifier;
import org.mesdag.portlib.network.PortNetworkHandler;
import org.mesdag.portlib.registries.PortAttributeRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.registries.PortRegistryEntry;
import org.mesdag.portlib.wrapper.common.PortBooleanAttribute;
import org.mesdag.portlib.wrapper.common.extensions.IPortEntityExtension;
import org.mesdag.portlib.wrapper.common.world.PortAddCarversBiomeModifier;
import org.mesdag.portlib.wrapper.sounds.PortSoundEvents;
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
    public static final PortRegistryEntry<Attribute, RangedAttribute> JUMP_STRENGTH = ATTRIBUTES.register(
            "generic.jump_strength",
            () -> new RangedAttribute("attribute.name.generic.jump_strength", 0.42F, 0.0, 32.0),
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
            "generic.water_movement_efficiency",
            () -> new RangedAttribute("attribute.name.generic.water_movement_efficiency", 0.0, 0.0, 1.0),
            maker -> maker.setSyncable(true)
    );
    public static final PortRegistryEntry<Attribute, PortBooleanAttribute> CREATIVE_FLIGHT = ATTRIBUTES.register(
            "player.creative_flight",
            () -> new PortBooleanAttribute("attribute.name.player.creative_flight", false),
            maker -> maker.setSyncable(true)
    );

    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);
    public static final RegistryObject<Codec<PortAddTableLootModifier>> ADD_TABLE_LOOT_MODIFIER = GLOBAL_LOOT_MODIFIERS.register("add_table", () -> PortAddTableLootModifier.CODEC);

    public static final PortDataMapType<Item, PortCompostable> COMPOSTABLES = PortDataMapType.builder(
            asResource("compostables"),
            Registries.ITEM,
            PortCompostable.CODEC
    ).synced(PortCompostable.CHANCE_CODEC, false).build();
    public static final PortDataMapType<Item, PortFurnaceFuel> FURNACE_FUELS = PortDataMapType.builder(asResource("furnace_fuels"), Registries.ITEM, PortFurnaceFuel.CODEC).synced(PortFurnaceFuel.BURN_TIME_CODEC, false).build();

    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MODID);
    public static final RegistryObject<Codec<PortAddCarversBiomeModifier>> ADD_CARVERS_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_carvers", () -> PortAddCarversBiomeModifier.CODEC);

    public PortLib(FMLJavaModLoadingContext context) {
        PortRegisterHandler.init(context.getModEventBus());
        PortRegistries.init();
        PortAttachmentSync.init();
        PortEventHooks.init();
        PortAttachmentInternals.init();
        PortDataMapLoader.init();
        PortNetworkHandler.init();
        IPortLivingEntity.init();
        IPortEntityExtension.init();
        IEventBus eventBus = context.getModEventBus();
        PortSoundEvents.register(eventBus);
        GLOBAL_LOOT_MODIFIERS.register(eventBus);
        BIOME_MODIFIER_SERIALIZERS.register(eventBus);
        PortEventHandler.addListener((RegisterCapabilitiesEvent event) -> {
//            ForcedChunkManager
            PortDataMapLoader.initDataMaps();
            PortModifyDefaultComponentsEvent.modifyComponents();
//            extendPoiTypes
        });

        PortEventHandler.addListener((EntityAttributeModificationEvent event) -> {
            for (EntityType<? extends LivingEntity> type : event.getTypes()) {
                if (type == EntityType.PLAYER) continue;
                for (PortRegistryEntry<Attribute, Attribute> entry : ATTRIBUTES.getEntries()) {
                    if (entry.getId().getPath().startsWith("player.")) continue;
                    event.add(type, entry.value());
                }
            }
            for (PortRegistryEntry<Attribute, Attribute> entry : ATTRIBUTES.getEntries()) {
                event.add(EntityType.PLAYER, entry.value());
            }
            event.add(EntityType.PLAYER, FLYING_SPEED.value(), 1.0);
        });

        PortEventHandler.addListener((PortRegisterDataMapTypesEvent event) -> {
            event.register(COMPOSTABLES);
            event.register(FURNACE_FUELS);
        });
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
