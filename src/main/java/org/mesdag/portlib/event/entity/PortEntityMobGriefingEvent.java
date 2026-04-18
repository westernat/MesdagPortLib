package org.mesdag.portlib.event.entity;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.PortEventHooks;

public class PortEntityMobGriefingEvent extends PortEntityEvent<EntityMobGriefingEvent> {
    @Diff
    public PortEntityMobGriefingEvent(EntityMobGriefingEvent e) {
        super(e);
    }

    public boolean isMobGriefingEnabled() {
        return e.getEntity().level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
    }

    public void setCanGrief(boolean canGrief) {
        if (canGrief == isMobGriefingEnabled()) {
            e.setResult(Result.DEFAULT);
        } else {
            e.setResult(canGrief ? Result.ALLOW : Result.DENY);
        }
    }

    public boolean canGrief() {
        Result result = e.getResult();
        return result == Result.DEFAULT ? isMobGriefingEnabled() : result == Result.ALLOW;
    }

    static {
        PortEventHooks.register();
    }
}
