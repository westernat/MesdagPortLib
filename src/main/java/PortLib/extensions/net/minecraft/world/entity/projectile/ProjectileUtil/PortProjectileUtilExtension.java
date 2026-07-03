package PortLib.extensions.net.minecraft.world.entity.projectile.ProjectileUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class PortProjectileUtilExtension {
    public static HitResult getHitResultOnMoveVector(Entity projectile, Predicate<Entity> filter) {
        Vec3 vec3 = projectile.getDeltaMovement();
        Level level = projectile.level();
        Vec3 vec31 = projectile.position();
        return getHitResult(vec31, projectile, filter, vec3, level, 0.3F, ClipContext.Block.COLLIDER);
    }

    public static HitResult getHitResultOnMoveVector(Entity projectile, Predicate<Entity> filter, ClipContext.Block clipContext) {
        Vec3 vec3 = projectile.getDeltaMovement();
        Level level = projectile.level();
        Vec3 vec31 = projectile.position();
        return getHitResult(vec31, projectile, filter, vec3, level, 0.3F, clipContext);
    }

    public static HitResult getHitResultOnViewVector(Entity projectile, Predicate<Entity> filter, double scale) {
        Vec3 vec3 = projectile.getViewVector(0.0F).scale(scale);
        Level level = projectile.level();
        Vec3 vec31 = projectile.getEyePosition();
        return getHitResult(vec31, projectile, filter, vec3, level, 0.0F, ClipContext.Block.COLLIDER);
    }

    public static HitResult getHitResult(Vec3 pos, Entity projectile, Predicate<Entity> filter, Vec3 deltaMovement, Level level, float margin, ClipContext.Block clipContext) {
        Vec3 vec3 = pos.add(deltaMovement);
        HitResult hitresult = level.clip(new ClipContext(pos, vec3, clipContext, ClipContext.Fluid.NONE, projectile));
        if (hitresult.getType() != HitResult.Type.MISS) {
            vec3 = hitresult.getLocation();
        }

        HitResult hitresult1 = ProjectileUtil.getEntityHitResult(
                level, projectile, pos, vec3, projectile.getBoundingBox().expandTowards(deltaMovement).inflate(1.0), filter, margin
        );
        if (hitresult1 != null) {
            hitresult = hitresult1;
        }

        return hitresult;
    }
}
