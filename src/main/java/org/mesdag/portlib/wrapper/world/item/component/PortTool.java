package org.mesdag.portlib.wrapper.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortEnvironment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public record PortTool(List<PortRule> rules, float defaultMiningSpeed, int damagePerBlock) {
    public static final Codec<PortTool> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PortRule.CODEC.listOf().fieldOf("rules").forGetter(PortTool::rules),
            Codec.FLOAT.optionalFieldOf("default_mining_speed", 1.0F).forGetter(PortTool::defaultMiningSpeed),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("damage_per_block", 1).forGetter(PortTool::damagePerBlock)
    ).apply(instance, PortTool::new));

    @Diff
    public static final String KEY = "portlib:tool";

    @Diff
    public CompoundTag save() {
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, PortEnvironment.registryAccess());
        return (CompoundTag) CODEC.encodeStart(ops, this).result().orElseGet(CompoundTag::new);
    }

    @Diff
    public static @Nullable PortTool load(CompoundTag data) {
        RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, PortEnvironment.registryAccess());
        return CODEC.parse(ops, data).result().orElse(null);
    }

    public float getMiningSpeed(BlockState state) {
        for (PortRule rule : rules) {
            if (rule.speed.isPresent() && state.is(rule.blocks)) {
                return rule.speed.get();
            }
        }

        return this.defaultMiningSpeed;
    }

    public boolean isCorrectForDrops(BlockState state) {
        for (PortRule rule : rules) {
            if (rule.correctForDrops.isPresent() && state.is(rule.blocks)) {
                return rule.correctForDrops.get();
            }
        }

        return false;
    }

    public record PortRule(
            HolderSet<Block> blocks,
            Optional<Float> speed,
            Optional<Boolean> correctForDrops
    ) {
        @Diff
        public static final Codec<PortRule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(PortRule::blocks),
                ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("speed").forGetter(PortRule::speed),
                Codec.BOOL.optionalFieldOf("correct_for_drops").forGetter(PortRule::correctForDrops)
        ).apply(instance, PortRule::new));

        public static PortRule minesAndDrops(List<Block> blocks, float speed) {
            return forBlocks(blocks, Optional.of(speed), Optional.of(true));
        }

        public static PortRule minesAndDrops(TagKey<Block> blocks, float speed) {
            return forTag(blocks, Optional.of(speed), Optional.of(true));
        }

        public static PortRule deniesDrops(TagKey<Block> blocks) {
            return forTag(blocks, Optional.empty(), Optional.of(false));
        }

        public static PortRule overrideSpeed(TagKey<Block> blocks, float speed) {
            return forTag(blocks, Optional.of(speed), Optional.empty());
        }

        public static PortRule overrideSpeed(List<Block> blocks, float speed) {
            return forBlocks(blocks, Optional.of(speed), Optional.empty());
        }

        private static PortRule forTag(TagKey<Block> tag, Optional<Float> speed, Optional<Boolean> correctForDrops) {
            return new PortRule(BuiltInRegistries.BLOCK.getOrCreateTag(tag), speed, correctForDrops);
        }

        private static PortRule forBlocks(List<Block> blocks, Optional<Float> speed, Optional<Boolean> correctForDrops) {
            return new PortRule(HolderSet.direct(blocks.stream().map(Block::builtInRegistryHolder).collect(Collectors.toList())), speed, correctForDrops);
        }
    }
}
