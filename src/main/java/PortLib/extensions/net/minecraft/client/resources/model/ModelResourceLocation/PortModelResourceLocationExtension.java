package PortLib.extensions.net.minecraft.client.resources.model.ModelResourceLocation;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public class PortModelResourceLocationExtension {
    public static ModelResourceLocation inventory(ResourceLocation id) {
        return new ModelResourceLocation(id, "inventory");
    }
}
