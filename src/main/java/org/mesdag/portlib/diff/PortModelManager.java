package org.mesdag.portlib.diff;

import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

@Diff
public class PortModelManager {
    public static Map<ResourceLocation, AtlasSet.StitchResult> atlasPreparations;
}
