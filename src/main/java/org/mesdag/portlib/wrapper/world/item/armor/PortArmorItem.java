package org.mesdag.portlib.wrapper.world.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.registries.PortRegistryEntry;

import java.util.UUID;

public class PortArmorItem {

    private static final UUID BODY_ARMOR_UUID = UUID.fromString("CA404753-1563-47E4-99D3-3D0468E77490");

    @Diff
    public static ArmorItem create(PortRegistryEntry<ArmorMaterial> materialEntry, PortArmorType portType, Item.Properties properties) {

        Object raw = materialEntry.getRaw();
        ArmorItem.Type vanillaType = portType.getVanilla();

        if (raw instanceof PortArmorMaterial portMaterial) {
            int multiplier = portMaterial.durabilityMultiplier();
            int durability = portType.isBody() ? multiplier * 16 : switch (vanillaType) {
                case HELMET -> multiplier * 11;
                case LEGGINGS -> multiplier * 15;
                case BOOTS -> multiplier * 13;
                case CHESTPLATE -> multiplier * 16;
            };
            properties.durability(durability);
        }

        return new ArmorItem(materialEntry.get(), vanillaType, properties) {
            @Override
            public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
                if (vanillaType != null && slot == vanillaType.getSlot()) {
                    return super.getDefaultAttributeModifiers(slot);
                }

                if (portType.isBody() && slot.getName().equals("body")) {
                    ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

                    builder.put(Attributes.ARMOR, new AttributeModifier(
                        BODY_ARMOR_UUID, "Armor modifier", (double)this.getDefense(), AttributeModifier.Operation.ADDITION));

                    builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                        BODY_ARMOR_UUID, "Armor toughness", (double)this.getToughness(), AttributeModifier.Operation.ADDITION));

                    return builder.build();
                }

                return super.getDefaultAttributeModifiers(slot);
            }
        };
    }
}