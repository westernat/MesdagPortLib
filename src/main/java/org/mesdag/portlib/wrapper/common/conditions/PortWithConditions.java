package org.mesdag.portlib.wrapper.common.conditions;

import net.minecraftforge.common.crafting.conditions.ICondition;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public record PortWithConditions<A>(List<ICondition> conditions, A carrier) {
    public PortWithConditions(A carrier, ICondition... conditions) {
        this(List.of(conditions), carrier);
    }

    public PortWithConditions(A carrier) {
        this(List.of(), carrier);
    }

    public static <A> Builder<A> builder(A carrier) {
        return new Builder<A>().withCarrier(carrier);
    }
    public static class Builder<T> {
        private final List<ICondition> conditions = new ArrayList<>();
        private T carrier;

        public Builder<T> addCondition(ICondition... condition) {
            this.conditions.addAll(List.of(condition));
            return this;
        }

        public Builder<T> addCondition(Collection<ICondition> conditions) {
            this.conditions.addAll(conditions);
            return this;
        }

        public Builder<T> withCarrier(T carrier) {
            this.carrier = carrier;
            return this;
        }

        public PortWithConditions<T> build() {
            Validate.notNull(this.carrier, "You need to supply a carrier to create a WithConditions");
            Validate.notEmpty(this.conditions, "You need to supply at least one condition to create a WithConditions");

            return new PortWithConditions<>(this.conditions, this.carrier);
        }
    }
}
