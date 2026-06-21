package org.mesdag.portlib.event.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortRegisterColorHandlersEvent<E extends RegisterColorHandlersEvent> extends PortEvent<E> implements IPortModBusEvent {
    @Diff
    public PortRegisterColorHandlersEvent(E e) {
        super(e);
    }

    public static class Block extends PortRegisterColorHandlersEvent<RegisterColorHandlersEvent.Block> {
        @Diff
        public Block(RegisterColorHandlersEvent.Block e) {
            super(e);
        }

        public BlockColors getBlockColors() {
            return e.getBlockColors();
        }

        public void register(BlockColor blockColor, net.minecraft.world.level.block.Block... blocks) {
            e.register(blockColor, blocks);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class Item extends PortRegisterColorHandlersEvent<RegisterColorHandlersEvent.Item> {
        @Diff
        public Item(RegisterColorHandlersEvent.Item e) {
            super(e);
        }

        public ItemColors getItemColors() {
            return e.getItemColors();
        }

        public BlockColors getBlockColors() {
            return e.getBlockColors();
        }

        public void register(ItemColor itemColor, ItemLike... items) {
            e.register(itemColor, items);
        }

        static {
            PortEventHooks.register();
        }
    }

    public static class ColorResolvers extends PortRegisterColorHandlersEvent<RegisterColorHandlersEvent.ColorResolvers> {
        @Diff
        public ColorResolvers(RegisterColorHandlersEvent.ColorResolvers e) {
            super(e);
        }

        public void register(ColorResolver resolver) {
            e.register(resolver);
        }

        static {
            PortEventHooks.register();
        }
    }
}
