package org.mesdag.portlib.event;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import org.mesdag.portlib.diff.Diff;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;


public final class PortModifyDefaultComponentsEvent extends PortEvent implements IPortModBusEvent {
    private final ModifyDefaultComponentsEvent e;

    @Diff
    public PortModifyDefaultComponentsEvent(ModifyDefaultComponentsEvent e) {
        super();
        this.e = e;
    }

    public void modify(ItemLike item, Consumer<DataComponentPatch.Builder> patch) {
        e.modify(item, patch);
    }

    public void modifyMatching(Predicate<? super Item> predicate, Consumer<DataComponentPatch.Builder> patch) {
        e.modifyMatching(predicate, patch);
    }

    public Stream<Item> getAllItems() {
        return e.getAllItems();
    }

    static {
        PortEventHooks.register(ModifyDefaultComponentsEvent.class, PortModifyDefaultComponentsEvent.class, PortModifyDefaultComponentsEvent::new);
    }
}