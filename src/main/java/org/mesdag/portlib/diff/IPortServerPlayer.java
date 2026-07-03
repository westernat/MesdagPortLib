package org.mesdag.portlib.diff;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.mesdag.portlib.wrapper.PortSelfGetter;
import org.mesdag.portlib.wrapper.common.extensions.IPortServerPlayerExtension;

public interface IPortServerPlayer extends PortSelfGetter<ServerPlayer>, IPortServerPlayerExtension {
    Vec3 portlib$getKnownMovement();

    void portlib$setKnownMovement(Vec3 knownMovement);

    static IPortServerPlayer of(ServerPlayer player) {
        return (IPortServerPlayer) player;
    }
}
