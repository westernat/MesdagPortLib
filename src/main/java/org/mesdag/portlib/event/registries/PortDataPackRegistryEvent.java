package org.mesdag.portlib.event.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.jetbrains.annotations.Nullable;
import org.mesdag.portlib.diff.Diff;
import org.mesdag.portlib.event.IPortModBusEvent;
import org.mesdag.portlib.event.PortEvent;
import org.mesdag.portlib.event.PortEventHooks;

public abstract class PortDataPackRegistryEvent<E extends DataPackRegistryEvent> extends PortEvent<E> implements IPortModBusEvent {
    @Diff
    public PortDataPackRegistryEvent(E e) {
        super(e);
    }

    public static final class PortNewRegistry extends PortDataPackRegistryEvent<DataPackRegistryEvent.NewRegistry> {
        @Diff
        public PortNewRegistry(DataPackRegistryEvent.NewRegistry e) {
            super(e);
        }

        public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec) {
            e.dataPackRegistry(registryKey, codec);
        }

        public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, @Nullable Codec<T> networkCodec) {
            e.dataPackRegistry(registryKey, codec, networkCodec);
        }

//        public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, @Nullable Codec<T> networkCodec, Consumer<PortRegistryMaker<T>> consumer) {
//            e.dataPackRegistry(registryKey, codec, networkCodec, builder -> {
//                PortRegistryMaker<T> maker = new PortRegistryMaker<>();
//                maker.builder = builder;
//                consumer.accept(maker);
//            });
//        }

        static {
            PortEventHooks.register();
        }
    }
}
