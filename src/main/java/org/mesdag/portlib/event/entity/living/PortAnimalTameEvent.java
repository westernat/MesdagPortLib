package org.mesdag.portlib.event.entity.living;

import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.AnimalTameEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortCancellableEvent;
import org.mesdag.portlib.event.PortEventHooks;

public class PortAnimalTameEvent extends PortLivingEvent<AnimalTameEvent> implements IPortCancellableEvent {
    @Diff
    public PortAnimalTameEvent(AnimalTameEvent e) {
        super(e);
    }

    public Animal getAnimal() {
        return e.getAnimal();
    }

    public Player getTamer() {
        return e.getTamer();
    }

    static {
        PortEventHooks.register(AnimalTameEvent.class, PortAnimalTameEvent.class, PortAnimalTameEvent::new);
    }
}
