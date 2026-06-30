package org.mesdag.portlib.event.other;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.mesdag.portlib.component.PortDataComponentMap;
import org.mesdag.portlib.component.PortDataComponentPatch;
import org.mesdag.portlib.component.PortDataComponentType;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.diff.IPortItem;
import org.mesdag.portlib.event.PortEventHandler;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PortModifyDefaultComponentsEvent extends Event implements IModBusEvent {
    @Diff
    public PortModifyDefaultComponentsEvent() {}

    public void modify(ItemLike item, Consumer<PortDataComponentPatch.Builder> patch) {
        PortDataComponentPatch.Builder builder = PortDataComponentPatch.builder();
        patch.accept(builder);
        Reference2ObjectMap<PortDataComponentType<?>, Optional<?>> compPatch = builder.build();
        if (!compPatch.isEmpty()) {
            if (!canModifyComponents()) {
                throw new IllegalStateException("Default components cannot be modified now!");
            }
            PortDataComponentMap.PortBuilder builder2 = PortDataComponentMap.builder();
            IPortItem portItem = IPortItem.of(item.asItem());
            for (Map.Entry<PortDataComponentType<?>, Object> entry : portItem.portlib$getComponents().entrySet()) {
                builder2.set((PortDataComponentType) entry.getKey(), entry.getValue());
            }
            for (Map.Entry<PortDataComponentType<?>, Optional<?>> entry : compPatch.entrySet()) {
                builder2.set((PortDataComponentType) entry.getKey(), entry.getValue());
            }
            portItem.portlib$setComponents(builder2.getMap());
        }
    }

    public void modifyMatching(Predicate<? super Item> predicate, Consumer<PortDataComponentPatch.Builder> patch) {
        getAllItems().filter(predicate).forEach(item -> modify(item, patch));
    }

    public Stream<Item> getAllItems() {
        return ForgeRegistries.ITEMS.getValues().stream();
    }

    private static boolean canModifyComponents;

    @Diff
    public static void modifyComponents() {
        canModifyComponents = true;
        PortEventHandler.postEvent(new PortModifyDefaultComponentsEvent());
        canModifyComponents = false;
    }

    @Diff
    public static boolean canModifyComponents() {
        return canModifyComponents;
    }
}
