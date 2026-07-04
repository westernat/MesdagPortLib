package org.mesdag.portlib.diff;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import java.util.Set;

@SuppressWarnings({"deprecation", "UnstableApiUsage"})
@Diff
public class PortRegistryManager {
    private static Set<ResourceLocation> synced;
    private static final Set<Registry<?>> VANILLA_SYNC_REGISTRIES = Set.of(
            BuiltInRegistries.SOUND_EVENT, // Required for SoundEvent packets
            BuiltInRegistries.MOB_EFFECT, // Required for MobEffect packets
            BuiltInRegistries.BLOCK, // Required for chunk BlockState paletted containers syncing
            BuiltInRegistries.ENTITY_TYPE, // Required for Entity spawn packets
            BuiltInRegistries.ITEM, // Required for Item/ItemStack packets
            BuiltInRegistries.FLUID, // Required for Fluid/FluidStack packets
            BuiltInRegistries.PARTICLE_TYPE, // Required for ParticleType packets
            BuiltInRegistries.BLOCK_ENTITY_TYPE, // Required for BlockEntity packets
            BuiltInRegistries.MENU, // Required for ClientboundOpenScreenPacket
            BuiltInRegistries.COMMAND_ARGUMENT_TYPE, // Required for ClientboundCommandsPacket
            BuiltInRegistries.STAT_TYPE, // Required for ClientboundAwardStatsPacket
            BuiltInRegistries.VILLAGER_TYPE, // Required for EntityDataSerializers
            BuiltInRegistries.VILLAGER_PROFESSION, // Required for EntityDataSerializers
            BuiltInRegistries.CAT_VARIANT, // Required for EntityDataSerializers
            BuiltInRegistries.FROG_VARIANT, // Required for EntityDataSerializers
//            BuiltInRegistries.DATA_COMPONENT_TYPE, // Required for itemstack sync
            BuiltInRegistries.RECIPE_SERIALIZER, // Required for Recipe sync
            BuiltInRegistries.ATTRIBUTE, // Required for ClientboundUpdateAttributesPacket

            // Required due to appearing in usages of ByteBufCodecs#registry
            BuiltInRegistries.POTION, // PotionContents#STREAM_CODEC
//            BuiltInRegistries.NUMBER_FORMAT_TYPE, // NumberFormatTypes#STREAM_CODEC
            BuiltInRegistries.CUSTOM_STAT, // StatType creates a registry StreamCodec using the provided stat registry
            BuiltInRegistries.POSITION_SOURCE_TYPE // PositionSource#STREAM_CODEC
//            BuiltInRegistries.ARMOR_MATERIAL, // TrimMaterial#DIRECT_STREAM_CODEC
//            BuiltInRegistries.MAP_DECORATION_TYPE // MapDecorationType#STREAM_CODEC
    );

    public static boolean isNonSyncedBuiltInRegistry(Registry<?> registry) {
        if (synced == null) {
            synced = Set.copyOf(RegistryManager.getRegistryNamesForSyncToClient());
        }
        ForgeRegistry<?> registry1 = RegistryManager.ACTIVE.getRegistry(registry.key());
        if (registry1 == null) return false;
        return !synced.contains(registry1.getRegistryName()) && !VANILLA_SYNC_REGISTRIES.contains(registry);
    }
}
