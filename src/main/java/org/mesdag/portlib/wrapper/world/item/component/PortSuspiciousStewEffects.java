package org.mesdag.portlib.wrapper.world.item.component;

import com.google.common.collect.Lists;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.effect.MobEffectHolder;

import java.util.List;

@SuppressWarnings("all")
public record PortSuspiciousStewEffects(ItemStack suspiciousStewStack) {
    public PortSuspiciousStewEffects {
        if (suspiciousStewStack.isEmpty()) {
            throw new IllegalArgumentException("SuspiciousStewStack cannot be empty!");
        }
    }

    @Diff
    public SuspiciousStewEffects unwrap() {
        return suspiciousStewStack.getOrDefault(DataComponents.SUSPICIOUS_STEW_EFFECTS, SuspiciousStewEffects.EMPTY);
    }

    public PortSuspiciousStewEffects withEffectAdded(PortEntry entry) {
        suspiciousStewStack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, unwrap().withEffectAdded(entry.unwrap()));
        return this;
    }

    public List<PortEntry> effects() {
        return Lists.transform(unwrap().effects(), PortEntry::wrap);
    }

    public void applyTo(ItemStack stack) {
        if (stack == suspiciousStewStack) return;
        stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, suspiciousStewStack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS));
    }

    public record PortEntry(MobEffectHolder effect, int duration) {
        public MobEffectInstance createEffectInstance() {
            return new MobEffectInstance(effect, duration);
        }

        @Diff
        public static PortEntry wrap(SuspiciousStewEffects.Entry entry) {
            return new PortEntry(MobEffectHolder.wrap(entry.effect()), entry.duration());
        }

        @Diff
        public SuspiciousStewEffects.Entry unwrap() {
            return new SuspiciousStewEffects.Entry(effect, duration);
        }
    }
}
