package org.mesdag.portlib.wrapper.world.item.alchemy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import org.mesdag.portlib.diff.Diff;

import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;

@SuppressWarnings("all")
public record PortPotionContents(ItemStack potionStack) {
    public PortPotionContents {
        if (potionStack.isEmpty()) {
            throw new IllegalArgumentException("PotionStack cannot be empty!");
        }
    }

    @Diff
    public Potion unwrap() {
        return PotionUtils.getPotion(potionStack);
    }

    public static ItemStack createItemStack(Item item, PotionHolder potion) {
        return PotionUtils.setPotion(item.getDefaultInstance(), potion.value());
    }

    public boolean is(PotionHolder potion) {
        return potion.value().equals(unwrap()) && customEffects().isEmpty();
    }

    public Iterable<MobEffectInstance> getAllEffects() {
        return Iterables.concat(unwrap().getEffects(), customEffects());
    }

    public void forEachEffect(Consumer<MobEffectInstance> action) {
        unwrap().getEffects().stream().map(MobEffectInstance::new).forEachOrdered(action);
    }

    public PortPotionContents withPotion(PotionHolder potion) {
        PotionUtils.setPotion(potionStack, potion.value());
        return this;
    }

    public PortPotionContents withEffectAdded(MobEffectInstance effect) {
        List<MobEffectInstance> list = customEffects();
        Collection<MobEffectInstance> customEffects = ImmutableList
                .<MobEffectInstance>builderWithExpectedSize(list.size() + 1)
                .addAll(list).add(effect).build();
        PotionUtils.setCustomEffects(potionStack, customEffects);
        return this;
    }

    public int getColor() {
        return PotionUtils.getColor(potionStack);
    }

    public static int getColor(PotionHolder potion) {
        return PotionUtils.getColor(potion.value());
    }

    public static int getColor(Collection<MobEffectInstance> effects) {
        return PotionUtils.getColor(effects);
    }

    public static OptionalInt getColorOptional(Collection<MobEffectInstance> effects) {
        return effects.isEmpty() ? OptionalInt.empty() : OptionalInt.of(getColor(effects));
    }

    public boolean hasEffects() {
        return !customEffects().isEmpty() || !unwrap().getEffects().isEmpty();
    }

    public List<MobEffectInstance> customEffects() {
        return PotionUtils.getCustomEffects(potionStack);
    }
}
