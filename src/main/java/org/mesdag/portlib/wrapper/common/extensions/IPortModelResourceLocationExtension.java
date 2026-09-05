package org.mesdag.portlib.wrapper.common.extensions;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public interface IPortModelResourceLocationExtension {
    static ModelResourceLocation inventory(ResourceLocation id) {
        return new ModelResourceLocation(id, "inventory");
    }
}
