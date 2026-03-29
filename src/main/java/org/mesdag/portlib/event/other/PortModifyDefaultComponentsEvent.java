package org.mesdag.portlib.event.other;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import org.mesdag.portlib.component.PortDataComponentPatch$Builder;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PortModifyDefaultComponentsEvent extends PortEvent<ModifyDefaultComponentsEvent> implements IPortModBusEvent {
    @Diff
    public PortModifyDefaultComponentsEvent(ModifyDefaultComponentsEvent e) {
        super(e);
    }

    public void modify(ItemLike item, Consumer<PortDataComponentPatch$Builder> patch) {
        e.modify(item, builder -> patch.accept(PortDataComponentPatch$Builder.wrap(builder)));
    }

    public void modifyMatching(Predicate<? super Item> predicate, Consumer<PortDataComponentPatch$Builder> patch) {
        e.modifyMatching(predicate, builder -> patch.accept(PortDataComponentPatch$Builder.wrap(builder)));
    }

    public Stream<Item> getAllItems() {
        return e.getAllItems();
    }

    static {
        PortEventHooks.register(ModifyDefaultComponentsEvent.class, PortModifyDefaultComponentsEvent.class, PortModifyDefaultComponentsEvent::new);
    }
}
