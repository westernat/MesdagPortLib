package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.diff.PortModelManager;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.client.PortRegisterMaterialAtlasesEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelManager.class)
public abstract class ModelManagerMixin {
    @Inject(method = "loadModels", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 1))
    private void catching(CallbackInfoReturnable<?> cir, @Local(argsOnly = true) Map<ResourceLocation, AtlasSet.StitchResult> atlasPreparations) {
        PortModelManager.atlasPreparations = atlasPreparations;
    }

    @Inject(method = "loadModels", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 2))
    private void releasing(CallbackInfoReturnable<?> cir) {
        PortModelManager.atlasPreparations = null;
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/AtlasSet;<init>(Ljava/util/Map;Lnet/minecraft/client/renderer/texture/TextureManager;)V"))
    private Map<ResourceLocation, ResourceLocation> gatherMaterialAtlases(Map<ResourceLocation, ResourceLocation> atlasMap) {
        var event = new PortRegisterMaterialAtlasesEvent(atlasMap);
        PortEventHandler.postEvent(event);
        return event.getAtlases();
    }
}
