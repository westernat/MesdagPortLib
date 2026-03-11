package org.mesdag.portlib.wrapper;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.mesdag.portlib.diff.Diff;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public record PortTool(List<PortRule> rules, float defaultMiningSpeed, int damagePerBlock) {
    @Diff
    public static PortTool of(Tool tool) {
        return new PortTool(Lists.transform(tool.rules(), PortRule::of), tool.defaultMiningSpeed(), tool.damagePerBlock());
    }

    @Diff
    public Tool unwrap() {
        return new Tool(Lists.transform(rules, PortRule::unwrap), defaultMiningSpeed, damagePerBlock);
    }

    public float getMiningSpeed(BlockState state) {
        for (PortRule tool$rule : this.rules) {
            if (tool$rule.speed.isPresent() && state.is(tool$rule.blocks)) {
                return tool$rule.speed.get();
            }
        }

        return this.defaultMiningSpeed;
    }

    public boolean isCorrectForDrops(BlockState state) {
        for (PortRule rule : this.rules) {
            if (rule.correctForDrops.isPresent() && state.is(rule.blocks)) {
                return rule.correctForDrops.get();
            }
        }

        return false;
    }

    public record PortRule(HolderSet<Block> blocks, Optional<Float> speed, Optional<Boolean> correctForDrops) {
        @Diff
        public static PortRule of(Tool.Rule rule) {
            return new PortRule(rule.blocks(), rule.speed(), rule.correctForDrops());
        }

        @Diff
        public Tool.Rule unwrap() {
            return new Tool.Rule(blocks, speed, correctForDrops);
        }

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
