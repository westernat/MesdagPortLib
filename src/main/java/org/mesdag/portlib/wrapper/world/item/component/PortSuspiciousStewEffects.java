package org.mesdag.portlib.wrapper.world.item.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SuspiciousStewItem;
import org.mesdag.portlib.wrapper.world.effect.MobEffectHolder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public record PortSuspiciousStewEffects(ItemStack suspiciousStewStack) {
    public PortSuspiciousStewEffects {
        if (suspiciousStewStack.isEmpty()) {
            throw new IllegalArgumentException("SuspiciousStewStack cannot be empty!");
        }
    }

    public PortSuspiciousStewEffects withEffectAdded(PortEntry entry) {
        SuspiciousStewItem.saveMobEffect(suspiciousStewStack, entry.effect.value(), entry.duration);
        return this;
    }

    public List<PortEntry> effects() {
        List<PortEntry> effects = new ArrayList<>();
        SuspiciousStewItem.listPotionEffects(suspiciousStewStack, instance -> effects.add(new PortEntry(
                MobEffectHolder.wrap(instance.getEffect()), instance.getDuration()
        )));
        return effects;
    }

    public void applyTo(ItemStack stack) {
        if (stack == suspiciousStewStack) return;
        CompoundTag tag = suspiciousStewStack.getOrCreateTag();
        if (tag.contains(SuspiciousStewItem.EFFECTS_TAG, Tag.TAG_LIST)) {
            stack.getOrCreateTag().put(
                    SuspiciousStewItem.EFFECTS_TAG,
                    tag.getList(SuspiciousStewItem.EFFECTS_TAG, Tag.TAG_COMPOUND).copy()
            );
        }
    }

    public record PortEntry(MobEffectHolder effect, int duration) {
        public MobEffectInstance createEffectInstance() {
            return new MobEffectInstance(effect.value(), duration);
        }
    }
}
