package org.mesdag.portlib.wrapper.world.item;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import org.mesdag.portlib.wrapper.resources.PortIdentifier;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class PortArmorMaterial {

    public static class Settings {
        public PortIdentifier assetId;
        public int enchantmentValue;
        public float toughness;
        public float knockbackResistance;
        public int durabilityMultiplier;
        public final Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        public Holder<SoundEvent> equipSound;
        public Supplier<Ingredient> repairIngredient;

        public static Settings create() {
            return new Settings();
        }

        public Settings assetId(PortIdentifier assetId) {
            this.assetId = assetId;
            return this;
        }

        public Settings enchantmentValue(int value) {
            this.enchantmentValue = value;
            return this;
        }

        public Settings toughness(float value) {
            this.toughness = value;
            return this;
        }

        public Settings knockbackResistance(float value) {
            this.knockbackResistance = value;
            return this;
        }

        public Settings durabilityMultiplier(int value) {
            this.durabilityMultiplier = value;
            return this;
        }

        public Settings defense(int boots, int leggings, int chestplate, int helmet) {
            this.defense.put(ArmorItem.Type.BOOTS, boots);
            this.defense.put(ArmorItem.Type.LEGGINGS, leggings);
            this.defense.put(ArmorItem.Type.CHESTPLATE, chestplate);
            this.defense.put(ArmorItem.Type.HELMET, helmet);
            return this;
        }

        public Settings defense(ArmorItem.Type type, int value) {
            this.defense.put(type, value);
            return this;
        }

        public Settings equipSound(Holder<SoundEvent> sound) {
            this.equipSound = sound;
            return this;
        }

        public Settings equipSound(SoundEvent sound) {
            this.equipSound = Holder.direct(sound);
            return this;
        }

        public Settings repairIngredient(Supplier<Ingredient> ingredient) {
            this.repairIngredient = ingredient;
            return this;
        }
    }
}