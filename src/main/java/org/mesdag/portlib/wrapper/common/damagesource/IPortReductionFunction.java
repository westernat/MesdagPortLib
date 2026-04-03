package org.mesdag.portlib.wrapper.common.damagesource;

@FunctionalInterface
public interface IPortReductionFunction {
    float modify(PortDamageContainer container, float reductionIn);
}
