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
    private final List<PortLayer> layers;

    private PortArmorMaterial(ArmorMaterial delegate) {
        this.delegate = delegate;
        this.layers = Lists.transform(delegate.layers(), PortLayer::wrap);
    }

    public PortArmorMaterial(Settings settings) {
        this(new ArmorMaterial(
                settings.defense,
                settings.enchantmentValue,
                settings.equipSound,
                settings.repairIngredient,
                settings.layers,
                settings.toughness,
                settings.knockbackResistance
        ));
    }

    @Diff
    public static PortArmorMaterial wrap(ArmorMaterial delegate) {
        return new PortArmorMaterial(delegate);
    }

    @Diff
    public ArmorMaterial unwrap() {
        return delegate;
    }

    public int getDefense(ArmorItem.Type type) {
        return delegate.getDefense(type);
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
        private final ArmorMaterial.Layer delegate;

        public PortLayer(ResourceLocation assetName, String suffix, boolean dyeable) {
            this.delegate = new ArmorMaterial.Layer(assetName, suffix, dyeable);
        }

        public PortLayer(ResourceLocation assetName) {
            this.delegate = new ArmorMaterial.Layer(assetName);
        }

        private PortLayer(ArmorMaterial.Layer delegate) {
            this.delegate = delegate;
        }

        @Diff
        public static PortLayer wrap(ArmorMaterial.Layer delegate) {
            return new PortLayer(delegate);
        }

        @Diff
        public ArmorMaterial.Layer unwrap() {
            return delegate;
        }

        public ResourceLocation texture(boolean innerTexture) {
            return delegate.texture(innerTexture);
        }

        public boolean dyeable() {
            return delegate.dyeable();
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
        public final List<ArmorMaterial.Layer> layers = Lists.newArrayList();

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
            this.defense.put(ArmorItem.Type.HELMET, helmet);
            this.defense.put(ArmorItem.Type.CHESTPLATE, chestplate);
            this.defense.put(ArmorItem.Type.LEGGINGS, leggings);
            this.defense.put(ArmorItem.Type.BOOTS, boots);
            this.defense.put(ArmorItem.Type.BODY, Math.max(chestplate - 1, 0));
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
            this.layers.add(layer.unwrap());
            return this;
        }
    }
}
