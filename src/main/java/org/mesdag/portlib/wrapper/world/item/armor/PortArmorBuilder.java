package org.mesdag.portlib.wrapper.world.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.PortIdentifier;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public class PortArmorBuilder {
    private final Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
    private int enchantmentValue;
    private Holder<SoundEvent> equipSound;
    private Supplier<Ingredient> repairIngredient;
    private float toughness;
    private float knockbackResistance;
    private PortIdentifier assetId;

    @Diff
    private int bodyDefense;

    @Diff
    private int durabilityMultiplier = 15;

    public static PortArmorBuilder create() {
        return new PortArmorBuilder();
    }

    @Diff
    public PortArmorBuilder defense(int helmet, int chestplate, int leggings, int boots, int body) {
        defense.put(ArmorItem.Type.HELMET, helmet);
        defense.put(ArmorItem.Type.CHESTPLATE, chestplate);
        defense.put(ArmorItem.Type.LEGGINGS, leggings);
        defense.put(ArmorItem.Type.BOOTS, boots);
        defense.put(ArmorItem.Type.BODY, body);
        this.bodyDefense = body;
        return this;
    }

    @Diff
    public PortArmorBuilder durability(int multiplier) {
        this.durabilityMultiplier = multiplier;
        return this;
    }

    public PortArmorBuilder enchantmentValue(int value) {
        this.enchantmentValue = value;
        return this;
    }

    public PortArmorBuilder equipSound(Holder<SoundEvent> sound) {
        this.equipSound = sound;
        return this;
    }

    public PortArmorBuilder repairIngredient(Supplier<Ingredient> ingredient) {
        this.repairIngredient = ingredient;
        return this;
    }

    public PortArmorBuilder toughness(float toughness) {
        this.toughness = toughness;
        return this;
    }

    public PortArmorBuilder knockbackResistance(float knockbackResistance) {
        this.knockbackResistance = knockbackResistance;
        return this;
    }

    public PortArmorBuilder assetId(PortIdentifier id) {
        this.assetId = id;
        return this;
    }

    public PortArmorBuilder assetId(String location) {
        this.assetId = PortIdentifier.parse(location);
        return this;
    }

    @Diff
    public PortArmorMaterial build() {
        return new PortArmorMaterial(
            defense,
            bodyDefense,
            enchantmentValue,
            equipSound,
            repairIngredient,
            toughness,
            knockbackResistance,
            assetId,
            durabilityMultiplier
        );
    }
}