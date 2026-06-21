package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.CustomSpawner;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.diff.attachment.CPortAttachmentHolder;
import org.mesdag.portlib.diff.attachment.PortAttachmentSync;
import org.mesdag.portlib.diff.attachment.PortLevelAttachmentsSavedData;
import org.mesdag.portlib.event.PortEventHandler;
import org.mesdag.portlib.event.level.PortModifyCustomSpawnersEvent;
import org.mesdag.portlib.event.tick.PortEntityTickEvent;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements CPortAttachmentHolder, PortSelfGetter<ServerLevel> {
    @Mutable
    @Shadow
    @Final
    private List<CustomSpawner> customSpawners;

    @Override
    public void portlib$syncAttach(PortAttachmentType<?> type) {
        PortAttachmentSync.syncLevelUpdate(portlib$self(), type);
    }

    @Inject(method = "initCapabilities", at = @At("TAIL"), remap = false)
    private void initAttachments(CallbackInfo ci) {
        PortLevelAttachmentsSavedData.init(portlib$self());

        PortModifyCustomSpawnersEvent event = new PortModifyCustomSpawnersEvent(portlib$self(), customSpawners);
        PortEventHandler.postEvent(event);
        this.customSpawners = event.getCustomSpawners();
    }

    @WrapOperation(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"))
    private void fireEntityTick(Entity instance, Operation<Void> original) {
        if (!PortEventHandler.postEventWithReturn(new PortEntityTickEvent.Pre(instance)).isCanceled()) {
            original.call(instance);
            PortEventHandler.postEvent(new PortEntityTickEvent.Post(instance));
        }
    }
}
