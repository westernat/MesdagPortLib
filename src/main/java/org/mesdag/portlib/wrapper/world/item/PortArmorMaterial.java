package org.mesdag.portlib.wrapper.world.item;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.sounds.SoundEventHolder;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PortArmorMaterial implements ArmorMaterial {
    private final Map<ArmorItem.Type, Integer> defense;
    private final int enchantmentValue;
    private final Holder<SoundEvent> equipSound;
    private final Supplier<Ingredient> repairIngredient;
    private final List<PortLayer> layers;
    private final float toughness;
    private final float knockbackResistance;

    private @Nullable String name;

    public PortArmorMaterial(
            Map<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            Supplier<Ingredient> repairIngredient,
            List<PortLayer> layers,
            float toughness,
            float knockbackResistance
    ) {
        this.defense = defense;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.repairIngredient = repairIngredient;
        this.layers = layers;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    public PortArmorMaterial(Settings settings) {
        this.name = settings.name;
        this.defense = settings.defense;
        this.enchantmentValue = settings.enchantmentValue;
        this.equipSound = settings.equipSound;
        this.repairIngredient = settings.repairIngredient;
        this.layers = settings.layers;
        this.toughness = settings.toughness;
        this.knockbackResistance = settings.knockbackResistance;
    }

    public PortArmorMaterial setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return switch (type) {
            case HELMET -> 11;
            case LEGGINGS -> 15;
            case BOOTS -> 13;
            default -> 16;
        };
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return defense.getOrDefault(type, 0);
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound.value();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        if (name != null) {
            return name;
        }
        return layers.isEmpty() ? "empty" : layers.get(0).suffix;
    }

    // todo
    public List<PortLayer> getLayers() {
        return layers;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    public static class PortLayer {
        private final ResourceLocation assetName;
        private final String suffix;
        private final boolean dyeable;
        private final ResourceLocation innerTexture;
        private final ResourceLocation outerTexture;

        public PortLayer(ResourceLocation assetName, String suffix, boolean dyeable) {
            this.assetName = assetName;
            this.suffix = suffix;
            this.dyeable = dyeable;
            this.innerTexture = this.resolveTexture(true);
            this.outerTexture = this.resolveTexture(false);
        }

        public PortLayer(ResourceLocation assetName) {
            this(assetName, "", false);
        }

        private ResourceLocation resolveTexture(boolean innerTexture) {
            return assetName.withPath(path -> "textures/models/armor/" + path + "_layer_" + (innerTexture ? 2 : 1) + suffix + ".png");
        }

        public ResourceLocation texture(boolean inner) {
            return inner ? innerTexture : outerTexture;
        }

        public boolean dyeable() {
            return dyeable;
        }
    }

    public static class Settings {
        public String name;
        public int enchantmentValue;
        public float toughness;
        public float knockbackResistance;
        public int durabilityFactor;
        public final Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        public SoundEventHolder equipSound;
        public Supplier<Ingredient> repairIngredient;
        @Diff
        public final List<PortLayer> layers = Lists.newArrayList();

        Settings() {}

        public static Settings create() {
            return new Settings();
        }

        public Settings name(String name) {
            this.name = name;
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

        public Settings durabilityFactor(int value) {
            this.durabilityFactor = value;
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

        public Settings equipSound(SoundEventHolder sound) {
            this.equipSound = sound;
            return this;
        }

        public Settings repairIngredient(Supplier<Ingredient> ingredient) {
            this.repairIngredient = ingredient;
            return this;
        }

        public Settings layer(PortLayer layer) {
            this.layers.add(layer);
            return this;
        }
    }
}
