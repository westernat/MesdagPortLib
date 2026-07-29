package org.mesdag.portlib.datamap.builtin;

import PortLib.extensions.com.mojang.serialization.Codec.PortCodecExtension;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.PortLib;
import org.mesdag.portlib.datamap.PortDataMapType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.datamap.PortDataMapEntry;
import org.mesdag.portlib.diff.datamap.PortDataMapFile;
import org.mesdag.portlib.diff.mixin.WorkAtComposterAccessor;
import org.mesdag.portlib.wrapper.common.conditions.PortWithConditions;

import java.util.*;

/// PortLib 可堆肥物品 Data Map 的值。
///
/// @param chance             进入堆肥桶时提升层级的概率，范围为 `[0, 1]`
/// @param canVillagerCompost 是否允许农民村民把该物品投入堆肥桶
@SuppressWarnings({"unchecked", "deprecation", "rawtypes"})
public record PortCompostable(float chance, boolean canVillagerCompost) {
    public static final Codec<PortCompostable> CHANCE_CODEC = Codec.floatRange(0.0F, 1.0F)
            .xmap(PortCompostable::new, PortCompostable::chance);
    public static final Codec<PortCompostable> CODEC = PortCodecExtension.withAlternative(
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("chance").forGetter(PortCompostable::chance),
                    Codec.BOOL.optionalFieldOf("can_villager_compost", false).forGetter(PortCompostable::canVillagerCompost)
            ).apply(instance, PortCompostable::new)),
            CHANCE_CODEC
    );

    public PortCompostable(float chance) {
        this(chance, false);
    }

    private static Set<Item> VILLAGER_BACKUP;

    @Diff
    public static <T> void injectFake(Map<PortDataMapType<T, ?>, List<PortDataMapFile<?, T>>> results) {
        if (VILLAGER_BACKUP == null) {
            VILLAGER_BACKUP = new ReferenceOpenHashSet<>(WorkAtComposterAccessor.getCompostableItems());
        }
        ComposterBlock.COMPOSTABLES.clear();
        ComposterBlock.bootStrap();
        List<PortDataMapFile<?, T>> files = results.computeIfAbsent((PortDataMapType<T, ?>) PortLib.COMPOSTABLES, o -> new LinkedList<>());
        Map<Either<TagKey<T>, ResourceKey<T>>, Optional<PortWithConditions<PortDataMapEntry<PortCompostable>>>> values = new HashMap<>();
        for (Object2FloatMap.Entry<ItemLike> entry : ComposterBlock.COMPOSTABLES.object2FloatEntrySet()) {
            Item item = entry.getKey().asItem();
            PortCompostable compostable = new PortCompostable(entry.getFloatValue(), VILLAGER_BACKUP.contains(item));
            values.put(
                    Either.right((ResourceKey<T>) item.builtInRegistryHolder().key()),
                    Optional.of(new PortWithConditions<>(new PortDataMapEntry<>(compostable)))
            );
        }
        files.add(new PortDataMapFile<>(false, values, List.of()));
    }

    @Diff
    public static void doFill(@Nullable Map map) {
        if (map == null) return;
        ComposterBlock.COMPOSTABLES.clear();
        ImmutableList.Builder<Item> builder = ImmutableList.builder();
        for (Map.Entry<ResourceKey<Item>, PortCompostable> entry : ((Map<ResourceKey<Item>, PortCompostable>) map).entrySet()) {
            PortCompostable compostable = entry.getValue();
            Item item = Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(entry.getKey().location()));
            ComposterBlock.COMPOSTABLES.put(item, compostable.chance);
            if (compostable.canVillagerCompost) {
                builder.add(item);
            }
        }
        WorkAtComposterAccessor.setCompostableItems(builder.build());
    }
}
