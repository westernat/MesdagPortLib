package org.mesdag.portlib.wrapper.world;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import org.mesdag.portlib.diff.Diff;

public enum PortItemInteractionResult {
    SUCCESS,
    CONSUME,
    CONSUME_PARTIAL,
    PASS_TO_DEFAULT_BLOCK_INTERACTION,
    SKIP_DEFAULT_BLOCK_INTERACTION,
    FAIL;

    @Diff
    public ItemInteractionResult unwrap() {
        return switch (this) {
            case SUCCESS -> ItemInteractionResult.SUCCESS;
            case CONSUME -> ItemInteractionResult.CONSUME;
            case CONSUME_PARTIAL -> ItemInteractionResult.CONSUME_PARTIAL;
            case PASS_TO_DEFAULT_BLOCK_INTERACTION -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            case SKIP_DEFAULT_BLOCK_INTERACTION -> ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
            default -> ItemInteractionResult.FAIL;
        };
    }

    @Diff
    public static PortItemInteractionResult wrap(ItemInteractionResult delegate) {
        return switch (delegate) {
            case SUCCESS -> SUCCESS;
            case CONSUME -> CONSUME;
            case CONSUME_PARTIAL -> CONSUME_PARTIAL;
            case PASS_TO_DEFAULT_BLOCK_INTERACTION -> PASS_TO_DEFAULT_BLOCK_INTERACTION;
            case SKIP_DEFAULT_BLOCK_INTERACTION -> SKIP_DEFAULT_BLOCK_INTERACTION;
            default -> FAIL;
        };
    }

    public boolean consumesAction() {
        return unwrap().consumesAction();
    }

    public static PortItemInteractionResult sidedSuccess(boolean clientSide) {
        return PortItemInteractionResult.wrap(ItemInteractionResult.sidedSuccess(clientSide));
    }

    public InteractionResult result() {
        return unwrap().result();
    }
}
