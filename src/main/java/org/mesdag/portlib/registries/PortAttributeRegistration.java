package org.mesdag.portlib.registries;

import com.google.common.base.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.mesdag.portlib.diff.IPortAttribute;
import org.mesdag.portlib.wrapper.world.entity.ai.attributes.PortAttribute;

import java.util.function.Consumer;

public class PortAttributeRegistration extends PortRegistration<Attribute> {
    PortAttributeRegistration(String namespace) {
        super(namespace, Registries.ATTRIBUTE);
    }

    public <T extends Attribute> PortRegistryEntry<Attribute, T> register(String name, Supplier<T> valueSupplier, Consumer<AttributeMaker> consumer) {
        return super.register(name, () -> {
            T t = valueSupplier.get();
            consumer.accept(new AttributeMaker(t));
            return t;
        });
    }

    public static class AttributeMaker {
        private final Attribute delegate;

        AttributeMaker(Attribute delegate) {
            this.delegate = delegate;
        }

        public AttributeMaker setSyncable(boolean watch) {
            delegate.setSyncable(watch);
            return this;
        }

        public AttributeMaker setSentiment(PortAttribute.PortSentiment sentiment) {
            IPortAttribute.of(delegate).portlib$setSentiment(sentiment);
            return this;
        }
    }
}
