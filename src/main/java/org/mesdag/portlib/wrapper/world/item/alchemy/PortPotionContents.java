package org.mesdag.portlib.wrapper.world.item.alchemy;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
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
    public PotionContents unwrap() {
        return potionStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    public static ItemStack createItemStack(Item item, PotionHolder potion) {
        return PotionContents.createItemStack(item, potion);
    }

    public boolean is(PotionHolder potion) {
        return unwrap().is(potion);
    }

    public Iterable<MobEffectInstance> getAllEffects() {
        return unwrap().getAllEffects();
    }

    public void forEachEffect(Consumer<MobEffectInstance> action) {
        unwrap().forEachEffect(action);
    }

    public PortPotionContents withPotion(PotionHolder potion) {
        potionStack.set(DataComponents.POTION_CONTENTS, unwrap().withPotion(potion));
        return this;
    }

    public PortPotionContents withEffectAdded(MobEffectInstance effect) {
        potionStack.set(DataComponents.POTION_CONTENTS, unwrap().withEffectAdded(effect));
        return this;
    }

    public int getColor() {
        return unwrap().getColor();
    }

    public static int getColor(PotionHolder potion) {
        return PotionContents.getColor(potion);
    }

    public static int getColor(Collection<MobEffectInstance> effects) {
        return PotionContents.getColor(effects);
    }

    public static OptionalInt getColorOptional(Collection<MobEffectInstance> effects) {
        return PotionContents.getColorOptional(effects);
    }

    public boolean hasEffects() {
        return unwrap().hasEffects();
    }

    public List<MobEffectInstance> customEffects() {
        return unwrap().customEffects();
    }

    public void applyTo(ItemStack stack) {
        if (stack == potionStack) return;
        stack.set(DataComponents.POTION_CONTENTS, potionStack.get(DataComponents.POTION_CONTENTS));
    }
}
