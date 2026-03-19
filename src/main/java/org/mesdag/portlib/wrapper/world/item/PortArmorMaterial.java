package org.mesdag.portlib.wrapper.world.item;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.sounds.SoundEventHolder;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class PortArmorMaterial {
    private final ArmorMaterial delegate;

    private PortArmorMaterial(ArmorMaterial delegate) {
        this.delegate = delegate;
    }

    private static int getDurability(ArmorItem.Type type, int durabilityFactor) {
        return switch (type) {
            case HELMET -> 11;
            case CHESTPLATE -> 16;
            case LEGGINGS -> 15;
            case BOOTS -> 13;
        } * durabilityFactor;
    }

    public PortArmorMaterial(Settings settings) {
        this.delegate = new ArmorMaterial() {
            @Override
            public int getDurabilityForType(ArmorItem.Type type) {
                return getDurability(type, settings.durabilityFactor);
            }

            @Override
            public int getDefenseForType(ArmorItem.Type type) {
                return settings.defense.getOrDefault(type, 0);
            }

            @Override
            public int getEnchantmentValue() {
                return settings.enchantmentValue;
            }

            @Override
            public SoundEvent getEquipSound() {
                return settings.equipSound.value();
            }

            @Override
            public Ingredient getRepairIngredient() {
                return settings.repairIngredient.get();
            }

            @Override
            public String getName() {
                return settings.name;
            }

            @Override
            public float getToughness() {
                return settings.toughness;
            }

            @Override
            public float getKnockbackResistance() {
                return settings.knockbackResistance;
            }
        };
    }

    @Diff
    public ArmorMaterial unwrap() {
        return delegate;
    }

    @Diff
    public static PortArmorMaterial wrap(ArmorMaterial delegate) {
        return new PortArmorMaterial(delegate);
    }

    public int getDefense(ArmorItem.Type type) {
        return delegate.getDefenseForType(type);
    }

    public int getEnchantmentValue() {
        return delegate.enchantmentValue();
    }

    public Holder<SoundEvent> getEquipSound() {
        return delegate.equipSound();
    }

    public Supplier<Ingredient> getRepairIngredient() {
        return delegate.repairIngredient();
    }

    public List<PortLayer> getLayers() {
        return layers;
    }

    public float getToughness() {
        return delegate.toughness();
    }

    public float getKnockbackResistance() {
        return delegate.knockbackResistance();
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

        private ResourceLocation resolveTexture(boolean innerTexture) {
            return this.assetName
                    .withPath(p_324187_ -> "textures/models/armor/" + this.assetName.getPath() + "_layer_" + (innerTexture ? 2 : 1) + this.suffix + ".png");
        }

        public ResourceLocation texture(boolean innerTexture) {
            return innerTexture ? this.innerTexture : this.outerTexture;
        }

        public boolean dyeable() {
            return this.dyeable;
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

        public Settings equipSound(Holder<SoundEvent> sound) {
            this.equipSound = SoundEventHolder.wrap(sound);
            return this;
        }

        public Settings equipSound(SoundEvent sound) {
            this.equipSound = SoundEventHolder.wrap(sound);
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
