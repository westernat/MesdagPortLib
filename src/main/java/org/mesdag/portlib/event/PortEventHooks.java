package org.mesdag.portlib.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.level.PortChunkWatchEvent;
import org.mesdag.portlib.event.registries.PortModifyRegistriesEvent;

public class PortEventHooks {
    public static void init() {
        PortEventHandler.wrapEvent(false, RegisterCapabilitiesEvent.class, e -> new PortModifyRegistriesEvent());
    }

    @Diff
    public static void fireChunkSent(ServerPlayer entity, LevelChunk chunk, ServerLevel level) {
        PortEventHandler.postEvent(new PortChunkWatchEvent.Sent(entity, chunk, level));
    }
}
