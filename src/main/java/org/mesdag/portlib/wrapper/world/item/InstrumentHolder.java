package org.mesdag.portlib.wrapper.world.item;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Instrument;
import org.mesdag.portlib.wrapper.core.PortHolder;

@SuppressWarnings("all")
public class InstrumentHolder implements PortHolder<Instrument> {
    private final Holder<Instrument> delegate;

    private InstrumentHolder(Holder<Instrument> value) {
        this.delegate = value;
    }

    @Override
    public Holder<Instrument> delegate() {
        return delegate;
    }

    public static InstrumentHolder wrap(Holder<Instrument> value) {
        return new InstrumentHolder(value);
    }
}
