package org.mesdag.portlib.diff.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelWriter;
import org.mesdag.portlib.diff.action.LevelWriter$AddFreshEntityAction;
import org.mesdag.portlib.event.entity.living.PortMobSplitEvent;
import org.mesdag.portlib.util.PortLists;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Slime.class)
public abstract class SlimeMixin implements PortSelfGetter<Slime> {
    @WrapOperation(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean recordSlimes(Level instance, Entity entity, Operation<Boolean> original, @Share("actions") LocalRef<List<LevelWriter$AddFreshEntityAction>> actions) {
        if (!(entity instanceof Mob)) {
            return original.call(instance, entity);
        }
        List<LevelWriter$AddFreshEntityAction> list = actions.get();
        if (list == null) {
            actions.set(list = new ArrayList<>());
        }
        return list.add(new LevelWriter$AddFreshEntityAction(instance, entity, original));
    }

    @Inject(method = "remove", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;remove(Lnet/minecraft/world/entity/Entity$RemovalReason;)V"))
    private void onMobSplit(CallbackInfo ci, @Share("actions") LocalRef<List<LevelWriter$AddFreshEntityAction>> actions) {
        List<LevelWriter$AddFreshEntityAction> list = actions.get();
        if (list == null) return;
        List<Mob> children = PortLists.mutableTransform(list,
                action -> (Mob) action.entity(),
                mob -> new LevelWriter$AddFreshEntityAction(portlib$self().level(), mob, args -> ((LevelWriter) args[0]).addFreshEntity((Entity) args[1]))
        );
        if (!PortMobSplitEvent.onMobSplit(portlib$self(), children).isCanceled()) {
            for (LevelWriter$AddFreshEntityAction action : list) {
                action.call();
            }
        }
    }
}
