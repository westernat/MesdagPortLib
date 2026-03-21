package org.mesdag.portlib.wrapper.common.damagesource;

import net.neoforged.neoforge.common.damagesource.IReductionFunction;
import org.mesdag.portlib.diff.Diff;

@FunctionalInterface
public interface IPortReductionFunction {
    float modify(PortDamageContainer container, float reductionIn);

    @Diff
    default IReductionFunction unwrap() {
        return (container, reductionIn) -> modify(PortDamageContainer.wrap(container), reductionIn);
    }

    @Diff
    static IPortReductionFunction wrap(IReductionFunction delegate) {
        return new Delegate(delegate);
    }

    @Diff
    record Delegate(IReductionFunction delegate) implements IPortReductionFunction {
        @Override
        public float modify(PortDamageContainer container, float reductionIn) {
            return delegate.modify(container.unwrap(), reductionIn);
        }

        @Override
        public IReductionFunction unwrap() {
            return delegate;
        }
    }
}
