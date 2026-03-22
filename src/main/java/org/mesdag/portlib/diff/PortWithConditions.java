package org.mesdag.portlib.diff;

import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.List;

@Diff
public record PortWithConditions<A>(List<ICondition> conditions, A carrier) {
    public PortWithConditions(A carrier, ICondition... conditions) {
        this(List.of(conditions), carrier);
    }

    public PortWithConditions(A carrier) {
        this(List.of(), carrier);
    }
}
