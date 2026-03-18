package org.mesdag.portlib.registries;

import net.minecraft.world.item.ArmorMaterial;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.wrapper.world.item.PortArmorMaterial;
import org.mesdag.portlib.wrapper.world.item.armor.PortArmorMaterialWrapper;

public class PortArmorMaterialRegistration extends PortRegistration<ArmorMaterial> {

    PortArmorMaterialRegistration(String namespace) {
        super(namespace, null);
    }

    @Diff
    public PortRegistryEntry<ArmorMaterial> register(PortArmorMaterial.Settings settings) {
        return new PortRegistryEntry.Memoized<>(
            namespace,
            settings.assetId.getPath(),
            () -> new PortArmorMaterialWrapper(settings)
        );
    }

    @Override
    public PortRegistryEntry<ArmorMaterial> register(String name, com.google.common.base.Supplier<ArmorMaterial> valueSupplier) {
        throw new UnsupportedOperationException("ArmorMaterial does not support direct registration in 1.20.1");
    }
}