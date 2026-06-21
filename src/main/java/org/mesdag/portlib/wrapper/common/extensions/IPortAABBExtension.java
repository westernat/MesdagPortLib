package org.mesdag.portlib.wrapper.common.extensions;

import PortLib.extensions.net.minecraft.world.phys.AABB.PortAABBExtension;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("all")
public interface IPortAABBExtension {

    private AABB self() {
        return (AABB) (Object) this;
    }

    default Vec3 getMinPosition() {
        return PortAABBExtension.getMinPosition(self());
    }

    default Vec3 getMaxPosition() {
        return PortAABBExtension.getMaxPosition(self());
    }

    static IPortAABBExtension of(AABB aabb) {
        return (IPortAABBExtension) (Object) aabb;
    }
}
