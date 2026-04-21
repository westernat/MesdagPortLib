package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttribute;

import java.util.function.Consumer;

public class PortAttributeRegistration extends PortRegistration<Attribute> {
    PortAttributeRegistration(String namespace) {
        super(namespace, Registries.ATTRIBUTE);
    }

    public <T extends Attribute> PortRegistryEntry<Attribute, T> register(String name, Supplier<T> valueSupplier, Consumer<PortAttributeMaker> consumer) {
        PortRegistryEntry<Attribute, T> entry = new PortRegistryEntry<>(asId(name), () -> {
            T t = valueSupplier.get();
            consumer.accept(new PortAttributeMaker(t));
            return t;
        });
        entry.object = DeferredHolder.create(registryKey, entry.identifier);
        entries.add(entry);
        return entry;
    }

    public static class PortAttributeMaker {
        private final Attribute delegate;

        PortAttributeMaker(Attribute delegate) {
            this.delegate = delegate;
        }

        public PortAttributeMaker setSyncable(boolean watch) {
            delegate.setSyncable(watch);
            return this;
        }

        public PortAttributeMaker setSentiment(PortAttribute.PortSentiment sentiment) {
            delegate.setSentiment(sentiment.unwrap());
            return this;
        }
    }
}
