package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;


public class PortLivingExperienceDropEvent extends PortLivingEvent<LivingExperienceDropEvent> implements IPortCancellableEvent {
    @Diff
    public PortLivingExperienceDropEvent(LivingExperienceDropEvent e) {
        super(e);
    }

    public int getDroppedExperience() {
        return e.getDroppedExperience();
    }

    public void setDroppedExperience(int droppedExperience) {
        e.setDroppedExperience(droppedExperience);
    }

    public @Nullable Player getAttackingPlayer() {
        return e.getAttackingPlayer();
    }

    public int getOriginalExperience() {
        return e.getOriginalExperience();
    }

    static {
        PortEventHooks.register(LivingExperienceDropEvent.class, PortLivingExperienceDropEvent.class, PortLivingExperienceDropEvent::new);
    }
}
